package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import com.plus33.erp.inventory.event.ReplenishmentRefreshEvent;
import com.plus33.erp.inventory.mapper.ReplenishmentMapper;
import com.plus33.erp.inventory.repository.*;
import com.plus33.erp.organization.entity.*;
import com.plus33.erp.organization.repository.*;
import com.plus33.erp.procurement.dto.PurchaseRequestItemRequest;
import com.plus33.erp.procurement.dto.PurchaseRequestRequest;
import com.plus33.erp.procurement.dto.PurchaseRequestResponse;
import com.plus33.erp.procurement.entity.PurchaseRequest;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.procurement.repository.PurchaseRequestRepository;
import com.plus33.erp.procurement.repository.SupplierRepository;
import com.plus33.erp.procurement.service.PurchaseRequestService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ReplenishmentServiceImpl implements ReplenishmentService {

    private final ReplenishmentRuleRepository replenishmentRuleRepository;
    private final ReplenishmentSuggestionRepository replenishmentSuggestionRepository;
    private final InventoryStockRepository inventoryStockRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseRepository warehouseRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestService purchaseRequestService;
    private final InventoryTransferRepository inventoryTransferRepository;
    private final InventoryTransferService inventoryTransferService;
    private final ReplenishmentMapper replenishmentMapper;
    private final ApplicationEventPublisher eventPublisher;

    public ReplenishmentServiceImpl(
            ReplenishmentRuleRepository replenishmentRuleRepository,
            ReplenishmentSuggestionRepository replenishmentSuggestionRepository,
            InventoryStockRepository inventoryStockRepository,
            CompanyRepository companyRepository,
            WarehouseRepository warehouseRepository,
            StoreRepository storeRepository,
            ProductRepository productRepository,
            SupplierRepository supplierRepository,
            PurchaseRequestRepository purchaseRequestRepository,
            PurchaseRequestService purchaseRequestService,
            InventoryTransferRepository inventoryTransferRepository,
            InventoryTransferService inventoryTransferService,
            ReplenishmentMapper replenishmentMapper,
            ApplicationEventPublisher eventPublisher) {
        this.replenishmentRuleRepository = replenishmentRuleRepository;
        this.replenishmentSuggestionRepository = replenishmentSuggestionRepository;
        this.inventoryStockRepository = inventoryStockRepository;
        this.companyRepository = companyRepository;
        this.warehouseRepository = warehouseRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.purchaseRequestService = purchaseRequestService;
        this.inventoryTransferRepository = inventoryTransferRepository;
        this.inventoryTransferService = inventoryTransferService;
        this.replenishmentMapper = replenishmentMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public IdempotentCreateResult<ReplenishmentRuleResponse> createRule(ReplenishmentRuleRequest request) {
        // Idempotency check
        Optional<ReplenishmentRule> existing = replenishmentRuleRepository.findByClientReferenceId(request.clientReferenceId());
        if (existing.isPresent()) {
            ReplenishmentRule rule = existing.get();
            validateReplayPayload(rule, request);
            return new IdempotentCreateResult<>(replenishmentMapper.toResponse(rule), false);
        }

        // XOR location checks
        if ((request.warehouseId() != null && request.storeId() != null) ||
                (request.warehouseId() == null && request.storeId() == null)) {
            throw new BusinessException("Replenishment rule must target exactly one location (Warehouse or Store)");
        }

        // Validate quantities constraint
        validateQuantities(request.minQuantity(), request.maxQuantity(), request.reorderPoint(), request.reorderQuantity());

        // Check if rule already exists for the same product and location
        Optional<ReplenishmentRule> duplicateCheck = request.warehouseId() != null
                ? replenishmentRuleRepository.findByProductIdAndWarehouseId(request.productId(), request.warehouseId())
                : replenishmentRuleRepository.findByProductIdAndStoreId(request.productId(), request.storeId());
        if (duplicateCheck.isPresent()) {
            throw new BusinessException("A replenishment rule already exists for this product and location");
        }

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        if (!Boolean.TRUE.equals(company.getActive())) {
            throw new BusinessException("Company is inactive");
        }

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + request.productId()));
        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new BusinessException("Product is inactive");
        }

        Warehouse warehouse = null;
        Store store = null;
        Long locCompanyId;

        if (request.warehouseId() != null) {
            warehouse = warehouseRepository.findById(request.warehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.warehouseId()));
            locCompanyId = warehouse.getRegion().getCompany().getId();
        } else {
            store = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.storeId()));
            locCompanyId = store.getRegion().getCompany().getId();
        }

        if (!locCompanyId.equals(company.getId())) {
            throw new BusinessException("Location must belong to the rule company");
        }

        ReplenishmentRule rule = ReplenishmentRule.builder()
                .company(company)
                .product(product)
                .warehouse(warehouse)
                .store(store)
                .minQuantity(request.minQuantity())
                .maxQuantity(request.maxQuantity())
                .reorderPoint(request.reorderPoint())
                .reorderQuantity(request.reorderQuantity())
                .leadTimeDays(request.leadTimeDays() != null ? request.leadTimeDays() : 0)
                .active(request.active() == null || request.active())
                .clientReferenceId(request.clientReferenceId())
                .build();

        ReplenishmentRule saved = replenishmentRuleRepository.save(rule);
        eventPublisher.publishEvent(new ReplenishmentRefreshEvent(this));

        return new IdempotentCreateResult<>(replenishmentMapper.toResponse(saved), true);
    }

    @Override
    @Transactional
    public ReplenishmentRuleResponse updateRule(Long ruleId, ReplenishmentRuleUpdateRequest request) {
        ReplenishmentRule rule = replenishmentRuleRepository.findById(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("Replenishment Rule not found with ID: " + ruleId));

        BigDecimal min = request.minQuantity() != null ? request.minQuantity() : rule.getMinQuantity();
        BigDecimal max = request.maxQuantity() != null ? request.maxQuantity() : rule.getMaxQuantity();
        BigDecimal pt = request.reorderPoint() != null ? request.reorderPoint() : rule.getReorderPoint();
        BigDecimal qty = request.reorderQuantity() != null ? request.reorderQuantity() : rule.getReorderQuantity();

        validateQuantities(min, max, pt, qty);

        rule.setMinQuantity(min);
        rule.setMaxQuantity(max);
        rule.setReorderPoint(pt);
        rule.setReorderQuantity(qty);

        if (request.leadTimeDays() != null) {
            rule.setLeadTimeDays(request.leadTimeDays());
        }
        if (request.active() != null) {
            rule.setActive(request.active());
        }

        ReplenishmentRule saved = replenishmentRuleRepository.save(rule);
        eventPublisher.publishEvent(new ReplenishmentRefreshEvent(this));

        return replenishmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteRule(Long ruleId) {
        ReplenishmentRule rule = replenishmentRuleRepository.findById(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("Replenishment Rule not found with ID: " + ruleId));

        // Check for active suggestions (OPEN or ACKNOWLEDGED)
        List<ReplenishmentSuggestion> activeSuggestions = replenishmentSuggestionRepository.findByRuleIdAndStatusIn(
                ruleId, Arrays.asList(ReplenishmentSuggestionStatus.OPEN, ReplenishmentSuggestionStatus.ACKNOWLEDGED)
        );
        if (!activeSuggestions.isEmpty()) {
            throw new BusinessException("Cannot deactivate rule while active suggestions exist");
        }

        rule.setActive(false);
        replenishmentRuleRepository.save(rule);
        eventPublisher.publishEvent(new ReplenishmentRefreshEvent(this));
    }

    @Override
    public ReplenishmentRuleResponse getRule(Long ruleId) {
        ReplenishmentRule rule = replenishmentRuleRepository.findById(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("Replenishment Rule not found with ID: " + ruleId));
        return replenishmentMapper.toResponse(rule);
    }

    @Override
    public PageResponse<ReplenishmentRuleResponse> listRules(Long companyId, Long warehouseId, Long storeId, Long productId, Boolean active, Pageable pageable) {
        Specification<ReplenishmentRule> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (companyId != null) {
                predicates.add(cb.equal(root.get("company").get("id"), companyId));
            }
            if (warehouseId != null) {
                predicates.add(cb.equal(root.get("warehouse").get("id"), warehouseId));
            }
            if (storeId != null) {
                predicates.add(cb.equal(root.get("store").get("id"), storeId));
            }
            if (productId != null) {
                predicates.add(cb.equal(root.get("product").get("id"), productId));
            }
            if (active != null) {
                predicates.add(cb.equal(root.get("active"), active));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<ReplenishmentRule> page = replenishmentRuleRepository.findAll(spec, pageable);
        List<ReplenishmentRuleResponse> content = page.getContent().stream()
                .map(replenishmentMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    @Transactional
    public List<ReplenishmentSuggestionResponse> evaluateAll(Long companyId) {
        List<ReplenishmentRule> rules = replenishmentRuleRepository.findAllByCompanyIdAndActiveTrue(companyId);
        List<ReplenishmentSuggestionResponse> results = new ArrayList<>();

        for (ReplenishmentRule rule : rules) {
            ReplenishmentSuggestionResponse suggestion = evaluateRuleInternal(rule, ReplenishmentEvaluationSource.SCHEDULED);
            if (suggestion != null) {
                results.add(suggestion);
            }
        }

        if (!results.isEmpty()) {
            eventPublisher.publishEvent(new ReplenishmentRefreshEvent(this));
        }

        return results;
    }

    @Override
    @Transactional
    public ReplenishmentSuggestionResponse evaluateRule(Long ruleId) {
        ReplenishmentRule rule = replenishmentRuleRepository.findById(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("Replenishment Rule not found with ID: " + ruleId));

        if (!rule.isActive()) {
            throw new BusinessException("Cannot evaluate an inactive replenishment rule");
        }

        ReplenishmentSuggestionResponse suggestion = evaluateRuleInternal(rule, ReplenishmentEvaluationSource.MANUAL);
        eventPublisher.publishEvent(new ReplenishmentRefreshEvent(this));

        return suggestion;
    }

    private ReplenishmentSuggestionResponse evaluateRuleInternal(ReplenishmentRule rule, ReplenishmentEvaluationSource source) {
        BigDecimal onHand = BigDecimal.ZERO;
        BigDecimal reserved = BigDecimal.ZERO;

        if (rule.getWarehouse() != null) {
            Optional<InventoryStock> stockOpt = inventoryStockRepository.findByProductIdAndWarehouseId(
                    rule.getProduct().getId(), rule.getWarehouse().getId()
            );
            if (stockOpt.isPresent()) {
                onHand = stockOpt.get().getQuantity();
                reserved = stockOpt.get().getReservedQuantity();
            }
        } else if (rule.getStore() != null) {
            Optional<InventoryStock> stockOpt = inventoryStockRepository.findByProductIdAndStoreId(
                    rule.getProduct().getId(), rule.getStore().getId()
            );
            if (stockOpt.isPresent()) {
                onHand = stockOpt.get().getQuantity();
                reserved = stockOpt.get().getReservedQuantity();
            }
        }

        BigDecimal available = onHand.subtract(reserved);

        if (available.compareTo(rule.getReorderPoint()) <= 0) {
            // Check if there is already an active suggestion (OPEN or ACKNOWLEDGED)
            List<ReplenishmentSuggestion> active = replenishmentSuggestionRepository.findByRuleIdAndStatusIn(
                    rule.getId(), Arrays.asList(ReplenishmentSuggestionStatus.OPEN, ReplenishmentSuggestionStatus.ACKNOWLEDGED)
            );
            if (!active.isEmpty()) {
                return replenishmentMapper.toResponse(active.get(0));
            }

            BigDecimal suggestedQuantity = rule.getMaxQuantity().subtract(available);
            if (suggestedQuantity.compareTo(BigDecimal.ZERO) <= 0) {
                return null;
            }

            ReplenishmentSuggestion suggestion = ReplenishmentSuggestion.builder()
                    .rule(rule)
                    .company(rule.getCompany())
                    .product(rule.getProduct())
                    .warehouse(rule.getWarehouse())
                    .store(rule.getStore())
                    .currentQuantity(onHand)
                    .reservedQuantity(reserved)
                    .availableQuantity(available)
                    .suggestedQuantity(suggestedQuantity)
                    .status(ReplenishmentSuggestionStatus.OPEN)
                    .evaluationSource(source)
                    .clientReferenceId(UUID.randomUUID())
                    .build();

            ReplenishmentSuggestion saved = replenishmentSuggestionRepository.save(suggestion);
            return replenishmentMapper.toResponse(saved);
        }

        return null;
    }

    @Override
    @Transactional
    public ReplenishmentSuggestionResponse acknowledgeSuggestion(Long suggestionId) {
        ReplenishmentSuggestion suggestion = replenishmentSuggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Replenishment Suggestion not found with ID: " + suggestionId));

        if (suggestion.getStatus() != ReplenishmentSuggestionStatus.OPEN) {
            throw new BusinessException("Only OPEN suggestions can be acknowledged. Current status: " + suggestion.getStatus());
        }

        suggestion.setStatus(ReplenishmentSuggestionStatus.ACKNOWLEDGED);
        suggestion.setAcknowledgedAt(LocalDateTime.now());

        ReplenishmentSuggestion saved = replenishmentSuggestionRepository.save(suggestion);
        eventPublisher.publishEvent(new ReplenishmentRefreshEvent(this));

        return replenishmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ReplenishmentSuggestionResponse cancelSuggestion(Long suggestionId, String notes) {
        ReplenishmentSuggestion suggestion = replenishmentSuggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Replenishment Suggestion not found with ID: " + suggestionId));

        if (suggestion.getStatus() != ReplenishmentSuggestionStatus.OPEN &&
                suggestion.getStatus() != ReplenishmentSuggestionStatus.ACKNOWLEDGED) {
            throw new BusinessException("Cannot cancel suggestion in status: " + suggestion.getStatus());
        }

        suggestion.setStatus(ReplenishmentSuggestionStatus.CANCELLED);
        suggestion.setCancelledAt(LocalDateTime.now());
        suggestion.setNotes(notes);

        ReplenishmentSuggestion saved = replenishmentSuggestionRepository.save(suggestion);
        eventPublisher.publishEvent(new ReplenishmentRefreshEvent(this));

        return replenishmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ReplenishmentSuggestionResponse createPurchaseRequestFromSuggestion(Long suggestionId) {
        ReplenishmentSuggestion suggestion = replenishmentSuggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Replenishment Suggestion not found with ID: " + suggestionId));

        if (suggestion.getStatus() != ReplenishmentSuggestionStatus.OPEN &&
                suggestion.getStatus() != ReplenishmentSuggestionStatus.ACKNOWLEDGED) {
            throw new BusinessException("Purchase request can only be created for OPEN or ACKNOWLEDGED suggestions");
        }

        // Find active supplier for the company
        List<Supplier> suppliers = supplierRepository.findByCompanyIdAndActiveTrue(
                suggestion.getCompany().getId(), PageRequest.of(0, 1)
        ).getContent();

        if (suppliers.isEmpty()) {
            throw new BusinessException("No active supplier found for company: " + suggestion.getCompany().getId());
        }
        Supplier supplier = suppliers.get(0);

        LocalDate requiredDate = LocalDate.now().plusDays(
                suggestion.getRule().getLeadTimeDays() > 0 ? suggestion.getRule().getLeadTimeDays() : 7
        );

        String uom = suggestion.getProduct().getUnit() != null ? suggestion.getProduct().getUnit().getCode() : "PCS";

        PurchaseRequestItemRequest itemReq = new PurchaseRequestItemRequest(
                suggestion.getProduct().getId(),
                suggestion.getSuggestedQuantity(),
                uom,
                "Auto-generated line item"
        );

        PurchaseRequestRequest prRequest = new PurchaseRequestRequest(
                suggestion.getCompany().getId(),
                supplier.getId(),
                suggestion.getWarehouse() != null ? suggestion.getWarehouse().getId() : null,
                suggestion.getStore() != null ? suggestion.getStore().getId() : null,
                requiredDate,
                "Auto-generated from Replenishment Suggestion ID: " + suggestion.getId(),
                Collections.singletonList(itemReq)
        );

        PurchaseRequestResponse prResponse = purchaseRequestService.createPurchaseRequest(prRequest);

        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(prResponse.id())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to load created Purchase Request"));

        suggestion.setPurchaseRequest(purchaseRequest);
        suggestion.setStatus(ReplenishmentSuggestionStatus.ORDERED);
        suggestion.setOrderedAt(LocalDateTime.now());

        ReplenishmentSuggestion saved = replenishmentSuggestionRepository.save(suggestion);
        eventPublisher.publishEvent(new ReplenishmentRefreshEvent(this));

        return replenishmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ReplenishmentSuggestionResponse createTransferFromSuggestion(Long suggestionId) {
        ReplenishmentSuggestion suggestion = replenishmentSuggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Replenishment Suggestion not found with ID: " + suggestionId));

        if (suggestion.getStatus() != ReplenishmentSuggestionStatus.OPEN &&
                suggestion.getStatus() != ReplenishmentSuggestionStatus.ACKNOWLEDGED) {
            throw new BusinessException("Transfer can only be created for OPEN or ACKNOWLEDGED suggestions");
        }

        if (suggestion.getStore() == null) {
            throw new BusinessException("Inventory transfers can only replenish Store locations. This suggestion is for a Warehouse.");
        }

        // Find source warehouse for the transfer within the company
        List<Warehouse> warehouses = warehouseRepository.findAll().stream()
                .filter(w -> w.getRegion().getCompany().getId().equals(suggestion.getCompany().getId()) && Boolean.TRUE.equals(w.getActive()))
                .toList();

        if (warehouses.isEmpty()) {
            throw new BusinessException("No active warehouse found in the company to act as the source of the transfer");
        }
        Warehouse sourceWarehouse = warehouses.get(0);

        InventoryTransferItemRequest itemReq = new InventoryTransferItemRequest(
                suggestion.getProduct().getId(),
                suggestion.getSuggestedQuantity()
        );

        InventoryTransferRequest xferRequest = new InventoryTransferRequest(
                suggestion.getCompany().getId(),
                sourceWarehouse.getId(),
                null,
                null,
                suggestion.getStore().getId(),
                UUID.randomUUID(),
                "Auto-generated replenishment from Suggestion ID: " + suggestion.getId(),
                Collections.singletonList(itemReq)
        );

        var xferResponse = inventoryTransferService.createTransfer(xferRequest);

        InventoryTransfer transfer = inventoryTransferRepository.findById(xferResponse.data().id())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to load created Inventory Transfer"));

        suggestion.setTransfer(transfer);
        suggestion.setStatus(ReplenishmentSuggestionStatus.ORDERED);
        suggestion.setOrderedAt(LocalDateTime.now());

        ReplenishmentSuggestion saved = replenishmentSuggestionRepository.save(suggestion);
        eventPublisher.publishEvent(new ReplenishmentRefreshEvent(this));

        return replenishmentMapper.toResponse(saved);
    }

    @Override
    public PageResponse<ReplenishmentSuggestionResponse> listSuggestions(Long companyId, ReplenishmentSuggestionStatus status, Long productId, Pageable pageable) {
        Specification<ReplenishmentSuggestion> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (companyId != null) {
                predicates.add(cb.equal(root.get("company").get("id"), companyId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (productId != null) {
                predicates.add(cb.equal(root.get("product").get("id"), productId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<ReplenishmentSuggestion> page = replenishmentSuggestionRepository.findAll(spec, pageable);
        List<ReplenishmentSuggestionResponse> content = page.getContent().stream()
                .map(replenishmentMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public ReplenishmentSuggestionResponse getSuggestion(Long suggestionId) {
        ReplenishmentSuggestion suggestion = replenishmentSuggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Replenishment Suggestion not found with ID: " + suggestionId));
        return replenishmentMapper.toResponse(suggestion);
    }

    private void validateQuantities(BigDecimal min, BigDecimal max, BigDecimal point, BigDecimal qty) {
        if (min == null || min.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Min quantity must be at least 0");
        }
        if (point == null || point.compareTo(min) < 0) {
            throw new BusinessException("Reorder point must be at least min quantity");
        }
        if (max == null || max.compareTo(point) <= 0) {
            throw new BusinessException("Max quantity must be greater than reorder point");
        }
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Reorder quantity must be greater than 0");
        }
        if (qty.compareTo(max) > 0) {
            throw new BusinessException("Reorder quantity cannot exceed max quantity");
        }
    }

    private void validateReplayPayload(ReplenishmentRule rule, ReplenishmentRuleRequest request) {
        if (!rule.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Idempotent replay mismatch: company mismatch");
        }
        if (!rule.getProduct().getId().equals(request.productId())) {
            throw new BusinessException("Idempotent replay mismatch: product mismatch");
        }
        if (request.warehouseId() != null && (rule.getWarehouse() == null || !rule.getWarehouse().getId().equals(request.warehouseId()))) {
            throw new BusinessException("Idempotent replay mismatch: warehouse mismatch");
        }
        if (request.storeId() != null && (rule.getStore() == null || !rule.getStore().getId().equals(request.storeId()))) {
            throw new BusinessException("Idempotent replay mismatch: store mismatch");
        }
    }
}
