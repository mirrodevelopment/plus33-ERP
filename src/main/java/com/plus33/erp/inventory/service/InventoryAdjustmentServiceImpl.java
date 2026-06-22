package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import com.plus33.erp.inventory.event.InventoryAdjustmentRefreshEvent;
import com.plus33.erp.inventory.mapper.InventoryAdjustmentMapper;
import com.plus33.erp.inventory.repository.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class InventoryAdjustmentServiceImpl implements InventoryAdjustmentService {

    private final InventoryAdjustmentRepository inventoryAdjustmentRepository;
    private final InventoryStockRepository inventoryStockRepository;
    private final StockMovementRepository stockMovementRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseRepository warehouseRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryAdjustmentMapper inventoryAdjustmentMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final InventoryTraceabilityService inventoryTraceabilityService;

    public InventoryAdjustmentServiceImpl(
            InventoryAdjustmentRepository inventoryAdjustmentRepository,
            InventoryStockRepository inventoryStockRepository,
            StockMovementRepository stockMovementRepository,
            CompanyRepository companyRepository,
            WarehouseRepository warehouseRepository,
            StoreRepository storeRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            InventoryAdjustmentMapper inventoryAdjustmentMapper,
            ApplicationEventPublisher eventPublisher,
            InventoryTraceabilityService inventoryTraceabilityService) {
        this.inventoryAdjustmentRepository = inventoryAdjustmentRepository;
        this.inventoryStockRepository = inventoryStockRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.companyRepository = companyRepository;
        this.warehouseRepository = warehouseRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.inventoryAdjustmentMapper = inventoryAdjustmentMapper;
        this.eventPublisher = eventPublisher;
        this.inventoryTraceabilityService = inventoryTraceabilityService;
    }

    @Override
    @Transactional
    public IdempotentCreateResult<InventoryAdjustmentResponse> createAdjustment(InventoryAdjustmentRequest request) {
        // Idempotency check
        Optional<InventoryAdjustment> existing = inventoryAdjustmentRepository.findByClientReferenceId(request.clientReferenceId());
        if (existing.isPresent()) {
            InventoryAdjustment adjustment = existing.get();
            validateReplayPayload(adjustment, request);
            return new IdempotentCreateResult<>(inventoryAdjustmentMapper.toResponse(adjustment), false);
        }

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        if (!company.getActive()) {
            throw new BusinessException("Company is inactive");
        }

        InventoryAdjustment adjustment = new InventoryAdjustment();
        adjustment.setCompany(company);
        adjustment.setClientReferenceId(request.clientReferenceId());
        adjustment.setRemarks(request.remarks());
        adjustment.setAdjustmentType(request.adjustmentType());
        adjustment.setStatus(InventoryAdjustmentStatus.DRAFT);
        adjustment.setCreatedBy(getCurrentUser());

        setLocationsAndValidate(adjustment, request.warehouseId(), request.storeId());

        List<InventoryAdjustmentItem> items = new ArrayList<>();
        Set<Long> productIds = new HashSet<>();

        for (InventoryAdjustmentItemRequest itemReq : request.items()) {
            if (itemReq.quantity() == null || itemReq.quantity().compareTo(BigDecimal.ZERO) == 0) {
                throw new BusinessException("Quantity change cannot be zero");
            }
            if (!productIds.add(itemReq.productId())) {
                throw new BusinessException("Duplicate product ID in items list: " + itemReq.productId());
            }

            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemReq.productId()));
            if (!product.getActive()) {
                throw new BusinessException("Product " + product.getName() + " is inactive");
            }

            InventoryAdjustmentItem item = new InventoryAdjustmentItem();
            item.setInventoryAdjustment(adjustment);
            item.setProduct(product);
            item.setQuantity(itemReq.quantity());
            items.add(item);
        }

        adjustment.setItems(items);
        adjustment.setAdjustmentNumber(generateAdjustmentNumber());

        InventoryAdjustment saved = inventoryAdjustmentRepository.save(adjustment);
        return new IdempotentCreateResult<>(inventoryAdjustmentMapper.toResponse(saved), true);
    }

    @Override
    @Transactional
    public InventoryAdjustmentResponse updateAdjustment(Long id, InventoryAdjustmentUpdateRequest request) {
        InventoryAdjustment adjustment = inventoryAdjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Adjustment not found with ID: " + id));

        if (adjustment.getStatus() != InventoryAdjustmentStatus.DRAFT) {
            throw new BusinessException("Only DRAFT adjustments can be updated. Current status: " + adjustment.getStatus());
        }

        adjustment.setRemarks(request.remarks());
        adjustment.setAdjustmentType(request.adjustmentType());
        setLocationsAndValidate(adjustment, request.warehouseId(), request.storeId());

        // Recreate items
        adjustment.getItems().clear();
        Set<Long> productIds = new HashSet<>();

        for (InventoryAdjustmentItemRequest itemReq : request.items()) {
            if (itemReq.quantity() == null || itemReq.quantity().compareTo(BigDecimal.ZERO) == 0) {
                throw new BusinessException("Quantity change cannot be zero");
            }
            if (!productIds.add(itemReq.productId())) {
                throw new BusinessException("Duplicate product ID in items list: " + itemReq.productId());
            }

            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemReq.productId()));
            if (!product.getActive()) {
                throw new BusinessException("Product " + product.getName() + " is inactive");
            }

            InventoryAdjustmentItem item = new InventoryAdjustmentItem();
            item.setInventoryAdjustment(adjustment);
            item.setProduct(product);
            item.setQuantity(itemReq.quantity());
            adjustment.getItems().add(item);
        }

        InventoryAdjustment saved = inventoryAdjustmentRepository.save(adjustment);
        return inventoryAdjustmentMapper.toResponse(saved);
    }

    @Override
    public InventoryAdjustmentResponse getAdjustmentById(Long id) {
        InventoryAdjustment adjustment = inventoryAdjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Adjustment not found with ID: " + id));
        return inventoryAdjustmentMapper.toResponse(adjustment);
    }

    @Override
    public PageResponse<InventoryAdjustmentResponse> searchAdjustments(InventoryAdjustmentSearchRequest searchRequest, Pageable pageable) {
        Specification<InventoryAdjustment> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.status() != null) {
                predicates.add(cb.equal(root.get("status"), searchRequest.status()));
            }
            if (searchRequest.companyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.companyId()));
            }
            if (searchRequest.warehouseId() != null) {
                predicates.add(cb.equal(root.get("warehouse").get("id"), searchRequest.warehouseId()));
            }
            if (searchRequest.storeId() != null) {
                predicates.add(cb.equal(root.get("store").get("id"), searchRequest.storeId()));
            }
            if (searchRequest.adjustmentType() != null) {
                predicates.add(cb.equal(root.get("adjustmentType"), searchRequest.adjustmentType()));
            }
            if (searchRequest.adjustmentNumber() != null && !searchRequest.adjustmentNumber().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("adjustmentNumber")), "%" + searchRequest.adjustmentNumber().toLowerCase() + "%"));
            }
            if (searchRequest.clientReferenceId() != null) {
                predicates.add(cb.equal(root.get("clientReferenceId"), searchRequest.clientReferenceId()));
            }
            if (searchRequest.createdAtFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), searchRequest.createdAtFrom().atStartOfDay()));
            }
            if (searchRequest.createdAtTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), searchRequest.createdAtTo().plusDays(1).atStartOfDay()));
            }
            if (searchRequest.createdBy() != null) {
                predicates.add(cb.equal(root.get("createdBy").get("id"), searchRequest.createdBy()));
            }
            if (searchRequest.productId() != null) {
                predicates.add(cb.equal(root.join("items").get("product").get("id"), searchRequest.productId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<InventoryAdjustment> page = inventoryAdjustmentRepository.findAll(spec, pageable);
        List<InventoryAdjustmentResponse> content = page.getContent().stream()
                .map(inventoryAdjustmentMapper::toResponse)
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
    public InventoryAdjustmentResponse submitAdjustment(Long id) {
        InventoryAdjustment adjustment = inventoryAdjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Adjustment not found with ID: " + id));

        if (adjustment.getStatus() != InventoryAdjustmentStatus.DRAFT) {
            throw new BusinessException("Only DRAFT adjustments can be submitted. Current status: " + adjustment.getStatus());
        }

        if (adjustment.getItems().isEmpty()) {
            throw new BusinessException("Adjustment must contain at least one line item");
        }

        User currentUser = getCurrentUser();
        adjustment.setSubmittedBy(currentUser);
        adjustment.setSubmittedAt(LocalDateTime.now());

        // Check if any quantity is negative (reduction)
        boolean hasNegative = false;
        for (InventoryAdjustmentItem item : adjustment.getItems()) {
            if (item.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                hasNegative = true;
                break;
            }
        }

        if (hasNegative) {
            // Requires manual approval
            adjustment.setStatus(InventoryAdjustmentStatus.SUBMITTED);
        } else {
            // Auto-approved for pure stock additions
            adjustment.setStatus(InventoryAdjustmentStatus.APPROVED);
            adjustment.setApprovedBy(currentUser);
            adjustment.setApprovedAt(LocalDateTime.now());
        }

        InventoryAdjustment saved = inventoryAdjustmentRepository.save(adjustment);
        return inventoryAdjustmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InventoryAdjustmentResponse approveAdjustment(Long id) {
        InventoryAdjustment adjustment = inventoryAdjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Adjustment not found with ID: " + id));

        if (adjustment.getStatus() != InventoryAdjustmentStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED adjustments can be approved. Current status: " + adjustment.getStatus());
        }

        User currentUser = getCurrentUser();
        adjustment.setStatus(InventoryAdjustmentStatus.APPROVED);
        adjustment.setApprovedBy(currentUser);
        adjustment.setApprovedAt(LocalDateTime.now());

        InventoryAdjustment saved = inventoryAdjustmentRepository.save(adjustment);
        return inventoryAdjustmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InventoryAdjustmentResponse postAdjustment(Long id) {
        InventoryAdjustment adjustment = inventoryAdjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Adjustment not found with ID: " + id));

        if (adjustment.getStatus() != InventoryAdjustmentStatus.APPROVED) {
            throw new BusinessException("Only APPROVED adjustments can be posted. Current status: " + adjustment.getStatus());
        }

        User currentUser = getCurrentUser();

        for (InventoryAdjustmentItem item : adjustment.getItems()) {
            InventoryStock stock = getOrLockStock(item.getProduct().getId(),
                    adjustment.getWarehouse() != null ? adjustment.getWarehouse().getId() : null,
                    adjustment.getStore() != null ? adjustment.getStore().getId() : null,
                    true);

            BigDecimal newQuantity = stock.getQuantity().add(item.getQuantity());
            if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Insufficient stock for product " + item.getProduct().getName()
                        + ". Current stock: " + stock.getQuantity() + ", Adjustment: " + item.getQuantity());
            }

            stock.setQuantity(newQuantity);
            inventoryStockRepository.save(stock);

            createStockMovement(item.getProduct(), adjustment.getWarehouse(), adjustment.getStore(),
                    item.getQuantity(), adjustment, currentUser);
        }

        adjustment.setStatus(InventoryAdjustmentStatus.POSTED);
        adjustment.setPostedBy(currentUser);
        adjustment.setPostedAt(LocalDateTime.now());

        InventoryAdjustment saved = inventoryAdjustmentRepository.save(adjustment);

        // Record trace events
        for (InventoryAdjustmentItem item : saved.getItems()) {
            inventoryTraceabilityService.recordTraceEvent(
                    saved.getCompany().getId(),
                    item.getProduct().getId(),
                    null, // lotId
                    null, // serialId
                    saved.getWarehouse() != null ? saved.getWarehouse().getId() : null,
                    saved.getStore() != null ? saved.getStore().getId() : null,
                    com.plus33.erp.inventory.entity.InventoryTraceEventType.ADJUSTMENT,
                    item.getQuantity(),
                    com.plus33.erp.inventory.entity.InventoryTraceReferenceType.INVENTORY_ADJUSTMENT,
                    saved.getId(),
                    saved.getAdjustmentNumber(),
                    "Adjusted via Adjustment #" + saved.getAdjustmentNumber()
            );
        }

        eventPublisher.publishEvent(new InventoryAdjustmentRefreshEvent(this));

        return inventoryAdjustmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InventoryAdjustmentResponse cancelAdjustment(Long id, String reason) {
        InventoryAdjustment adjustment = inventoryAdjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Adjustment not found with ID: " + id));

        if (adjustment.getStatus() == InventoryAdjustmentStatus.POSTED ||
            adjustment.getStatus() == InventoryAdjustmentStatus.CANCELLED) {
            throw new BusinessException("Cannot cancel adjustment after posting or cancellation. Current status: " + adjustment.getStatus());
        }

        if (reason == null || reason.isBlank()) {
            throw new BusinessException("Cancellation reason is required");
        }

        User currentUser = getCurrentUser();
        adjustment.setStatus(InventoryAdjustmentStatus.CANCELLED);
        adjustment.setCancelledBy(currentUser);
        adjustment.setCancelledAt(LocalDateTime.now());
        adjustment.setCancellationReason(reason);

        InventoryAdjustment saved = inventoryAdjustmentRepository.save(adjustment);
        return inventoryAdjustmentMapper.toResponse(saved);
    }

    private void validateReplayPayload(InventoryAdjustment adjustment, InventoryAdjustmentRequest request) {
        if (!adjustment.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Idempotent replay mismatch: company ID does not match");
        }
        if (request.warehouseId() != null && (adjustment.getWarehouse() == null || !adjustment.getWarehouse().getId().equals(request.warehouseId()))) {
            throw new BusinessException("Idempotent replay mismatch: warehouse mismatch");
        }
        if (request.storeId() != null && (adjustment.getStore() == null || !adjustment.getStore().getId().equals(request.storeId()))) {
            throw new BusinessException("Idempotent replay mismatch: store mismatch");
        }
        if (adjustment.getAdjustmentType() != request.adjustmentType()) {
            throw new BusinessException("Idempotent replay mismatch: adjustment type mismatch");
        }
    }

    private InventoryStock getOrLockStock(Long productId, Long warehouseId, Long storeId, boolean lock) {
        if (warehouseId != null) {
            return lock
                    ? inventoryStockRepository.findWithPessimisticWriteByProductIdAndWarehouseId(productId, warehouseId)
                    .orElseGet(() -> createDefaultStock(productId, warehouseId, null))
                    : inventoryStockRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                    .orElseGet(() -> createDefaultStock(productId, warehouseId, null));
        } else if (storeId != null) {
            return lock
                    ? inventoryStockRepository.findWithPessimisticWriteByProductIdAndStoreId(productId, storeId)
                    .orElseGet(() -> createDefaultStock(productId, null, storeId))
                    : inventoryStockRepository.findByProductIdAndStoreId(productId, storeId)
                    .orElseGet(() -> createDefaultStock(productId, null, storeId));
        }
        throw new BusinessException("Valid location ID must be specified");
    }

    private InventoryStock createDefaultStock(Long productId, Long warehouseId, Long storeId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        InventoryStock stock = new InventoryStock();
        stock.setProduct(product);
        if (warehouseId != null) {
            stock.setWarehouse(warehouseRepository.getReferenceById(warehouseId));
        }
        if (storeId != null) {
            stock.setStore(storeRepository.getReferenceById(storeId));
        }
        stock.setQuantity(BigDecimal.ZERO);
        stock.setReservedQuantity(BigDecimal.ZERO);
        return inventoryStockRepository.save(stock);
    }

    private void createStockMovement(Product product, Warehouse warehouse, Store store, BigDecimal quantity,
                                     InventoryAdjustment adjustment, User user) {
        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setWarehouse(warehouse);
        movement.setStore(store);
        movement.setMovementType(StockMovementReferenceType.INVENTORY_ADJUSTMENT.name());
        movement.setQuantity(quantity);
        movement.setReferenceNo(adjustment.getAdjustmentNumber());
        movement.setReferenceType(StockMovementReferenceType.INVENTORY_ADJUSTMENT);
        movement.setReferenceId(adjustment.getId());
        movement.setReferenceNumber(adjustment.getAdjustmentNumber());
        movement.setCreatedBy(user);
        stockMovementRepository.save(movement);
    }

    private void setLocationsAndValidate(InventoryAdjustment adjustment, Long warehouseId, Long storeId) {
        // XOR checks
        if ((warehouseId != null && storeId != null) || (warehouseId == null && storeId == null)) {
            throw new BusinessException("Adjustment must reference exactly one location (Warehouse or Store)");
        }

        Warehouse warehouse = warehouseId != null ? warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + warehouseId)) : null;
        Store store = storeId != null ? storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + storeId)) : null;

        // Company match validation
        Long locCompanyId = warehouse != null ? warehouse.getRegion().getCompany().getId() 
                : (store != null ? store.getRegion().getCompany().getId() : null);

        if (locCompanyId == null || !locCompanyId.equals(adjustment.getCompany().getId())) {
            throw new BusinessException("Referenced location must belong to the adjustment company");
        }

        adjustment.setWarehouse(warehouse);
        adjustment.setStore(store);
    }

    private synchronized String generateAdjustmentNumber() {
        Long seqVal = inventoryAdjustmentRepository.getNextSequenceValue();
        return String.format("ADJ-%d-%06d", LocalDate.now().getYear(), seqVal);
    }

    private User getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return userRepository.findByEmail("admin@plus33.com")
                    .orElseThrow(() -> new ResourceNotFoundException("Default admin user not found"));
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found with email: " + email));
    }
}
