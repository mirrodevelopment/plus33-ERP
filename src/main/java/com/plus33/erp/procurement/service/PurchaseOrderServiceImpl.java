package com.plus33.erp.procurement.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.*;
import com.plus33.erp.procurement.mapper.PurchaseOrderMapper;
import com.plus33.erp.procurement.repository.PurchaseOrderRepository;
import com.plus33.erp.procurement.repository.PurchaseRequestRepository;
import com.plus33.erp.procurement.repository.SupplierRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.plus33.erp.analytics.event.ProcurementRefreshEvent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.plus33.erp.finance.budget.repository.BudgetDimensionSetRepository;
import com.plus33.erp.finance.budget.service.BudgetService;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.entity.Account;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final CompanyRepository companyRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final BudgetDimensionSetRepository budgetDimensionSetRepository;
    private final BudgetService budgetService;
    private final AccountRepository accountRepository;

    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository,
                                    PurchaseRequestRepository purchaseRequestRepository,
                                    CompanyRepository companyRepository,
                                    SupplierRepository supplierRepository,
                                    ProductRepository productRepository,
                                    UserRepository userRepository,
                                    PurchaseOrderMapper purchaseOrderMapper,
                                    ApplicationEventPublisher eventPublisher,
                                    BudgetDimensionSetRepository budgetDimensionSetRepository,
                                    BudgetService budgetService,
                                    AccountRepository accountRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.companyRepository = companyRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.eventPublisher = eventPublisher;
        this.budgetDimensionSetRepository = budgetDimensionSetRepository;
        this.budgetService = budgetService;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest request) {
        validateOrderDetails(request, null);

        User currentUser = getCurrentUser();
        PurchaseOrder po = purchaseOrderMapper.toEntity(request);

        po.setCompany(companyRepository.getReferenceById(request.companyId()));
        po.setSupplier(supplierRepository.getReferenceById(request.supplierId()));
        po.setOrderedBy(currentUser);
        po.setStatus(PurchaseOrderStatus.DRAFT);

        if (request.purchaseRequestId() != null) {
            po.setPurchaseRequest(purchaseRequestRepository.getReferenceById(request.purchaseRequestId()));
        }

        // Set financial fields defaults
        po.setDiscountAmount(request.discountAmount() != null ? request.discountAmount() : BigDecimal.ZERO);
        po.setTaxAmount(request.taxAmount() != null ? request.taxAmount() : BigDecimal.ZERO);
        po.setCurrencyCode(request.currencyCode() != null && !request.currencyCode().isBlank() ? request.currencyCode() : "AED");

        // Map and save items
        BigDecimal subtotal = BigDecimal.ZERO;
        for (PurchaseOrderItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemReq.productId()));
            if (!product.getActive()) {
                throw new BusinessException("Cannot assign product " + product.getName() + ": Product is inactive");
            }
            PurchaseOrderItem item = purchaseOrderMapper.toItemEntity(itemReq);
            item.setProduct(product);
            item.setPurchaseOrder(po);
            item.setReceivedQuantity(BigDecimal.ZERO);
            item.setRemainingQuantity(itemReq.orderedQuantity());
            if (itemReq.dimensionSetId() != null) {
                item.setDimensionSet(budgetDimensionSetRepository.findById(itemReq.dimensionSetId()).orElse(null));
            }
            po.getItems().add(item);

            BigDecimal lineTotal = itemReq.orderedQuantity().multiply(itemReq.unitPrice());
            subtotal = subtotal.add(lineTotal);
        }

        po.setSubtotalAmount(subtotal);
        po.setTotalAmount(subtotal.add(po.getTaxAmount()).subtract(po.getDiscountAmount()));

        po.setOrderNumber(generateOrderNumber());

        PurchaseOrder saved = purchaseOrderRepository.save(po);
        return purchaseOrderMapper.toResponse(saved);
    }

    @Override
    public PurchaseOrderResponse getPurchaseOrderById(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + id));
        return purchaseOrderMapper.toResponse(po);
    }

    @Override
    public PageResponse<PurchaseOrderResponse> searchPurchaseOrders(PurchaseOrderSearchRequest searchRequest, Pageable pageable) {
        Specification<PurchaseOrder> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.orderNumber() != null && !searchRequest.orderNumber().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("orderNumber")), "%" + searchRequest.orderNumber().toLowerCase() + "%"));
            }
            if (searchRequest.companyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.companyId()));
            }
            if (searchRequest.supplierId() != null) {
                predicates.add(cb.equal(root.get("supplier").get("id"), searchRequest.supplierId()));
            }
            if (searchRequest.status() != null) {
                predicates.add(cb.equal(root.get("status"), searchRequest.status()));
            }
            if (searchRequest.expectedDeliveryDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("expectedDeliveryDate"), searchRequest.expectedDeliveryDateFrom()));
            }
            if (searchRequest.expectedDeliveryDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("expectedDeliveryDate"), searchRequest.expectedDeliveryDateTo()));
            }
            if (searchRequest.purchaseRequestId() != null) {
                predicates.add(cb.equal(root.get("purchaseRequest").get("id"), searchRequest.purchaseRequestId()));
            }
            if (searchRequest.orderedBy() != null) {
                predicates.add(cb.equal(root.get("orderedBy").get("id"), searchRequest.orderedBy()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<PurchaseOrder> page = purchaseOrderRepository.findAll(spec, pageable);
        List<PurchaseOrderResponse> content = page.getContent().stream()
                .map(purchaseOrderMapper::toResponse)
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
    public PurchaseOrderResponse updatePurchaseOrder(Long id, PurchaseOrderRequest request) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + id));

        if (po.getStatus() != PurchaseOrderStatus.DRAFT) {
            throw new BusinessException("Terminal or non-draft states cannot be modified: Purchase Order is in " + po.getStatus() + " status");
        }

        validateOrderDetails(request, id);

        po.setCompany(companyRepository.getReferenceById(request.companyId()));
        po.setSupplier(supplierRepository.getReferenceById(request.supplierId()));
        po.setExpectedDeliveryDate(request.expectedDeliveryDate());
        po.setNotes(request.notes());

        if (request.purchaseRequestId() != null) {
            po.setPurchaseRequest(purchaseRequestRepository.getReferenceById(request.purchaseRequestId()));
        } else {
            po.setPurchaseRequest(null);
        }

        po.setDiscountAmount(request.discountAmount() != null ? request.discountAmount() : BigDecimal.ZERO);
        po.setTaxAmount(request.taxAmount() != null ? request.taxAmount() : BigDecimal.ZERO);
        po.setCurrencyCode(request.currencyCode() != null && !request.currencyCode().isBlank() ? request.currencyCode() : "AED");

        // Recreate items
        po.getItems().clear();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (PurchaseOrderItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemReq.productId()));
            if (!product.getActive()) {
                throw new BusinessException("Cannot assign product " + product.getName() + ": Product is inactive");
            }
            PurchaseOrderItem item = purchaseOrderMapper.toItemEntity(itemReq);
            item.setProduct(product);
            item.setPurchaseOrder(po);
            item.setReceivedQuantity(BigDecimal.ZERO);
            item.setRemainingQuantity(itemReq.orderedQuantity());
            if (itemReq.dimensionSetId() != null) {
                item.setDimensionSet(budgetDimensionSetRepository.findById(itemReq.dimensionSetId()).orElse(null));
            }
            po.getItems().add(item);

            BigDecimal lineTotal = itemReq.orderedQuantity().multiply(itemReq.unitPrice());
            subtotal = subtotal.add(lineTotal);
        }

        po.setSubtotalAmount(subtotal);
        po.setTotalAmount(subtotal.add(po.getTaxAmount()).subtract(po.getDiscountAmount()));

        PurchaseOrder saved = purchaseOrderRepository.save(po);
        return purchaseOrderMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deletePurchaseOrder(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + id));

        if (po.getStatus() != PurchaseOrderStatus.DRAFT) {
            throw new BusinessException("Only DRAFT purchase orders can be deleted");
        }

        purchaseOrderRepository.delete(po);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse approvePurchaseOrder(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + id));

        if (po.getStatus() != PurchaseOrderStatus.DRAFT) {
            throw new BusinessException("Only DRAFT purchase orders can be approved");
        }

        po.setStatus(PurchaseOrderStatus.ISSUED);
        po.setIssuedBy(getCurrentUser());
        po.setIssuedAt(LocalDateTime.now());

        PurchaseOrder saved = purchaseOrderRepository.save(po);

        // RELEASE PR RESERVATIONS IF LINKED
        if (saved.getPurchaseRequest() != null) {
            for (PurchaseRequestItem prItem : saved.getPurchaseRequest().getItems()) {
                try {
                    budgetService.releaseReservation("PROCUREMENT_PR", prItem.getId());
                } catch (Exception e) {
                    log.warn("Failed to release PR reservation for item: {}", prItem.getId(), e);
                }
            }
        }

        // CREATE PO BUDGET RESERVATIONS
        for (PurchaseOrderItem item : saved.getItems()) {
            if (item.getDimensionSet() != null) {
                try {
                    String accountCode = isInventoryProduct(item.getProduct()) ? "1300" : "5200";
                    Account account = accountRepository.findByCompanyIdAndAccountCode(saved.getCompany().getId(), accountCode)
                        .orElseThrow(() -> new BusinessException("Account not found: " + accountCode));

                    BigDecimal amount = item.getOrderedQuantity().multiply(item.getUnitPrice());

                    com.plus33.erp.finance.budget.dto.BudgetReservationRequest resReq = new com.plus33.erp.finance.budget.dto.BudgetReservationRequest(
                        account.getId(),
                        new com.plus33.erp.finance.budget.dto.BudgetDimensionSetRequest(
                            item.getDimensionSet().getDepartment() != null ? item.getDimensionSet().getDepartment().getId() : null,
                            item.getDimensionSet().getCostCenter() != null ? item.getDimensionSet().getCostCenter().getId() : null,
                            item.getDimensionSet().getProject() != null ? item.getDimensionSet().getProject().getId() : null,
                            item.getDimensionSet().getWarehouse() != null ? item.getDimensionSet().getWarehouse().getId() : null,
                            item.getDimensionSet().getAssetCategory() != null ? item.getDimensionSet().getAssetCategory().getId() : null,
                            item.getDimensionSet().getRegion() != null ? item.getDimensionSet().getRegion().getId() : null,
                            item.getDimensionSet().getStore() != null ? item.getDimensionSet().getStore().getId() : null
                        ),
                        LocalDate.now(),
                        amount,
                        "PROCUREMENT_PO",
                        item.getId(),
                        saved.getOrderNumber(),
                        LocalDate.now().plusDays(30)
                    );
                    budgetService.createReservation(saved.getCompany().getId(), resReq);
                } catch (Exception e) {
                    log.error("Failed to create budget reservation for PO item ID: {}", item.getId(), e);
                    throw e;
                }
            }
        }

        eventPublisher.publishEvent(new ProcurementRefreshEvent(this));
        return purchaseOrderMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse cancelPurchaseOrder(Long id, String reason) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + id));

        if (po.getStatus() != PurchaseOrderStatus.ISSUED) {
            throw new BusinessException("Only ISSUED purchase orders can be cancelled");
        }

        if (reason == null || reason.isBlank()) {
            throw new BusinessException("Cancellation reason is required");
        }

        po.setStatus(PurchaseOrderStatus.CANCELLED);
        po.setCancelledBy(getCurrentUser());
        po.setCancelledAt(LocalDateTime.now());
        po.setCancellationReason(reason);

        PurchaseOrder saved = purchaseOrderRepository.save(po);

        // RELEASE PO RESERVATIONS
        for (PurchaseOrderItem item : saved.getItems()) {
            try {
                budgetService.releaseReservation("PROCUREMENT_PO", item.getId());
            } catch (Exception e) {
                log.warn("Failed to release PO reservation for item: {}", item.getId(), e);
            }
        }

        eventPublisher.publishEvent(new ProcurementRefreshEvent(this));
        return purchaseOrderMapper.toResponse(saved);
    }

    private boolean isInventoryProduct(Product product) {
        if (product.getProductType() == null) {
            return true;
        }
        String type = product.getProductType().toUpperCase();
        return !type.equals("SERVICE") && !type.equals("EXPENSE");
    }

    @Override
    @Transactional
    public PurchaseOrderResponse closePurchaseOrder(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + id));

        if (po.getStatus() != PurchaseOrderStatus.RECEIVED) {
            throw new BusinessException("Only RECEIVED purchase orders can be closed");
        }

        po.setStatus(PurchaseOrderStatus.CLOSED);
        po.setClosedAt(LocalDateTime.now());

        PurchaseOrder saved = purchaseOrderRepository.save(po);
        eventPublisher.publishEvent(new ProcurementRefreshEvent(this));
        return purchaseOrderMapper.toResponse(saved);
    }

    private void validateOrderDetails(PurchaseOrderRequest request, Long existingId) {
        if (request.expectedDeliveryDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Expected delivery date cannot be in the past");
        }

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        if (!company.getActive()) {
            throw new BusinessException("Company is inactive");
        }

        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + request.supplierId()));
        if (!supplier.getActive()) {
            throw new BusinessException("Supplier is inactive");
        }
        if (!supplier.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Supplier must belong to the same company");
        }

        if (request.purchaseRequestId() != null) {
            PurchaseRequest pr = purchaseRequestRepository.findById(request.purchaseRequestId())
                    .orElseThrow(() -> new ResourceNotFoundException("Purchase Request not found with ID: " + request.purchaseRequestId()));
            if (pr.getStatus() != PurchaseRequestStatus.APPROVED && pr.getStatus() != PurchaseRequestStatus.CONVERTED_TO_PO) {
                throw new BusinessException("Purchase Request must be in APPROVED or CONVERTED_TO_PO status");
            }
            if (!pr.getCompany().getId().equals(request.companyId())) {
                throw new BusinessException("Purchase Request company must match Purchase Order company");
            }

            boolean alreadyLinked = existingId == null
                    ? purchaseOrderRepository.existsByPurchaseRequestId(request.purchaseRequestId())
                    : purchaseOrderRepository.existsByPurchaseRequestIdAndIdNot(request.purchaseRequestId(), existingId);

            if (alreadyLinked) {
                throw new BusinessException("Purchase Request is already linked to another Purchase Order");
            }
        }

        if (request.items() == null || request.items().isEmpty()) {
            throw new BusinessException("Purchase order must contain at least one line item");
        }
    }

    private synchronized String generateOrderNumber() {
        Long seqVal = purchaseOrderRepository.getNextSequenceValue();
        return String.format("PO-%d-%06d", LocalDate.now().getYear(), seqVal);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found with email: " + email));
    }
}
