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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository,
                                    PurchaseRequestRepository purchaseRequestRepository,
                                    CompanyRepository companyRepository,
                                    SupplierRepository supplierRepository,
                                    ProductRepository productRepository,
                                    UserRepository userRepository,
                                    PurchaseOrderMapper purchaseOrderMapper) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.companyRepository = companyRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.purchaseOrderMapper = purchaseOrderMapper;
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
        return purchaseOrderMapper.toResponse(saved);
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
            if (pr.getStatus() != PurchaseRequestStatus.APPROVED) {
                throw new BusinessException("Purchase Request must be in APPROVED status");
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
