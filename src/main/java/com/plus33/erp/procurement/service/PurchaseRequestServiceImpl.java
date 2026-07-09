/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : PurchaseRequestServiceImpl.java
 * Purpose           : Business logic service layer for Procurement Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseRequestController
 * Related Service   : PurchaseRequestServiceImpl
 * Related Repository: PurchaseRequestRepository, PurchaseOrderRepository, PurchaseOrderItemRepository, CompanyRepository, SupplierRepository, WarehouseRepository, StoreRepository, ProductRepository, UserRepository, BudgetDimensionSetRepository, AccountRepository
 * Related Entity    : PurchaseRequest
 * Related DTO       : approvePurchaseRequest, BudgetDimensionSetRequest, BudgetReservationRequest, cancelPurchaseRequest, createPurchaseRequest
 * Related Mapper    : PurchaseRequestMapper
 * Related DB Table  : purchase_requests
 * Related REST APIs : N/A
 * Depends On        : Common Module, Inventory Module, Organization Module, Security Module, Finance Module
 * Used By           : PurchaseRequestController, PurchaseRequestServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Procurement Module. Implements PurchaseRequestService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.procurement.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.*;
import com.plus33.erp.procurement.mapper.PurchaseRequestMapper;
import com.plus33.erp.procurement.repository.PurchaseOrderItemRepository;
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

import com.plus33.erp.finance.budget.repository.BudgetDimensionSetRepository;
import com.plus33.erp.finance.budget.service.BudgetService;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.entity.Account;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseRequestServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Procurement Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PurchaseRequestController
 *   --> PurchaseRequestServiceImpl (this)
 *   --> Validate business rules
 *   --> PurchaseRequestRepository (read/write 'purchase_requests')
 *   --> PurchaseRequestMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code purchase_requests}</p>
 * <p><b>Module Deps      :</b> Common, Inventory, Organization, Procurement, Security, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class PurchaseRequestServiceImpl implements PurchaseRequestService {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final CompanyRepository companyRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseRepository warehouseRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PurchaseRequestMapper purchaseRequestMapper;
    private final BudgetDimensionSetRepository budgetDimensionSetRepository;
    private final BudgetService budgetService;
    private final AccountRepository accountRepository;

    public PurchaseRequestServiceImpl(PurchaseRequestRepository purchaseRequestRepository,
                                     PurchaseOrderRepository purchaseOrderRepository,
                                     PurchaseOrderItemRepository purchaseOrderItemRepository,
                                     CompanyRepository companyRepository,
                                     SupplierRepository supplierRepository,
                                     WarehouseRepository warehouseRepository,
                                     StoreRepository storeRepository,
                                     ProductRepository productRepository,
                                     UserRepository userRepository,
                                     PurchaseRequestMapper purchaseRequestMapper,
                                     BudgetDimensionSetRepository budgetDimensionSetRepository,
                                     BudgetService budgetService,
                                     AccountRepository accountRepository) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.companyRepository = companyRepository;
        this.supplierRepository = supplierRepository;
        this.warehouseRepository = warehouseRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.purchaseRequestMapper = purchaseRequestMapper;
        this.budgetDimensionSetRepository = budgetDimensionSetRepository;
        this.budgetService = budgetService;
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new purchase request and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the PurchaseRequestResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PurchaseRequestResponse createPurchaseRequest(PurchaseRequestRequest request) {
        validateRequestDetails(request, null);

        User currentUser = getCurrentUser();

        PurchaseRequest pr = purchaseRequestMapper.toEntity(request);
        pr.setCompany(companyRepository.getReferenceById(request.companyId()));
        pr.setSupplier(supplierRepository.getReferenceById(request.supplierId()));
        pr.setRequestedBy(currentUser);
        pr.setStatus(PurchaseRequestStatus.DRAFT);

        if (request.warehouseId() != null) {
            pr.setWarehouse(warehouseRepository.getReferenceById(request.warehouseId()));
        }
        if (request.storeId() != null) {
            pr.setStore(storeRepository.getReferenceById(request.storeId()));
        }

        // Generate sequential unique request number PR-YYYY-00000X
        String requestNumber = generateRequestNumber();
        pr.setRequestNumber(requestNumber);

        // Add line items
        for (PurchaseRequestItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemReq.productId()));
            if (!product.getActive()) {
                throw new BusinessException("Cannot assign product " + product.getName() + ": Product is inactive");
            }
            PurchaseRequestItem item = purchaseRequestMapper.toItemEntity(itemReq);
            item.setProduct(product);
            item.setPurchaseRequest(pr);
            if (itemReq.dimensionSetId() != null) {
                item.setDimensionSet(budgetDimensionSetRepository.findById(itemReq.dimensionSetId()).orElse(null));
            }
            pr.getItems().add(item);
        }

        PurchaseRequest saved = purchaseRequestRepository.save(pr);
        return purchaseRequestMapper.toResponse(saved);
    }

    /**
     * Retrieves a single purchase request by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the PurchaseRequestResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single purchase request by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the PurchaseRequestResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public PurchaseRequestResponse getPurchaseRequestById(Long id) {
        PurchaseRequest pr = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Request not found with ID: " + id));
        return purchaseRequestMapper.toResponse(pr);
    }

    /**
     * Returns a filtered paginated list of purchase requests records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of purchase requests records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<PurchaseRequestResponse> searchPurchaseRequests(PurchaseRequestSearchRequest searchRequest, Pageable pageable) {
        Specification<PurchaseRequest> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.requestNumber() != null && !searchRequest.requestNumber().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("requestNumber")), "%" + searchRequest.requestNumber().toLowerCase() + "%"));
            }
            if (searchRequest.companyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.companyId()));
            }
            if (searchRequest.supplierId() != null) {
                predicates.add(cb.equal(root.get("supplier").get("id"), searchRequest.supplierId()));
            }
            if (searchRequest.warehouseId() != null) {
                predicates.add(cb.equal(root.get("warehouse").get("id"), searchRequest.warehouseId()));
            }
            if (searchRequest.storeId() != null) {
                predicates.add(cb.equal(root.get("store").get("id"), searchRequest.storeId()));
            }
            if (searchRequest.status() != null) {
                predicates.add(cb.equal(root.get("status"), searchRequest.status()));
            }
            if (searchRequest.requestedBy() != null) {
                predicates.add(cb.equal(root.get("requestedBy").get("id"), searchRequest.requestedBy()));
            }
            if (searchRequest.requestDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("requestDate"), searchRequest.requestDateFrom()));
            }
            if (searchRequest.requestDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("requestDate"), searchRequest.requestDateTo()));
            }
            if (searchRequest.requiredDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("requiredDate"), searchRequest.requiredDateFrom()));
            }
            if (searchRequest.requiredDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("requiredDate"), searchRequest.requiredDateTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<PurchaseRequest> page = purchaseRequestRepository.findAll(spec, pageable);
        List<PurchaseRequestResponse> content = page.getContent().stream()
                .map(purchaseRequestMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    /**
     * Updates an existing purchase request record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the PurchaseRequestResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PurchaseRequestResponse updatePurchaseRequest(Long id, PurchaseRequestRequest request) {
        PurchaseRequest pr = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Request not found with ID: " + id));

        if (pr.getStatus() != PurchaseRequestStatus.DRAFT) {
            throw new BusinessException("Terminal states cannot be modified: Purchase Request is in " + pr.getStatus() + " status");
        }

        validateRequestDetails(request, id);

        pr.setCompany(companyRepository.getReferenceById(request.companyId()));
        pr.setSupplier(supplierRepository.getReferenceById(request.supplierId()));
        pr.setRequiredDate(request.requiredDate());
        pr.setNotes(request.notes());

        if (request.warehouseId() != null) {
            pr.setWarehouse(warehouseRepository.getReferenceById(request.warehouseId()));
            pr.setStore(null);
        } else if (request.storeId() != null) {
            pr.setStore(storeRepository.getReferenceById(request.storeId()));
            pr.setWarehouse(null);
        }

        // Update items: clear and recreate
        pr.getItems().clear();
        for (PurchaseRequestItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemReq.productId()));
            if (!product.getActive()) {
                throw new BusinessException("Cannot assign product " + product.getName() + ": Product is inactive");
            }
            PurchaseRequestItem item = purchaseRequestMapper.toItemEntity(itemReq);
            item.setProduct(product);
            item.setPurchaseRequest(pr);
            if (itemReq.dimensionSetId() != null) {
                item.setDimensionSet(budgetDimensionSetRepository.findById(itemReq.dimensionSetId()).orElse(null));
            }
            pr.getItems().add(item);
        }

        PurchaseRequest saved = purchaseRequestRepository.save(pr);
        return purchaseRequestMapper.toResponse(saved);
    }

    /**
     * Submits the purchase request for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the PurchaseRequestResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PurchaseRequestResponse submitPurchaseRequest(Long id) {
        PurchaseRequest pr = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Request not found with ID: " + id));

        if (pr.getStatus() != PurchaseRequestStatus.DRAFT) {
            throw new BusinessException("Unauthorized transitions: Cannot submit Purchase Request from " + pr.getStatus() + " status");
        }

        pr.setStatus(PurchaseRequestStatus.SUBMITTED);
        pr.setSubmittedBy(getCurrentUser());
        pr.setSubmittedAt(LocalDateTime.now());

        PurchaseRequest saved = purchaseRequestRepository.save(pr);

        // budget reservation
        for (PurchaseRequestItem item : saved.getItems()) {
            if (item.getDimensionSet() != null) {
                try {
                    String accountCode = isInventoryProduct(item.getProduct()) ? "1300" : "5200";
                    Account account = accountRepository.findByCompanyIdAndAccountCode(saved.getCompany().getId(), accountCode)
                        .orElseThrow(() -> new BusinessException("Account not found: " + accountCode));

                    BigDecimal amount = item.getRequestedQuantity().multiply(BigDecimal.valueOf(100.00));

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
                        "PROCUREMENT_PR",
                        item.getId(),
                        saved.getRequestNumber(),
                        LocalDate.now().plusDays(30)
                    );
                    budgetService.createReservation(saved.getCompany().getId(), resReq);
                } catch (Exception e) {
                    log.error("Failed to create budget reservation for PR item ID: {}", item.getId(), e);
                    throw e;
                }
            }
        }

        return purchaseRequestMapper.toResponse(saved);
    }

    /**
     * Approves the purchase request, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the PurchaseRequestResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PurchaseRequestResponse approvePurchaseRequest(Long id) {
        PurchaseRequest pr = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Request not found with ID: " + id));

        if (pr.getStatus() != PurchaseRequestStatus.SUBMITTED) {
            throw new BusinessException("Unauthorized transitions: Cannot approve Purchase Request from " + pr.getStatus() + " status");
        }

        pr.setStatus(PurchaseRequestStatus.APPROVED);
        pr.setApprovedBy(getCurrentUser());
        pr.setApprovedAt(LocalDateTime.now());

        // Snapshot approved quantities
        for (PurchaseRequestItem item : pr.getItems()) {
            item.setApprovedQuantity(item.getRequestedQuantity());
        }

        PurchaseRequest saved = purchaseRequestRepository.save(pr);
        return purchaseRequestMapper.toResponse(saved);
    }

    /**
     * Performs the rejectPurchaseRequest operation in this module.
     *
     * @param id the unique database ID of the resource
     * @param rejectionReason the rejectionReason input value
     * @return the PurchaseRequestResponse result
     */
    @Override
    @Transactional
    public PurchaseRequestResponse rejectPurchaseRequest(Long id, String rejectionReason) {
        PurchaseRequest pr = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Request not found with ID: " + id));

        if (pr.getStatus() != PurchaseRequestStatus.SUBMITTED) {
            throw new BusinessException("Unauthorized transitions: Cannot reject Purchase Request from " + pr.getStatus() + " status");
        }

        if (rejectionReason == null || rejectionReason.isBlank()) {
            throw new BusinessException("Rejection reason is required");
        }

        pr.setStatus(PurchaseRequestStatus.REJECTED);
        pr.setApprovedBy(getCurrentUser()); // set acting user
        pr.setRejectedAt(LocalDateTime.now());
        pr.setRejectionReason(rejectionReason);

        PurchaseRequest saved = purchaseRequestRepository.save(pr);

        // Release reservations
        for (PurchaseRequestItem item : pr.getItems()) {
            budgetService.releaseReservation("PROCUREMENT_PR", item.getId());
        }

        return purchaseRequestMapper.toResponse(saved);
    }

    /**
     * Cancels the purchase request and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param cancellationReason the cancellationReason input value
     * @return the PurchaseRequestResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PurchaseRequestResponse cancelPurchaseRequest(Long id, String cancellationReason) {
        PurchaseRequest pr = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Request not found with ID: " + id));

        if (pr.getStatus() != PurchaseRequestStatus.SUBMITTED) {
            throw new BusinessException("Unauthorized transitions: Cannot cancel Purchase Request from " + pr.getStatus() + " status");
        }

        if (cancellationReason == null || cancellationReason.isBlank()) {
            throw new BusinessException("Cancellation reason is required");
        }

        pr.setStatus(PurchaseRequestStatus.CANCELLED);
        pr.setCancelledAt(LocalDateTime.now());
        pr.setCancellationReason(cancellationReason);

        PurchaseRequest saved = purchaseRequestRepository.save(pr);

        // Release reservations
        for (PurchaseRequestItem item : pr.getItems()) {
            budgetService.releaseReservation("PROCUREMENT_PR", item.getId());
        }

        return purchaseRequestMapper.toResponse(saved);
    }

    private boolean isInventoryProduct(Product product) {
        if (product.getProductType() == null) {
            return true;
        }
        String type = product.getProductType().toUpperCase();
        return !type.equals("SERVICE") && !type.equals("EXPENSE");
    }

    /**
     * Converts between Entity and DTO representations (MapStruct).
     *
     * @param id the unique database ID of the resource
     * @return the PurchaseRequestResponse result
     */
    @Override
    @Transactional
    public PurchaseRequestResponse convertPurchaseRequestToPo(Long id) {
        PurchaseRequest pr = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Request not found with ID: " + id));

        if (pr.getPurchaseOrder() != null) {
            throw new BusinessException("Double conversion attempts: Purchase Request is already linked to Purchase Order " + pr.getPurchaseOrder().getOrderNumber());
        }

        if (pr.getStatus() != PurchaseRequestStatus.APPROVED) {
            throw new BusinessException("Unauthorized transitions: Cannot convert Purchase Request to PO from " + pr.getStatus() + " status");
        }

        User currentUser = getCurrentUser();

        // 1. Create PurchaseOrder
        PurchaseOrder po = new PurchaseOrder();
        po.setCompany(pr.getCompany());
        po.setSupplier(pr.getSupplier());
        po.setPurchaseRequest(pr);
        po.setOrderedBy(currentUser);
        po.setExpectedDeliveryDate(pr.getRequiredDate());
        po.setStatus(PurchaseOrderStatus.DRAFT);
        po.setOrderNumber(generateOrderNumber());
        po.setDiscountAmount(BigDecimal.ZERO);
        po.setTaxAmount(BigDecimal.ZERO);
        po.setSubtotalAmount(BigDecimal.ZERO);
        po.setTotalAmount(BigDecimal.ZERO);
        po.setCurrencyCode("AED");

        PurchaseOrder savedPo = purchaseOrderRepository.save(po);

        // 2. Map items
        for (PurchaseRequestItem prItem : pr.getItems()) {
            PurchaseOrderItem poItem = new PurchaseOrderItem();
            poItem.setPurchaseOrder(savedPo);
            poItem.setProduct(prItem.getProduct());
            poItem.setOrderedQuantity(prItem.getApprovedQuantity());
            poItem.setUnitPrice(BigDecimal.ZERO);
            poItem.setReceivedQuantity(BigDecimal.ZERO);
            poItem.setRemainingQuantity(prItem.getApprovedQuantity());
            purchaseOrderItemRepository.save(poItem);
        }

        // 3. Link PO back to request and advance state
        pr.setPurchaseOrder(savedPo);
        pr.setStatus(PurchaseRequestStatus.CONVERTED_TO_PO);
        pr.setConvertedToPoAt(LocalDateTime.now());

        PurchaseRequest savedRequest = purchaseRequestRepository.save(pr);
        return purchaseRequestMapper.toResponse(savedRequest);
    }

    private void validateRequestDetails(PurchaseRequestRequest request, Long existingId) {
        // Required date cannot be in the past
        if (request.requiredDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Required date cannot be in the past");
        }

        // XOR destination verification
        boolean hasWarehouse = request.warehouseId() != null;
        boolean hasStore = request.storeId() != null;
        if ((hasWarehouse && hasStore) || (!hasWarehouse && !hasStore)) {
            throw new BusinessException("Only one destination type is allowed: must specify either warehouseId or storeId");
        }

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        if (!company.getActive()) {
            throw new BusinessException("Company is inactive");
        }

        // Destination company mismatch
        if (hasWarehouse) {
            Warehouse warehouse = warehouseRepository.findById(request.warehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.warehouseId()));
            if (!warehouse.getActive()) {
                throw new BusinessException("Warehouse is inactive");
            }
            if (!warehouse.getRegion().getCompany().getId().equals(request.companyId())) {
                throw new BusinessException("Warehouse does not belong to the selected company");
            }
        } else {
            Store store = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.storeId()));
            if (!store.getActive()) {
                throw new BusinessException("Store is inactive");
            }
            if (!store.getRegion().getCompany().getId().equals(request.companyId())) {
                throw new BusinessException("Store does not belong to the selected company");
            }
        }

        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + request.supplierId()));
        if (!supplier.getActive()) {
            throw new BusinessException("Supplier is inactive");
        }
        if (!supplier.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Supplier must belong to the same company");
        }

        if (request.items() == null || request.items().isEmpty()) {
            throw new BusinessException("Purchase request must contain at least one line item");
        }
    }

    private synchronized String generateRequestNumber() {
        Long seqVal = purchaseRequestRepository.getNextSequenceValue();
        return String.format("PR-%d-%06d", LocalDate.now().getYear(), seqVal);
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