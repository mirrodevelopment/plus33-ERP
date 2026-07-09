/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : GoodsReceiptServiceImpl.java
 * Purpose           : Business logic service layer for Procurement Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GoodsReceiptController
 * Related Service   : GoodsReceiptServiceImpl
 * Related Repository: GoodsReceiptRepository, PurchaseOrderRepository, CompanyRepository, WarehouseRepository, StoreRepository, UserRepository, InventoryStockRepository, StockMovementRepository
 * Related Entity    : GoodsReceipt
 * Related DTO       : getPurchaseRequest, GoodsReceiptItemRequest, GoodsReceiptRequest, GoodsReceiptResponse, GoodsReceiptSearchRequest
 * Related Mapper    : GoodsReceiptMapper
 * Related DB Table  : goods_receipts
 * Related REST APIs : N/A
 * Depends On        : Common Module, Inventory Module, Analytics Module, Organization Module, Security Module
 * Used By           : GoodsReceiptController, GoodsReceiptServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Procurement Module. Implements GoodsReceiptService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.procurement.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.DuplicateResourceException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.inventory.entity.InventoryStock;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.StockMovement;
import com.plus33.erp.inventory.entity.StockMovementReferenceType;
import com.plus33.erp.inventory.event.InventoryRefreshEvent;
import com.plus33.erp.analytics.event.ProcurementRefreshEvent;
import com.plus33.erp.inventory.repository.InventoryStockRepository;
import com.plus33.erp.inventory.repository.StockMovementRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.*;
import com.plus33.erp.procurement.mapper.GoodsReceiptMapper;
import com.plus33.erp.procurement.repository.GoodsReceiptRepository;
import com.plus33.erp.procurement.repository.PurchaseOrderItemRepository;
import com.plus33.erp.procurement.repository.PurchaseOrderRepository;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code GoodsReceiptServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Procurement Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * GoodsReceiptController
 *   --> GoodsReceiptServiceImpl (this)
 *   --> Validate business rules
 *   --> GoodsReceiptRepository (read/write 'goods_receipts')
 *   --> GoodsReceiptMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code goods_receipts}</p>
 * <p><b>Module Deps      :</b> Common, Inventory, Analytics, Organization, Procurement, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class GoodsReceiptServiceImpl implements GoodsReceiptService {

    private final GoodsReceiptRepository goodsReceiptRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseRepository warehouseRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final InventoryStockRepository inventoryStockRepository;
    private final StockMovementRepository stockMovementRepository;
    private final GoodsReceiptMapper goodsReceiptMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final com.plus33.erp.inventory.service.InventoryTraceabilityService inventoryTraceabilityService;

    public GoodsReceiptServiceImpl(GoodsReceiptRepository goodsReceiptRepository,
                                    PurchaseOrderRepository purchaseOrderRepository,
                                    CompanyRepository companyRepository,
                                    WarehouseRepository warehouseRepository,
                                    StoreRepository storeRepository,
                                    UserRepository userRepository,
                                    InventoryStockRepository inventoryStockRepository,
                                    StockMovementRepository stockMovementRepository,
                                    GoodsReceiptMapper goodsReceiptMapper,
                                    ApplicationEventPublisher eventPublisher,
                                    com.plus33.erp.inventory.service.InventoryTraceabilityService inventoryTraceabilityService) {
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.companyRepository = companyRepository;
        this.warehouseRepository = warehouseRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.inventoryStockRepository = inventoryStockRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.goodsReceiptMapper = goodsReceiptMapper;
        this.eventPublisher = eventPublisher;
        this.inventoryTraceabilityService = inventoryTraceabilityService;
    }

    /**
     * Creates a new goods receipt and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the IdempotentCreateResult result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public IdempotentCreateResult<GoodsReceiptResponse> createGoodsReceipt(GoodsReceiptRequest request) {
        // 1. Validate Idempotency
        Optional<GoodsReceipt> existingOpt = goodsReceiptRepository.findByClientReferenceId(request.clientReferenceId());
        if (existingOpt.isPresent()) {
            GoodsReceipt existing = existingOpt.get();
            if (isDuplicatePayload(existing, request)) {
                return new IdempotentCreateResult<>(goodsReceiptMapper.toResponse(existing), false);
            } else {
                throw new DuplicateResourceException("Client reference ID already exists with different request data.");
            }
        }

        // 2. Validate Purchase Order status
        PurchaseOrder po = purchaseOrderRepository.findById(request.purchaseOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + request.purchaseOrderId()));

        if (po.getStatus() != PurchaseOrderStatus.ISSUED && po.getStatus() != PurchaseOrderStatus.PARTIALLY_RECEIVED) {
            throw new BusinessException("Goods receipts can only be recorded against ISSUED or PARTIALLY_RECEIVED purchase orders. Current status: " + po.getStatus());
        }

        // 3. Validate Company
        if (!po.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Goods receipt company does not match purchase order company");
        }

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        if (!company.getActive()) {
            throw new BusinessException("Company is inactive");
        }

        // 4. Validate Destination
        boolean hasWarehouse = request.warehouseId() != null;
        boolean hasStore = request.storeId() != null;
        if ((hasWarehouse && hasStore) || (!hasWarehouse && !hasStore)) {
            throw new BusinessException("Must specify either warehouseId or storeId");
        }

        if (po.getPurchaseRequest() != null) {
            PurchaseRequest pr = po.getPurchaseRequest();
            if (pr.getWarehouse() != null) {
                if (!hasWarehouse || !request.warehouseId().equals(pr.getWarehouse().getId())) {
                    throw new BusinessException("Destination warehouse does not match the purchase order's destination warehouse");
                }
            } else if (pr.getStore() != null) {
                if (!hasStore || !request.storeId().equals(pr.getStore().getId())) {
                    throw new BusinessException("Destination store does not match the purchase order's destination store");
                }
            }
        }

        Warehouse warehouse = null;
        Store store = null;
        if (hasWarehouse) {
            warehouse = warehouseRepository.findById(request.warehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.warehouseId()));
            if (!warehouse.getActive()) {
                throw new BusinessException("Warehouse is inactive");
            }
        } else {
            store = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.storeId()));
            if (!store.getActive()) {
                throw new BusinessException("Store is inactive");
            }
        }

        // 5. Validate Product list (matching and duplicates check)
        Set<Long> requestProductIds = new HashSet<>();
        for (GoodsReceiptItemRequest itemReq : request.items()) {
            if (!requestProductIds.add(itemReq.productId())) {
                throw new BusinessException("Duplicate product lines detected in the receipt submission");
            }
        }

        // 6. Validate quantities and items mapping
        Map<Long, PurchaseOrderItem> poItemMap = new HashMap<>();
        for (PurchaseOrderItem poItem : po.getItems()) {
            poItemMap.put(poItem.getProduct().getId(), poItem);
        }

        User currentUser = getCurrentUser();
        GoodsReceipt gr = goodsReceiptMapper.toEntity(request);
        gr.setPurchaseOrder(po);
        gr.setCompany(company);
        gr.setWarehouse(warehouse);
        gr.setStore(store);
        gr.setReceivedBy(currentUser);
        gr.setStatus(GoodsReceiptStatus.COMPLETED);
        gr.setReceiptNumber(generateReceiptNumber());

        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalAmt = BigDecimal.ZERO;

        for (GoodsReceiptItemRequest itemReq : request.items()) {
            PurchaseOrderItem poItem = poItemMap.get(itemReq.productId());
            if (poItem == null) {
                throw new BusinessException("Product " + itemReq.productId() + " is not present on the linked Purchase Order");
            }

            if (itemReq.receivedQuantity().compareTo(poItem.getRemainingQuantity()) > 0) {
                throw new BusinessException("Received quantity for product " + poItem.getProduct().getName() 
                        + " exceeds remaining quantity. Received: " + itemReq.receivedQuantity() 
                        + ", Remaining: " + poItem.getRemainingQuantity());
            }

            GoodsReceiptItem grItem = goodsReceiptMapper.toItemEntity(itemReq);
            grItem.setProduct(poItem.getProduct());
            grItem.setGoodsReceipt(gr);
            gr.getItems().add(grItem);

            // Update totals
            totalQty = totalQty.add(itemReq.receivedQuantity());
            totalAmt = totalAmt.add(itemReq.receivedQuantity().multiply(poItem.getUnitPrice()));

            // Update PO item counts
            poItem.setReceivedQuantity(poItem.getReceivedQuantity().add(itemReq.receivedQuantity()));
            poItem.setRemainingQuantity(poItem.getOrderedQuantity().subtract(poItem.getReceivedQuantity()));

            // Update stock
            updateInventoryStock(poItem.getProduct(), warehouse, store, itemReq.receivedQuantity());

            // Create stock movement
            createStockMovement(poItem.getProduct(), warehouse, store, itemReq.receivedQuantity(), gr, StockMovementReferenceType.GOODS_RECEIPT, currentUser);
        }

        gr.setTotalQuantity(totalQty);
        gr.setTotalAmount(totalAmt);

        // Recalculate PO Status and percentage
        recalculatePurchaseOrderStatus(po);

        GoodsReceipt saved = goodsReceiptRepository.save(gr);

        // Record trace events
        for (GoodsReceiptItem item : saved.getItems()) {
            inventoryTraceabilityService.recordTraceEvent(
                    saved.getCompany().getId(),
                    item.getProduct().getId(),
                    null, // lotId
                    null, // serialId
                    saved.getWarehouse() != null ? saved.getWarehouse().getId() : null,
                    saved.getStore() != null ? saved.getStore().getId() : null,
                    com.plus33.erp.inventory.entity.InventoryTraceEventType.RECEIPT,
                    item.getReceivedQuantity(),
                    com.plus33.erp.inventory.entity.InventoryTraceReferenceType.GOODS_RECEIPT,
                    saved.getId(),
                    saved.getReceiptNumber(),
                    "Goods received from Purchase Order #" + po.getOrderNumber()
            );
        }

        // Publish event for transactional async refresh
        eventPublisher.publishEvent(new InventoryRefreshEvent(this));
        eventPublisher.publishEvent(new ProcurementRefreshEvent(this));

        return new IdempotentCreateResult<>(goodsReceiptMapper.toResponse(saved), true);
    }

    /**
     * Retrieves a single goods receipt by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the GoodsReceiptResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single goods receipt by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the GoodsReceiptResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public GoodsReceiptResponse getGoodsReceiptById(Long id) {
        GoodsReceipt gr = goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goods Receipt not found with ID: " + id));
        return goodsReceiptMapper.toResponse(gr);
    }

    /**
     * Returns a filtered paginated list of goods receipts records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of goods receipts records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<GoodsReceiptResponse> searchGoodsReceipts(GoodsReceiptSearchRequest searchRequest, Pageable pageable) {
        Specification<GoodsReceipt> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.receiptNumber() != null && !searchRequest.receiptNumber().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("receiptNumber")), "%" + searchRequest.receiptNumber().toLowerCase() + "%"));
            }
            if (searchRequest.companyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.companyId()));
            }
            if (searchRequest.purchaseOrderId() != null) {
                predicates.add(cb.equal(root.get("purchaseOrder").get("id"), searchRequest.purchaseOrderId()));
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
            if (searchRequest.receivedAtFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("receivedAt"), searchRequest.receivedAtFrom().atStartOfDay()));
            }
            if (searchRequest.receivedAtTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("receivedAt"), searchRequest.receivedAtTo().atTime(LocalTime.MAX)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<GoodsReceipt> page = goodsReceiptRepository.findAll(spec, pageable);
        List<GoodsReceiptResponse> content = page.getContent().stream()
                .map(goodsReceiptMapper::toResponse)
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
     * Updates an existing goods receipt record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the GoodsReceiptResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public GoodsReceiptResponse updateGoodsReceipt(Long id, GoodsReceiptUpdateRequest request) {
        GoodsReceipt gr = goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goods Receipt not found with ID: " + id));

        if (gr.getStatus() != GoodsReceiptStatus.COMPLETED) {
            throw new BusinessException("Cannot update remarks/references on non-COMPLETED Goods Receipt.");
        }

        if (request.remarks() != null) {
            gr.setRemarks(request.remarks());
        }
        if (request.supplierDeliveryNote() != null) {
            gr.setSupplierDeliveryNote(request.supplierDeliveryNote());
        }
        if (request.supplierInvoiceNumber() != null) {
            gr.setSupplierInvoiceNumber(request.supplierInvoiceNumber());
        }

        GoodsReceipt saved = goodsReceiptRepository.save(gr);
        return goodsReceiptMapper.toResponse(saved);
    }

    /**
     * Cancels the goods receipt and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param reason the reason input value
     * @return the GoodsReceiptResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public GoodsReceiptResponse cancelGoodsReceipt(Long id, String reason) {
        GoodsReceipt gr = goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goods Receipt not found with ID: " + id));

        if (gr.getStatus() != GoodsReceiptStatus.COMPLETED) {
            throw new BusinessException("Only COMPLETED goods receipts can be cancelled");
        }

        if (reason == null || reason.isBlank()) {
            throw new BusinessException("Cancellation reason is required");
        }

        User currentUser = getCurrentUser();

        // 1. Process items reversals under locks to check inventory integrity
        for (GoodsReceiptItem grItem : gr.getItems()) {
            InventoryStock stock = getLockedInventoryStock(grItem.getProduct(), gr.getWarehouse(), gr.getStore());
            if (stock == null || stock.getQuantity().compareTo(grItem.getReceivedQuantity()) < 0) {
                throw new BusinessException("Inventory underflow detected. Cannot cancel goods receipt.");
            }

            // Subtract stock
            stock.setQuantity(stock.getQuantity().subtract(grItem.getReceivedQuantity()));

            // Counter movement
            createStockMovement(grItem.getProduct(), gr.getWarehouse(), gr.getStore(), grItem.getReceivedQuantity().negate(), gr, StockMovementReferenceType.GOODS_RECEIPT_CANCEL, currentUser);

            // Revert PO line counts
            PurchaseOrder po = gr.getPurchaseOrder();
            Optional<PurchaseOrderItem> poItemOpt = po.getItems().stream()
                    .filter(pi -> pi.getProduct().getId().equals(grItem.getProduct().getId()))
                    .findFirst();
            if (poItemOpt.isPresent()) {
                PurchaseOrderItem poItem = poItemOpt.get();
                poItem.setReceivedQuantity(poItem.getReceivedQuantity().subtract(grItem.getReceivedQuantity()));
                poItem.setRemainingQuantity(poItem.getOrderedQuantity().subtract(poItem.getReceivedQuantity()));
            }
        }

        // 2. Set Cancellation headers
        gr.setStatus(GoodsReceiptStatus.CANCELLED);
        gr.setCancelledBy(currentUser);
        gr.setCancelledAt(LocalDateTime.now());
        gr.setCancellationReason(reason);

        // 3. Recalculate PO Status and reset timestamp if rolled back from 100%
        recalculatePurchaseOrderStatus(gr.getPurchaseOrder());

        GoodsReceipt saved = goodsReceiptRepository.save(gr);

        // Publish event for transactional async refresh
        eventPublisher.publishEvent(new InventoryRefreshEvent(this));
        eventPublisher.publishEvent(new ProcurementRefreshEvent(this));

        return goodsReceiptMapper.toResponse(saved);
    }

    private boolean isDuplicatePayload(GoodsReceipt existing, GoodsReceiptRequest request) {
        if (!existing.getPurchaseOrder().getId().equals(request.purchaseOrderId()) ||
                !existing.getCompany().getId().equals(request.companyId())) {
            return false;
        }

        if (existing.getWarehouse() != null && !existing.getWarehouse().getId().equals(request.warehouseId())) {
            return false;
        }
        if (existing.getWarehouse() == null && request.warehouseId() != null) {
            return false;
        }

        if (existing.getStore() != null && !existing.getStore().getId().equals(request.storeId())) {
            return false;
        }
        if (existing.getStore() == null && request.storeId() != null) {
            return false;
        }

        if (existing.getItems().size() != request.items().size()) {
            return false;
        }

        Map<Long, BigDecimal> existingItemsMap = new HashMap<>();
        for (GoodsReceiptItem item : existing.getItems()) {
            existingItemsMap.put(item.getProduct().getId(), item.getReceivedQuantity());
        }

        for (GoodsReceiptItemRequest reqItem : request.items()) {
            BigDecimal qty = existingItemsMap.get(reqItem.productId());
            if (qty == null || qty.compareTo(reqItem.receivedQuantity()) != 0) {
                return false;
            }
        }

        return true;
    }

    private void updateInventoryStock(Product product, Warehouse warehouse, Store store, BigDecimal receivedQty) {
        Optional<InventoryStock> stockOpt;
        if (warehouse != null) {
            stockOpt = inventoryStockRepository.findByProductIdAndWarehouseId(product.getId(), warehouse.getId());
        } else {
            stockOpt = inventoryStockRepository.findByProductIdAndStoreId(product.getId(), store.getId());
        }

        InventoryStock stock;
        if (stockOpt.isPresent()) {
            stock = stockOpt.get();
            stock.setQuantity(stock.getQuantity().add(receivedQty));
        } else {
            stock = new InventoryStock();
            stock.setProduct(product);
            stock.setWarehouse(warehouse);
            stock.setStore(store);
            stock.setQuantity(receivedQty);
        }
        inventoryStockRepository.save(stock);
    }

    private InventoryStock getLockedInventoryStock(Product product, Warehouse warehouse, Store store) {
        if (warehouse != null) {
            return inventoryStockRepository.findWithPessimisticWriteByProductIdAndWarehouseId(product.getId(), warehouse.getId())
                    .orElse(null);
        } else {
            return inventoryStockRepository.findWithPessimisticWriteByProductIdAndStoreId(product.getId(), store.getId())
                    .orElse(null);
        }
    }

    private void createStockMovement(Product product, Warehouse warehouse, Store store, BigDecimal quantity,
                                     GoodsReceipt gr, StockMovementReferenceType refType, User user) {
        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setWarehouse(warehouse);
        movement.setStore(store);
        movement.setMovementType(refType.name());
        movement.setQuantity(quantity);
        movement.setReferenceNo(gr.getReceiptNumber());
        movement.setReferenceType(refType);
        movement.setReferenceId(gr.getId());
        movement.setReferenceNumber(gr.getReceiptNumber());
        movement.setCreatedBy(user);
        stockMovementRepository.save(movement);
    }

    private void recalculatePurchaseOrderStatus(PurchaseOrder po) {
        BigDecimal totalOrdered = BigDecimal.ZERO;
        BigDecimal totalReceived = BigDecimal.ZERO;

        for (PurchaseOrderItem item : po.getItems()) {
            totalOrdered = totalOrdered.add(item.getOrderedQuantity());
            totalReceived = totalReceived.add(item.getReceivedQuantity());
        }

        BigDecimal percentage = BigDecimal.ZERO;
        if (totalOrdered.compareTo(BigDecimal.ZERO) > 0) {
            percentage = totalReceived.multiply(BigDecimal.valueOf(100))
                    .divide(totalOrdered, 2, RoundingMode.HALF_UP);
        }

        po.setReceivedPercentage(percentage);

        if (totalReceived.compareTo(BigDecimal.ZERO) == 0) {
            po.setStatus(PurchaseOrderStatus.ISSUED);
            po.setReceivedAt(null);
        } else if (totalReceived.compareTo(totalOrdered) >= 0) {
            po.setStatus(PurchaseOrderStatus.RECEIVED);
            if (po.getReceivedAt() == null) {
                po.setReceivedAt(LocalDateTime.now());
            }
        } else {
            po.setStatus(PurchaseOrderStatus.PARTIALLY_RECEIVED);
            po.setReceivedAt(null);
        }
    }

    private synchronized String generateReceiptNumber() {
        Long seqVal = goodsReceiptRepository.getNextSequenceValue();
        return String.format("GR-%d-%06d", LocalDate.now().getYear(), seqVal);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found with email: " + email));
    }
}