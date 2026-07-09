/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.service
 * File              : PickListServiceImpl.java
 * Purpose           : Business logic service layer for Sales Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListController
 * Related Service   : PickListServiceImpl
 * Related Repository: PickListRepository, InventoryAllocationRepository, SalesOrderRepository, SalesOrderItemRepository, CompanyRepository, WarehouseRepository, StoreRepository, ProductRepository, UserRepository, InventoryStockRepository, StockMovementRepository
 * Related Entity    : PickList
 * Related DTO       : CompletePickingRequest, PickListRequest, PickListResponse, ShipRequest, toResponse
 * Related Mapper    : PickListMapper
 * Related DB Table  : pick_lists
 * Related REST APIs : N/A
 * Depends On        : Common Module, Inventory Module, Organization Module, Security Module
 * Used By           : PickListController, PickListServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Sales Module. Implements PickListService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.sales.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.inventory.entity.InventoryStock;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.StockMovement;
import com.plus33.erp.inventory.entity.StockMovementReferenceType;
import com.plus33.erp.inventory.repository.InventoryStockRepository;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.inventory.repository.StockMovementRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.*;
import com.plus33.erp.sales.event.PickListRefreshEvent;
import com.plus33.erp.sales.mapper.PickListMapper;
import com.plus33.erp.sales.repository.InventoryAllocationRepository;
import com.plus33.erp.sales.repository.PickListRepository;
import com.plus33.erp.sales.repository.SalesOrderItemRepository;
import com.plus33.erp.sales.repository.SalesOrderRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code PickListServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Sales Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PickListController
 *   --> PickListServiceImpl (this)
 *   --> Validate business rules
 *   --> PickListRepository (read/write 'pick_lists')
 *   --> PickListMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code pick_lists}</p>
 * <p><b>Module Deps      :</b> Common, Inventory, Organization, Sales, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class PickListServiceImpl implements PickListService {

    private final PickListRepository pickListRepository;
    private final InventoryAllocationRepository inventoryAllocationRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseRepository warehouseRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryStockRepository inventoryStockRepository;
    private final StockMovementRepository stockMovementRepository;
    private final PickListMapper pickListMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public PickListServiceImpl(
            PickListRepository pickListRepository,
            InventoryAllocationRepository inventoryAllocationRepository,
            SalesOrderRepository salesOrderRepository,
            SalesOrderItemRepository salesOrderItemRepository,
            CompanyRepository companyRepository,
            WarehouseRepository warehouseRepository,
            StoreRepository storeRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            InventoryStockRepository inventoryStockRepository,
            StockMovementRepository stockMovementRepository,
            PickListMapper pickListMapper,
            ApplicationEventPublisher applicationEventPublisher) {
        this.pickListRepository = pickListRepository;
        this.inventoryAllocationRepository = inventoryAllocationRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.companyRepository = companyRepository;
        this.warehouseRepository = warehouseRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.inventoryStockRepository = inventoryStockRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.pickListMapper = pickListMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Creates a new pick list and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the PickListResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PickListResponse createPickList(PickListRequest request) {
        // 1. Idempotency check
        Optional<PickList> existing = pickListRepository.findByCompanyIdAndClientReferenceId(
                request.companyId(), request.clientReferenceId()
        );
        if (existing.isPresent()) {
            return pickListMapper.toResponse(existing.get());
        }

        // 2. Fetch Company & Sales Order
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        if (!company.getActive()) {
            throw new BusinessException("Company is inactive");
        }

        SalesOrder salesOrder = salesOrderRepository.findById(request.salesOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found with ID: " + request.salesOrderId()));

        if (!salesOrder.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Sales order does not belong to the requested company");
        }

        // Validate Sales Order status
        if (salesOrder.getStatus() != SalesOrderStatus.APPROVED && salesOrder.getStatus() != SalesOrderStatus.PARTIALLY_FULFILLED) {
            throw new BusinessException("Sales order must be in APPROVED or PARTIALLY_FULFILLED status to create a pick list");
        }

        // 3. Location XOR checks
        if ((request.warehouseId() != null && request.storeId() != null) ||
            (request.warehouseId() == null && request.storeId() == null)) {
            throw new BusinessException("Exactly one of Warehouse ID or Store ID must be specified");
        }

        Warehouse warehouse = null;
        Store store = null;
        List<PickListStatus> inactiveStatuses = List.of(PickListStatus.SHIPPED, PickListStatus.CANCELLED);

        if (request.warehouseId() != null) {
            warehouse = warehouseRepository.findById(request.warehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.warehouseId()));
            if (!warehouse.getRegion().getCompany().getId().equals(request.companyId())) {
                throw new BusinessException("Warehouse does not belong to the requested company");
            }
            // Check active pick lists at this warehouse
            if (pickListRepository.existsBySalesOrderIdAndWarehouseIdAndStatusNotIn(salesOrder.getId(), request.warehouseId(), inactiveStatuses)) {
                throw new BusinessException("An active pick list already exists for this sales order at the specified warehouse");
            }
        } else {
            store = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.storeId()));
            if (!store.getRegion().getCompany().getId().equals(request.companyId())) {
                throw new BusinessException("Store does not belong to the requested company");
            }
            // Check active pick lists at this store
            if (pickListRepository.existsBySalesOrderIdAndStoreIdAndStatusNotIn(salesOrder.getId(), request.storeId(), inactiveStatuses)) {
                throw new BusinessException("An active pick list already exists for this sales order at the specified store");
            }
        }

        // 4. Generate Pick Number
        Long seqVal = pickListRepository.getNextSequenceValue();
        String pickNumber = String.format("PK-%d-%06d", LocalDate.now().getYear(), seqVal);

        // 5. Create Pick List
        PickList pickList = PickList.builder()
                .company(company)
                .salesOrder(salesOrder)
                .pickNumber(pickNumber)
                .clientReferenceId(request.clientReferenceId())
                .status(PickListStatus.DRAFT)
                .warehouse(warehouse)
                .store(store)
                .createdBy(getCurrentUser())
                .build();

        // 6. Add lines for items needing allocation
        boolean hasLines = false;
        for (SalesOrderItem soItem : salesOrder.getItems()) {
            BigDecimal remainingToAllocate = soItem.getOrderedQuantity().subtract(soItem.getAllocatedQuantity());
            if (remainingToAllocate.compareTo(BigDecimal.ZERO) > 0) {
                PickListItem pickItem = PickListItem.builder()
                        .salesOrderItem(soItem)
                        .product(soItem.getProduct())
                        .orderedQuantity(remainingToAllocate)
                        .allocatedQuantity(BigDecimal.ZERO)
                        .pickedQuantity(BigDecimal.ZERO)
                        .shippedQuantity(BigDecimal.ZERO)
                        .build();
                pickList.addItem(pickItem);
                hasLines = true;
            }
        }

        if (!hasLines) {
            throw new BusinessException("All items in the sales order are already fully allocated");
        }

        PickList saved = pickListRepository.save(pickList);
        return pickListMapper.toResponse(saved);
    }

    /**
     * Retrieves a paginated list of pick list by id records.
     *
     * @param id the unique database ID of the resource
     * @return the PickListResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a paginated list of pick list by id records.
     *
     * @param id the unique database ID of the resource
     * @return the PickListResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public PickListResponse getPickListById(Long id) {
        PickList pickList = pickListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pick list not found with ID: " + id));
        return pickListMapper.toResponse(pickList);
    }

    /**
     * Releases previously reserved pick list resources back to the available pool.
     *
     * @param id the unique database ID of the resource
     * @return the PickListResponse result
     */
    @Override
    @Transactional
    public PickListResponse releasePickList(Long id) {
        PickList pickList = pickListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pick list not found with ID: " + id));

        if (pickList.getStatus() != PickListStatus.DRAFT) {
            throw new BusinessException("Only DRAFT pick lists can be released");
        }

        User currentUser = getCurrentUser();

        for (PickListItem item : pickList.getItems()) {
            // Pessimistic Write Lock on InventoryStock
            Optional<InventoryStock> stockOpt;
            if (pickList.getWarehouse() != null) {
                stockOpt = inventoryStockRepository.findWithPessimisticWriteByProductIdAndWarehouseId(
                        item.getProduct().getId(), pickList.getWarehouse().getId()
                );
            } else {
                stockOpt = inventoryStockRepository.findWithPessimisticWriteByProductIdAndStoreId(
                        item.getProduct().getId(), pickList.getStore().getId()
                );
            }

            BigDecimal availableStock = BigDecimal.ZERO;
            if (stockOpt.isPresent()) {
                availableStock = stockOpt.get().getQuantity().subtract(stockOpt.get().getReservedQuantity());
                if (availableStock.compareTo(BigDecimal.ZERO) < 0) {
                    availableStock = BigDecimal.ZERO;
                }
            }

            // Allocate up to ordered quantity or available stock
            BigDecimal toAllocate = item.getOrderedQuantity().min(availableStock);

            if (toAllocate.compareTo(BigDecimal.ZERO) > 0) {
                // Update InventoryStock reservation
                InventoryStock stock = stockOpt.get();
                stock.setReservedQuantity(stock.getReservedQuantity().add(toAllocate));
                inventoryStockRepository.save(stock);

                // Update PickListItem allocation
                item.setAllocatedQuantity(toAllocate);

                // Update SalesOrderItem allocation
                SalesOrderItem soItem = item.getSalesOrderItem();
                soItem.setAllocatedQuantity(soItem.getAllocatedQuantity().add(toAllocate));
                salesOrderItemRepository.save(soItem);

                // Create InventoryAllocation record
                String allocRef = String.format("AL-%s-%d", pickList.getPickNumber(), item.getProduct().getId());
                InventoryAllocation allocation = InventoryAllocation.builder()
                        .company(pickList.getCompany())
                        .salesOrder(pickList.getSalesOrder())
                        .salesOrderItem(item.getSalesOrderItem())
                        .pickList(pickList)
                        .product(item.getProduct())
                        .warehouse(pickList.getWarehouse())
                        .store(pickList.getStore())
                        .allocatedQuantity(toAllocate)
                        .allocationStatus(AllocationStatus.ACTIVE)
                        .allocationReference(allocRef)
                        .build();
                inventoryAllocationRepository.save(allocation);

                // Write stock movement
                createStockMovement(item.getProduct(), pickList.getWarehouse(), pickList.getStore(),
                        toAllocate, pickList, "SALES_RESERVE", currentUser);
            }
        }

        pickList.setStatus(PickListStatus.RELEASED);
        pickList.setReleasedBy(currentUser);
        pickList.setReleasedAt(LocalDateTime.now());

        PickList saved = pickListRepository.save(pickList);

        // Publish View Refresh Event
        applicationEventPublisher.publishEvent(new PickListRefreshEvent(this));

        return pickListMapper.toResponse(saved);
    }

    /**
     * Performs the startPicking operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the PickListResponse result
     */
    @Override
    @Transactional
    public PickListResponse startPicking(Long id) {
        PickList pickList = pickListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pick list not found with ID: " + id));

        if (pickList.getStatus() != PickListStatus.RELEASED) {
            throw new BusinessException("Pick list must be in RELEASED status to start picking");
        }

        pickList.setStatus(PickListStatus.PICKING);
        PickList saved = pickListRepository.save(pickList);
        return pickListMapper.toResponse(saved);
    }

    /**
     * Completes the picking workflow and finalizes the record status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the PickListResponse result
     */
    @Override
    @Transactional
    public PickListResponse completePicking(Long id, CompletePickingRequest request) {
        PickList pickList = pickListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pick list not found with ID: " + id));

        if (pickList.getStatus() != PickListStatus.PICKING) {
            throw new BusinessException("Pick list must be in PICKING status to complete picking");
        }

        Map<Long, BigDecimal> pickingUpdates = request.items().stream()
                .collect(Collectors.toMap(
                        CompletePickingRequest.ItemPickingUpdate::productId,
                        CompletePickingRequest.ItemPickingUpdate::pickedQuantity
                ));

        for (PickListItem item : pickList.getItems()) {
            BigDecimal pickedQty = pickingUpdates.getOrDefault(item.getProduct().getId(), BigDecimal.ZERO);

            if (pickedQty.compareTo(item.getAllocatedQuantity()) > 0) {
                throw new BusinessException("Picked quantity (" + pickedQty + ") cannot exceed allocated quantity (" +
                        item.getAllocatedQuantity() + ") for product ID: " + item.getProduct().getId());
            }

            item.setPickedQuantity(pickedQty);
        }

        pickList.setStatus(PickListStatus.PICKED);
        pickList.setPickedBy(getCurrentUser());
        pickList.setPickedAt(LocalDateTime.now());

        PickList saved = pickListRepository.save(pickList);
        return pickListMapper.toResponse(saved);
    }

    /**
     * Performs the packPickList operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the PickListResponse result
     */
    @Override
    @Transactional
    public PickListResponse packPickList(Long id) {
        PickList pickList = pickListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pick list not found with ID: " + id));

        if (pickList.getStatus() != PickListStatus.PICKED) {
            throw new BusinessException("Pick list must be in PICKED status to pack");
        }

        pickList.setStatus(PickListStatus.PACKED);
        pickList.setPackedBy(getCurrentUser());
        pickList.setPackedAt(LocalDateTime.now());

        PickList saved = pickListRepository.save(pickList);
        return pickListMapper.toResponse(saved);
    }

    /**
     * Performs the shipPickList operation in this module.
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the PickListResponse result
     */
    @Override
    @Transactional
    public PickListResponse shipPickList(Long id, ShipRequest request) {
        PickList pickList = pickListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pick list not found with ID: " + id));

        if (pickList.getStatus() != PickListStatus.PACKED) {
            throw new BusinessException("Pick list must be in PACKED status to ship");
        }

        User currentUser = getCurrentUser();
        Map<Long, BigDecimal> shipmentUpdates = request.items().stream()
                .collect(Collectors.toMap(
                        ShipRequest.ItemShipmentUpdate::productId,
                        ShipRequest.ItemShipmentUpdate::shippedQuantity
                ));

        List<InventoryAllocation> allocations = inventoryAllocationRepository.findByPickListId(pickList.getId());

        for (PickListItem item : pickList.getItems()) {
            BigDecimal shippedQty = shipmentUpdates.getOrDefault(item.getProduct().getId(), BigDecimal.ZERO);

            if (shippedQty.compareTo(item.getPickedQuantity()) > 0) {
                throw new BusinessException("Shipped quantity (" + shippedQty + ") cannot exceed picked quantity (" +
                        item.getPickedQuantity() + ") for product ID: " + item.getProduct().getId());
            }

            BigDecimal allocatedQty = item.getAllocatedQuantity();
            BigDecimal remainingQty = allocatedQty.subtract(shippedQty); // released reservation

            // Pessimistic Write Lock on InventoryStock
            Optional<InventoryStock> stockOpt;
            if (pickList.getWarehouse() != null) {
                stockOpt = inventoryStockRepository.findWithPessimisticWriteByProductIdAndWarehouseId(
                        item.getProduct().getId(), pickList.getWarehouse().getId()
                );
            } else {
                stockOpt = inventoryStockRepository.findWithPessimisticWriteByProductIdAndStoreId(
                        item.getProduct().getId(), pickList.getStore().getId()
                );
            }

            InventoryStock stock = stockOpt.orElseThrow(() -> new BusinessException(
                    "Inventory stock record not found for product ID: " + item.getProduct().getId()
            ));

            // Decrement physical stock by shippedQty
            stock.setQuantity(stock.getQuantity().subtract(shippedQty));
            // Decrement reserved stock by entire allocatedQty (shippedQty is consumed, remainingQty is released)
            stock.setReservedQuantity(stock.getReservedQuantity().subtract(allocatedQty));
            inventoryStockRepository.save(stock);

            // Update item
            item.setShippedQuantity(shippedQty);

            // Update sales order item quantities
            SalesOrderItem soItem = item.getSalesOrderItem();
            // Active allocation goes down by the whole allocatedQty
            soItem.setAllocatedQuantity(soItem.getAllocatedQuantity().subtract(allocatedQty));
            // Fulfilled increases by shippedQty
            soItem.setFulfilledQuantity(soItem.getFulfilledQuantity().add(shippedQty));
            salesOrderItemRepository.save(soItem);

            // Update allocations status to CONSUMED/RELEASED
            for (InventoryAllocation allocation : allocations) {
                if (allocation.getProduct().getId().equals(item.getProduct().getId()) &&
                    allocation.getAllocationStatus() == AllocationStatus.ACTIVE) {
                    
                    // Consume the shipped part
                    allocation.setAllocatedQuantity(shippedQty);
                    allocation.setAllocationStatus(AllocationStatus.CONSUMED);
                    inventoryAllocationRepository.save(allocation);

                    // If there is remaining unfulfilled allocation, create a RELEASED record for audit trail
                    if (remainingQty.compareTo(BigDecimal.ZERO) > 0) {
                        String relRef = String.format("AL-REL-%s-%d", pickList.getPickNumber(), item.getProduct().getId());
                        InventoryAllocation releasedAllocation = InventoryAllocation.builder()
                                .company(pickList.getCompany())
                                .salesOrder(pickList.getSalesOrder())
                                .salesOrderItem(item.getSalesOrderItem())
                                .pickList(pickList)
                                .product(item.getProduct())
                                .warehouse(pickList.getWarehouse())
                                .store(pickList.getStore())
                                .allocatedQuantity(remainingQty)
                                .allocationStatus(AllocationStatus.RELEASED)
                                .allocationReference(relRef)
                                .build();
                        inventoryAllocationRepository.save(releasedAllocation);
                    }
                }
            }

            // Write SALES_SHIPMENT movement
            if (shippedQty.compareTo(BigDecimal.ZERO) > 0) {
                createStockMovement(item.getProduct(), pickList.getWarehouse(), pickList.getStore(),
                        shippedQty, pickList, "SALES_SHIPMENT", currentUser);
            }

            // Write SALES_RELEASE movement if some allocation was released
            if (remainingQty.compareTo(BigDecimal.ZERO) > 0) {
                createStockMovement(item.getProduct(), pickList.getWarehouse(), pickList.getStore(),
                        remainingQty, pickList, "SALES_RELEASE", currentUser);
            }
        }

        pickList.setStatus(PickListStatus.SHIPPED);
        pickList.setShippedBy(currentUser);
        pickList.setShippedAt(LocalDateTime.now());

        PickList saved = pickListRepository.save(pickList);

        // Progress Sales Order Status
        updateSalesOrderStatus(pickList.getSalesOrder());

        // Publish View Refresh Event
        applicationEventPublisher.publishEvent(new PickListRefreshEvent(this));

        return pickListMapper.toResponse(saved);
    }

    /**
     * Cancels the pick list and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param reason the reason input value
     * @return the PickListResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PickListResponse cancelPickList(Long id, String reason) {
        PickList pickList = pickListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pick list not found with ID: " + id));

        if (pickList.getStatus() == PickListStatus.SHIPPED || pickList.getStatus() == PickListStatus.CANCELLED) {
            throw new BusinessException("Cannot cancel a shipped or already cancelled pick list");
        }

        User currentUser = getCurrentUser();
        PickListStatus originalStatus = pickList.getStatus();

        // Release allocations if the pick list was released/picking/picked/packed
        if (originalStatus != PickListStatus.DRAFT) {
            for (PickListItem item : pickList.getItems()) {
                BigDecimal allocatedQty = item.getAllocatedQuantity();
                if (allocatedQty.compareTo(BigDecimal.ZERO) > 0) {
                    // Pessimistic Write Lock on InventoryStock
                    Optional<InventoryStock> stockOpt;
                    if (pickList.getWarehouse() != null) {
                        stockOpt = inventoryStockRepository.findWithPessimisticWriteByProductIdAndWarehouseId(
                                item.getProduct().getId(), pickList.getWarehouse().getId()
                        );
                    } else {
                        stockOpt = inventoryStockRepository.findWithPessimisticWriteByProductIdAndStoreId(
                                item.getProduct().getId(), pickList.getStore().getId()
                        );
                    }

                    InventoryStock stock = stockOpt.orElseThrow(() -> new BusinessException(
                            "Inventory stock record not found for product ID: " + item.getProduct().getId()
                    ));

                    // Decrement reservedQuantity to restore available stock
                    stock.setReservedQuantity(stock.getReservedQuantity().subtract(allocatedQty));
                    inventoryStockRepository.save(stock);

                    // Decrement SalesOrderItem allocated quantity
                    SalesOrderItem soItem = item.getSalesOrderItem();
                    soItem.setAllocatedQuantity(soItem.getAllocatedQuantity().subtract(allocatedQty));
                    salesOrderItemRepository.save(soItem);

                    // Write SALES_RELEASE movement
                    createStockMovement(item.getProduct(), pickList.getWarehouse(), pickList.getStore(),
                            allocatedQty, pickList, "SALES_RELEASE", currentUser);
                }
            }

            // Update all active allocations to CANCELLED
            List<InventoryAllocation> allocations = inventoryAllocationRepository.findByPickListId(pickList.getId());
            for (InventoryAllocation allocation : allocations) {
                if (allocation.getAllocationStatus() == AllocationStatus.ACTIVE) {
                    allocation.setAllocationStatus(AllocationStatus.CANCELLED);
                    inventoryAllocationRepository.save(allocation);
                }
            }
        }

        pickList.setStatus(PickListStatus.CANCELLED);
        pickList.setCancelledBy(currentUser);
        pickList.setCancelledAt(LocalDateTime.now());
        pickList.setCancellationReason(reason);

        PickList saved = pickListRepository.save(pickList);

        // Publish View Refresh Event
        applicationEventPublisher.publishEvent(new PickListRefreshEvent(this));

        return pickListMapper.toResponse(saved);
    }

    /**
     * Retrieves a paginated list of pick lists by sales order id records.
     *
     * @param salesOrderId the salesOrderId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a paginated list of pick lists by sales order id records.
     *
     * @param salesOrderId the salesOrderId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<PickListResponse> getPickListsBySalesOrderId(Long salesOrderId) {
        return pickListMapper.toResponseList(pickListRepository.findBySalesOrderId(salesOrderId));
    }

    /**
     * Retrieves a paginated list of pick lists by warehouse id records.
     *
     * @param warehouseId the warehouseId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a paginated list of pick lists by warehouse id records.
     *
     * @param warehouseId the warehouseId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<PickListResponse> getPickListsByWarehouseId(Long warehouseId) {
        return pickListMapper.toResponseList(pickListRepository.findByWarehouseId(warehouseId));
    }

    /**
     * Retrieves a paginated list of pick lists by store id records.
     *
     * @param storeId the storeId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a paginated list of pick lists by store id records.
     *
     * @param storeId the storeId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<PickListResponse> getPickListsByStoreId(Long storeId) {
        return pickListMapper.toResponseList(pickListRepository.findByStoreId(storeId));
    }

    /**
     * Retrieves a paginated list of pick lists by status records.
     *
     * @param status status filter for narrowing query results
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a paginated list of pick lists by status records.
     *
     * @param status status filter for narrowing query results
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<PickListResponse> getPickListsByStatus(PickListStatus status) {
        return pickListMapper.toResponseList(pickListRepository.findByStatus(status));
    }

    private void updateSalesOrderStatus(SalesOrder salesOrder) {
        boolean allFulfilled = true;
        boolean anyFulfilled = false;

        for (SalesOrderItem item : salesOrder.getItems()) {
            if (item.getFulfilledQuantity().compareTo(item.getOrderedQuantity()) < 0) {
                allFulfilled = false;
            }
            if (item.getFulfilledQuantity().compareTo(BigDecimal.ZERO) > 0) {
                anyFulfilled = true;
            }
        }

        if (allFulfilled) {
            salesOrder.setStatus(SalesOrderStatus.FULFILLED);
        } else if (anyFulfilled) {
            salesOrder.setStatus(SalesOrderStatus.PARTIALLY_FULFILLED);
        } else {
            salesOrder.setStatus(SalesOrderStatus.APPROVED);
        }
        salesOrderRepository.save(salesOrder);
    }

    private void createStockMovement(Product product, Warehouse warehouse, Store store, BigDecimal quantity,
                                     PickList pickList, String movementType, User user) {
        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setWarehouse(warehouse);
        movement.setStore(store);
        movement.setMovementType(movementType);
        movement.setQuantity(quantity);
        movement.setReferenceNo(pickList.getPickNumber());
        movement.setReferenceType(StockMovementReferenceType.PICK_LIST);
        movement.setReferenceId(pickList.getId());
        movement.setReferenceNumber(pickList.getPickNumber());
        movement.setCreatedBy(user);
        stockMovementRepository.save(movement);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new BusinessException("User is not authenticated");
        }
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found with email: " + email));
    }
}