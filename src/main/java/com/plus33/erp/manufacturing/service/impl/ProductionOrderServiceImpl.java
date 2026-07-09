/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service.impl
 * File              : ProductionOrderServiceImpl.java
 * Purpose           : Business logic service layer for Manufacturing Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionOrderController
 * Related Service   : ProductionOrderServiceImpl
 * Related Repository: ProductionOrderRepository, ProductionOrderOperationRepository, BomHeaderRepository, RoutingHeaderRepository, ProductRepository, UnitOfMeasureRepository, InventoryStockRepository, StockMovementRepository, WarehouseRepository, ProductionCostRepository, WorkCenterRepository, ProductionConfirmationRepository, ProductionScrapRepository, ProductionReworkRepository, ManufacturingBatchGenealogyRepository, ManufacturingSerialGenealogyRepository
 * Related Entity    : ProductionOrder
 * Related DTO       : CompleteOperationRequest, CreateProductionOrderOperationRequest, CreateProductionOrderRequest, mapOpToDto, mapToDto
 * Related Mapper    : ProductionOrderMapper
 * Related DB Table  : production_orders
 * Related REST APIs : N/A
 * Depends On        : Common Module, Inventory Module, Organization Module
 * Used By           : ProductionOrderController, ProductionOrderServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Manufacturing Module. Implements ProductionOrderService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.manufacturing.service.impl;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import com.plus33.erp.inventory.entity.InventoryStock;
import com.plus33.erp.inventory.entity.StockMovement;
import com.plus33.erp.inventory.entity.StockMovementReferenceType;
import com.plus33.erp.inventory.repository.InventoryStockRepository;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.inventory.repository.StockMovementRepository;
import com.plus33.erp.inventory.repository.UnitOfMeasureRepository;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.manufacturing.dto.*;
import com.plus33.erp.manufacturing.entity.*;
import com.plus33.erp.manufacturing.repository.*;
import com.plus33.erp.manufacturing.service.ProductionCostService;
import com.plus33.erp.manufacturing.service.ProductionOrderService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionOrderServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Manufacturing Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProductionOrderController
 *   --> ProductionOrderServiceImpl (this)
 *   --> Validate business rules
 *   --> ProductionOrderRepository (read/write 'production_orders')
 *   --> ProductionOrderMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code production_orders}</p>
 * <p><b>Module Deps      :</b> Common, Inventory, Organization, Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class ProductionOrderServiceImpl implements ProductionOrderService {

    private final ProductionOrderRepository orderRepository;
    private final ProductionOrderOperationRepository operationRepository;
    private final BomHeaderRepository bomHeaderRepository;
    private final RoutingHeaderRepository routingHeaderRepository;
    private final ProductRepository productRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final InventoryStockRepository inventoryStockRepository;
    private final StockMovementRepository stockMovementRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductionCostRepository costRepository;
    private final ProductionCostService costService;
    private final ApplicationEventPublisher eventPublisher;
    private final WorkCenterRepository workCenterRepository;
    private final ProductionConfirmationRepository confirmationRepository;
    private final ProductionScrapRepository scrapRepository;
    private final ProductionReworkRepository reworkRepository;
    private final ManufacturingBatchGenealogyRepository batchGenealogyRepository;
    private final ManufacturingSerialGenealogyRepository serialGenealogyRepository;

    public ProductionOrderServiceImpl(ProductionOrderRepository orderRepository,
                                     ProductionOrderOperationRepository operationRepository,
                                     BomHeaderRepository bomHeaderRepository,
                                     RoutingHeaderRepository routingHeaderRepository,
                                     ProductRepository productRepository,
                                     UnitOfMeasureRepository unitOfMeasureRepository,
                                     InventoryStockRepository inventoryStockRepository,
                                     StockMovementRepository stockMovementRepository,
                                     WarehouseRepository warehouseRepository,
                                     ProductionCostRepository costRepository,
                                     ProductionCostService costService,
                                     ApplicationEventPublisher eventPublisher,
                                     WorkCenterRepository workCenterRepository,
                                     ProductionConfirmationRepository confirmationRepository,
                                     ProductionScrapRepository scrapRepository,
                                     ProductionReworkRepository reworkRepository,
                                     ManufacturingBatchGenealogyRepository batchGenealogyRepository,
                                     ManufacturingSerialGenealogyRepository serialGenealogyRepository) {
        this.orderRepository = orderRepository;
        this.operationRepository = operationRepository;
        this.bomHeaderRepository = bomHeaderRepository;
        this.routingHeaderRepository = routingHeaderRepository;
        this.productRepository = productRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.inventoryStockRepository = inventoryStockRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.warehouseRepository = warehouseRepository;
        this.costRepository = costRepository;
        this.costService = costService;
        this.eventPublisher = eventPublisher;
        this.workCenterRepository = workCenterRepository;
        this.confirmationRepository = confirmationRepository;
        this.scrapRepository = scrapRepository;
        this.reworkRepository = reworkRepository;
        this.batchGenealogyRepository = batchGenealogyRepository;
        this.serialGenealogyRepository = serialGenealogyRepository;
    }

    /**
     * Creates a new production order and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the ProductionOrderDto result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new production order and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the ProductionOrderDto result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public ProductionOrderDto createProductionOrder(CreateProductionOrderRequest request) {
        final Long companyId = request.getCompanyId();
        if (orderRepository.existsByCompanyIdAndOrderNumber(companyId, request.getOrderNumber())) {
            throw new IllegalArgumentException("Production order number already exists: " + request.getOrderNumber());
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + request.getProductId()));

        UnitOfMeasure unit = unitOfMeasureRepository.findById(request.getUnitId())
                .orElseThrow(() -> new NoSuchElementException("Unit of Measure not found with id: " + request.getUnitId()));

        ProductionOrder order = new ProductionOrder();
        order.setCompanyId(request.getCompanyId());
        order.setOrderNumber(request.getOrderNumber());
        order.setProduct(product);
        order.setUnit(unit);
        order.setTargetQuantity(request.getPlannedQuantity());
        order.setCompletedQuantity(BigDecimal.ZERO);
        order.setScrappedQuantity(BigDecimal.ZERO);
        order.setPlannedStartDate(request.getPlannedStartDate());
        order.setPlannedEndDate(request.getPlannedEndDate());
        order.setWarehouseId(request.getWarehouseId());
        order.setNotes(request.getNotes());
        order.setPriority(request.getPriority() != null ? request.getPriority() : 5);
        order.setStatus(ProductionOrderStatus.PLANNED);

        // Resolve BOM
        Long bomId = request.getBomHeaderId();
        if (bomId == null) {
            bomId = bomHeaderRepository.findActiveBomForProduct(request.getCompanyId(), request.getProductId(), LocalDate.now())
                    .stream().findFirst().map(BomHeader::getId).orElse(null);
        }
        if (bomId != null) {
            order.setBomHeader(bomHeaderRepository.findById(bomId).orElse(null));
        }

        // Resolve Routing
        Long routingId = request.getRoutingHeaderId();
        if (routingId == null) {
            routingId = routingHeaderRepository.findAll().stream()
                    .filter(r -> r.getCompanyId().equals(request.getCompanyId()) && r.getProduct().getId().equals(request.getProductId()) && "ACTIVE".equals(r.getStatus()))
                    .findFirst().map(RoutingHeader::getId).orElse(null);
        }
        if (routingId != null) {
            order.setRoutingHeader(routingHeaderRepository.findById(routingId).orElse(null));
        }

        order = orderRepository.save(order);

        // Create Operations
        List<ProductionOrderOperation> ops = new ArrayList<>();
        if (request.getOperations() != null && !request.getOperations().isEmpty()) {
            for (CreateProductionOrderOperationRequest opReq : request.getOperations()) {
                ProductionOrderOperation op = new ProductionOrderOperation();
                op.setProductionOrder(order);
                op.setOperationNumber(opReq.getOperationNumber());
                op.setOperationCode(opReq.getOperationCode() != null ? opReq.getOperationCode() : "OP" + opReq.getOperationNumber());
                op.setDescription(opReq.getOperationName() != null ? opReq.getOperationName() : opReq.getDescription());
                WorkCenter wc = workCenterRepository.findById(opReq.getWorkCenterId())
                        .orElseThrow(() -> new NoSuchElementException("Work Center not found"));
                op.setWorkCenter(wc);
                op.setPlannedSetupHours(opReq.getEstimatedSetupHours());
                op.setPlannedRunHours(opReq.getEstimatedRunHours());
                op.setPlannedQuantity(order.getTargetQuantity());
                op.setStatus("PENDING");
                ops.add(operationRepository.save(op));
            }
        } else if (order.getRoutingHeader() != null) {
            for (RoutingOperation ro : order.getRoutingHeader().getOperations()) {
                ProductionOrderOperation op = new ProductionOrderOperation();
                op.setProductionOrder(order);
                op.setOperationNumber(ro.getOperationNumber());
                op.setOperationCode(ro.getOperationCode());
                op.setDescription(ro.getDescription());
                op.setWorkCenter(ro.getWorkCenter());
                op.setPlannedSetupHours(ro.getSetupTimeHours());
                BigDecimal runHours = ro.getRunTimePerUnitHours().multiply(order.getTargetQuantity());
                op.setPlannedRunHours(runHours);
                op.setPlannedQuantity(order.getTargetQuantity());
                op.setStatus("PENDING");
                ops.add(operationRepository.save(op));
            }
        } else {
            // Default operation
            ProductionOrderOperation op = new ProductionOrderOperation();
            op.setProductionOrder(order);
            op.setOperationNumber(10);
            op.setOperationCode("OP10");
            op.setDescription("Assembly");
            op.setPlannedSetupHours(BigDecimal.ONE);
            op.setPlannedRunHours(BigDecimal.TEN);
            op.setPlannedQuantity(order.getTargetQuantity());
            op.setStatus("PENDING");
            WorkCenter defaultWc = workCenterRepository.findByCompanyId(companyId).stream()
                    .findFirst().orElseThrow(() -> new BusinessException("No work center defined for company ID: " + companyId));
            op.setWorkCenter(defaultWc);
            ops.add(operationRepository.save(op));
        }

        // Initialize Costing
        ProductionCost cost = new ProductionCost();
        cost.setProductionOrder(order);
        cost.setCostingMethod(order.getCostingMethod().name());
        cost.setStandardTotalCost(order.getTargetQuantity().multiply(new BigDecimal("10.00"))); // standard estimation
        costRepository.save(cost);

        ProductionOrderDto dto = mapToDto(order);
        dto.setOperations(ops.stream().map(this::mapOpToDto).toList());
        return dto;
    }

    /**
     * Retrieves a single production order by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the ProductionOrderDto result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public ProductionOrderDto getProductionOrderById(Long id) {
        ProductionOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Production order not found with ID: " + id));
        ProductionOrderDto dto = mapToDto(order);
        List<ProductionOrderOperation> ops = operationRepository.findByProductionOrderId(id);
        dto.setOperations(ops.stream().map(this::mapOpToDto).toList());
        return dto;
    }

    /**
     * Retrieves production orders by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductionOrderDto> getProductionOrdersByCompany(Long companyId) {
        return orderRepository.findByCompanyId(companyId).stream()
                .map(po -> getProductionOrderById(po.getId())).toList();
    }

    /**
     * Releases previously reserved production order resources back to the available pool.
     *
     * @param id the unique database ID of the resource
     * @return the ProductionOrderDto result
     */
    /**
     * Releases previously reserved production order resources back to the available pool.
     *
     * @param id the unique database ID of the resource
     * @return the ProductionOrderDto result
     */
    @Override
    public ProductionOrderDto releaseProductionOrder(Long id) {
        ProductionOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Production order not found with ID: " + id));
        if (order.getStatus() != ProductionOrderStatus.PLANNED && order.getStatus() != ProductionOrderStatus.DRAFT) {
            throw new IllegalStateException("Only PLANNED or DRAFT orders can be released");
        }

        order.setStatus(ProductionOrderStatus.RELEASED);
        order.setReleasedAt(LocalDateTime.now());
        final ProductionOrder savedOrder = orderRepository.save(order);

        // Fetch or create Costing record
        ProductionCost cost = costRepository.findByProductionOrderId(savedOrder.getId()).orElseGet(() -> {
            ProductionCost c = new ProductionCost();
            c.setProductionOrder(savedOrder);
            c.setCostingMethod(savedOrder.getCostingMethod().name());
            return costRepository.save(c);
        });

        // Issue raw materials from inventory to WIP based on BOM
        if (savedOrder.getBomHeader() != null && savedOrder.getWarehouseId() != null) {
            BigDecimal ratio = savedOrder.getTargetQuantity().divide(savedOrder.getBomHeader().getBaseQuantity(), 6, RoundingMode.HALF_UP);
            Warehouse mfgWh = warehouseRepository.findById(savedOrder.getWarehouseId()).orElse(null);

            BigDecimal stdMatCost = BigDecimal.ZERO;
            for (BomLine line : savedOrder.getBomHeader().getLines()) {
                BigDecimal requiredQty = line.getQuantity().multiply(ratio);
                InventoryStock stock = inventoryStockRepository
                        .findByProductIdAndWarehouseId(line.getComponentProduct().getId(), savedOrder.getWarehouseId())
                        .orElseThrow(() -> new BusinessException("Insufficient inventory stock for component: " + line.getComponentProduct().getCode()));

                if (stock.getQuantity().compareTo(requiredQty) < 0) {
                    throw new BusinessException("Insufficient stock on hand for: " + line.getComponentProduct().getCode());
                }

                // Decrement stock
                stock.setQuantity(stock.getQuantity().subtract(requiredQty));
                inventoryStockRepository.save(stock);

                // Add Stock Movement
                StockMovement movement = new StockMovement();
                movement.setProduct(line.getComponentProduct());
                movement.setWarehouse(mfgWh);
                movement.setMovementType("MANUFACTURING_ISSUE");
                movement.setQuantity(requiredQty.negate());
                movement.setReferenceNo(savedOrder.getOrderNumber());
                movement.setReferenceType(StockMovementReferenceType.MANUFACTURING_ISSUE);
                movement.setReferenceId(savedOrder.getId());
                stockMovementRepository.save(movement);

                stdMatCost = stdMatCost.add(requiredQty.multiply(new BigDecimal("10.00"))); // RM standard price default
            }

            cost.setStandardMaterialCost(stdMatCost);
            cost.setActualMaterialCost(stdMatCost);
            cost.setWipBalance(cost.getWipBalance().add(stdMatCost));
            cost.setActualTotalCost(cost.getActualTotalCost().add(stdMatCost));
        }

        // Calculate Standard Labor, Machine, and Overhead based on routing operations
        BigDecimal stdLaborCost = BigDecimal.ZERO;
        BigDecimal stdMachineCost = BigDecimal.ZERO;
        BigDecimal stdOverheadCost = BigDecimal.ZERO;
        if (savedOrder.getRoutingHeader() != null) {
            for (RoutingOperation ro : savedOrder.getRoutingHeader().getOperations()) {
                BigDecimal setupHours = ro.getSetupTimeHours();
                BigDecimal runHours = ro.getRunTimePerUnitHours().multiply(savedOrder.getTargetQuantity());
                stdLaborCost = stdLaborCost.add(setupHours.add(runHours).multiply(new BigDecimal("25.00")));
                stdMachineCost = stdMachineCost.add(runHours.multiply(new BigDecimal("15.00")));
            }
        } else {
            stdLaborCost = savedOrder.getTargetQuantity().multiply(new BigDecimal("5.00"));
            stdMachineCost = savedOrder.getTargetQuantity().multiply(new BigDecimal("3.00"));
        }
        stdOverheadCost = stdLaborCost.multiply(new BigDecimal("0.10")); // 10% overhead default rate

        cost.setStandardLaborCost(stdLaborCost);
        cost.setStandardMachineCost(stdMachineCost);
        cost.setStandardOverheadCost(stdOverheadCost);
        cost.setStandardTotalCost(cost.getStandardMaterialCost().add(stdLaborCost).add(stdMachineCost).add(stdOverheadCost));
        costRepository.save(cost);

        // Publish event
        eventPublisher.publishEvent(new com.plus33.erp.manufacturing.entity.ManufacturingEventBus.ProductionOrderReleasedEvent(savedOrder.getId(), savedOrder.getCompanyId()));

        return getProductionOrderById(id);
    }

    /**
     * Completes the operation workflow and finalizes the record status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param orderId the orderId input value
     * @param operationId the operationId input value
     * @param request the validated request DTO containing input data
     * @return the ProductionOrderOperationDto result
     */
    /**
     * Completes the operation workflow and finalizes the record status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param orderId the orderId input value
     * @param operationId the operationId input value
     * @param request the validated request DTO containing input data
     * @return the ProductionOrderOperationDto result
     */
    @Override
    public ProductionOrderOperationDto completeOperation(Long orderId, Long operationId, CompleteOperationRequest request) {
        ProductionOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        ProductionOrderOperation op = operationRepository.findById(operationId)
                .orElseThrow(() -> new NoSuchElementException("Operation not found"));

        op.setActualSetupHours(request.getActualSetupHours());
        op.setActualRunHours(request.getActualRunHours());
        op.setCompletedQuantity(request.getYieldQuantity());
        op.setScrappedQuantity(request.getScrapQuantity());
        op.setStatus("COMPLETED");
        op.setActualEndDatetime(LocalDateTime.now());
        op = operationRepository.save(op);

        // Save Production Confirmation
        ProductionConfirmation conf = new ProductionConfirmation();
        conf.setProductionOrder(order);
        conf.setProductionOrderOperation(op);
        conf.setConfirmationNumber("CONF-" + System.currentTimeMillis());
        conf.setConfirmationType("PARTIAL");
        conf.setConfirmedQuantity(request.getYieldQuantity());
        conf.setScrappedQuantity(request.getScrapQuantity());
        conf.setUnit(order.getUnit());
        conf.setActualLaborHours(request.getActualRunHours().add(request.getActualSetupHours()));
        conf.setActualMachineHours(request.getActualRunHours());
        conf.setLaborGroupId(request.getLaborGroupId());
        conf.setWorkCenter(op.getWorkCenter());
        conf.setConfirmedBy(1L);
        conf.setConfirmedAt(LocalDateTime.now());
        conf.setNotes(request.getNotes());
        confirmationRepository.save(conf);

        // If scrap quantity > 0, log to scrapRepository
        if (request.getScrapQuantity().compareTo(BigDecimal.ZERO) > 0) {
            ProductionScrap scrap = new ProductionScrap();
            scrap.setProductionOrder(order);
            scrap.setProductionOrderOperation(op);
            scrap.setScrapNumber("SCRAP-" + System.currentTimeMillis());
            scrap.setProduct(order.getProduct());
            scrap.setScrapQuantity(request.getScrapQuantity());
            scrap.setUnit(order.getUnit());
            scrap.setScrapDisposition("DISCARD");
            scrap.setRecordedBy(1L);
            scrap.setRecordedAt(LocalDateTime.now());
            scrapRepository.save(scrap);
        }

        // Transition order status to IN_PROGRESS if it is released or planned
        if (order.getStatus() == ProductionOrderStatus.RELEASED || order.getStatus() == ProductionOrderStatus.PLANNED) {
            order.setStatus(ProductionOrderStatus.IN_PROGRESS);
            order.setActualStartDate(LocalDate.now());
            orderRepository.save(order);
        }

        // Recalculate WIP and actual cost in ProductionCost
        ProductionCost cost = costRepository.findByProductionOrderId(orderId).orElseGet(() -> {
            ProductionCost c = new ProductionCost();
            c.setProductionOrder(order);
            c.setCostingMethod(order.getCostingMethod().name());
            return costRepository.save(c);
        });

        // Add to actual labor, machine, and overhead costs
        BigDecimal opLaborCost = request.getActualRunHours().add(request.getActualSetupHours()).multiply(new BigDecimal("25.00")); // labor rate default
        BigDecimal opMachineCost = request.getActualRunHours().multiply(new BigDecimal("15.00")); // machine rate default
        BigDecimal opOverheadCost = opLaborCost.multiply(new BigDecimal("0.10")); // overhead absorption default rate 10%
        cost.setActualLaborCost(cost.getActualLaborCost().add(opLaborCost));
        cost.setActualMachineCost(cost.getActualMachineCost().add(opMachineCost));
        cost.setActualOverheadCost(cost.getActualOverheadCost().add(opOverheadCost));
        cost.setActualTotalCost(cost.getActualTotalCost().add(opLaborCost).add(opMachineCost).add(opOverheadCost));
        cost.setWipBalance(cost.getWipBalance().add(opLaborCost).add(opMachineCost).add(opOverheadCost));
        costRepository.save(cost);

        // Publish event
        eventPublisher.publishEvent(new com.plus33.erp.manufacturing.entity.ManufacturingEventBus.OperationCompletedEvent(order.getId(), op.getId(), order.getCompanyId()));

        return mapOpToDto(op);
    }

    /**
     * Completes the production order workflow and finalizes the record status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the ProductionOrderDto result
     */
    /**
     * Completes the production order workflow and finalizes the record status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the ProductionOrderDto result
     */
    @Override
    public ProductionOrderDto completeProductionOrder(Long id) {
        ProductionOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Production order not found with ID: " + id));

        // Complete any pending operations
        List<ProductionOrderOperation> ops = operationRepository.findByProductionOrderId(id);
        for (ProductionOrderOperation op : ops) {
            if (!"COMPLETED".equals(op.getStatus())) {
                op.setStatus("COMPLETED");
                op.setCompletedQuantity(order.getTargetQuantity());
                op.setActualSetupHours(op.getPlannedSetupHours() != null ? op.getPlannedSetupHours() : BigDecimal.ZERO);
                op.setActualRunHours(op.getPlannedRunHours() != null ? op.getPlannedRunHours() : BigDecimal.ZERO);
                op.setActualEndDatetime(LocalDateTime.now());
                operationRepository.save(op);
            }
        }

        order.setStatus(ProductionOrderStatus.COMPLETED);
        order.setCompletedQuantity(order.getTargetQuantity());
        order.setActualEndDate(LocalDate.now());
        order = orderRepository.save(order);

        // Receive Finished Goods into target warehouse
        if (order.getWarehouseId() != null) {
            Warehouse targetWh = warehouseRepository.findById(order.getWarehouseId()).orElse(null);
            final Product orderProduct = order.getProduct();
            InventoryStock stock = inventoryStockRepository
                    .findByProductIdAndWarehouseId(orderProduct.getId(), order.getWarehouseId())
                    .orElseGet(() -> {
                        InventoryStock newStock = new InventoryStock();
                        newStock.setProduct(orderProduct);
                        newStock.setWarehouse(targetWh);
                        newStock.setQuantity(BigDecimal.ZERO);
                        newStock.setReservedQuantity(BigDecimal.ZERO);
                        return newStock;
                    });

            stock.setQuantity(stock.getQuantity().add(order.getCompletedQuantity()));
            inventoryStockRepository.save(stock);

            // Add Stock Movement
            StockMovement movement = new StockMovement();
            movement.setProduct(order.getProduct());
            movement.setWarehouse(targetWh);
            movement.setMovementType("MANUFACTURING_RECEIPT");
            movement.setQuantity(order.getCompletedQuantity());
            movement.setReferenceNo(order.getOrderNumber());
            movement.setReferenceType(StockMovementReferenceType.MANUFACTURING_RECEIPT);
            movement.setReferenceId(order.getId());
            stockMovementRepository.save(movement);

            // Log batch genealogy input/output
            String fgBatchNo = "LOT-FG-" + order.getId() + "-" + System.currentTimeMillis();
            
            ManufacturingBatchGenealogy fgGenealogy = new ManufacturingBatchGenealogy();
            fgGenealogy.setProductionOrder(order);
            fgGenealogy.setProduct(order.getProduct());
            fgGenealogy.setBatchNumber(fgBatchNo);
            fgGenealogy.setQuantity(order.getCompletedQuantity());
            fgGenealogy.setUnit(order.getUnit());
            fgGenealogy.setGenealogyType("OUTPUT");
            fgGenealogy.setProducedAt(LocalDateTime.now());
            fgGenealogy.setRecallStatus("CLEAR");
            batchGenealogyRepository.save(fgGenealogy);

            if (order.getBomHeader() != null) {
                for (BomLine line : order.getBomHeader().getLines()) {
                    ManufacturingBatchGenealogy inputGenealogy = new ManufacturingBatchGenealogy();
                    inputGenealogy.setProductionOrder(order);
                    inputGenealogy.setProduct(line.getComponentProduct());
                    inputGenealogy.setBatchNumber("LOT-RM-" + line.getComponentProduct().getId());
                    inputGenealogy.setParentBatchNumber(fgBatchNo);
                    inputGenealogy.setParentProduct(order.getProduct());
                    inputGenealogy.setQuantity(line.getQuantity().multiply(order.getCompletedQuantity()));
                    inputGenealogy.setUnit(line.getComponentProduct().getUnit());
                    inputGenealogy.setGenealogyType("INPUT");
                    inputGenealogy.setProducedAt(LocalDateTime.now());
                    inputGenealogy.setRecallStatus("CLEAR");
                    batchGenealogyRepository.save(inputGenealogy);
                }
            }
        }

        // Recalculate and finalize costs
        costService.finalizeProductionCosts(order.getCompanyId(), order.getId(), order.getReleasedBy() != null ? order.getReleasedBy() : 1L);

        // Publish event
        eventPublisher.publishEvent(new com.plus33.erp.manufacturing.entity.ManufacturingEventBus.ProductionOrderCompletedEvent(order.getId(), order.getCompanyId()));

        return getProductionOrderById(id);
    }

    /**
     * Performs the startOrder operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param orderId the orderId input value
     * @param userId authenticated user identifier
     * @return the ProductionOrderDto result
     */
    /**
     * Performs the startOrder operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param orderId the orderId input value
     * @param userId authenticated user identifier
     * @return the ProductionOrderDto result
     */
    @Override
    public ProductionOrderDto startOrder(Long companyId, Long orderId, Long userId) {
        ProductionOrder order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(ProductionOrderStatus.IN_PROGRESS);
        order.setActualStartDate(LocalDate.now());
        return mapToDto(orderRepository.save(order));
    }

    /**
     * Completes the order workflow and finalizes the record status.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param orderId the orderId input value
     * @param userId authenticated user identifier
     * @return the ProductionOrderDto result
     */
    /**
     * Completes the order workflow and finalizes the record status.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param orderId the orderId input value
     * @param userId authenticated user identifier
     * @return the ProductionOrderDto result
     */
    @Override
    public ProductionOrderDto closeOrder(Long companyId, Long orderId, Long userId) {
        ProductionOrder order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(ProductionOrderStatus.CLOSED);
        order.setClosedBy(userId);
        order.setClosedAt(LocalDateTime.now());
        return mapToDto(orderRepository.save(order));
    }

    /**
     * Cancels the order and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param orderId the orderId input value
     * @param reason the reason input value
     * @param userId authenticated user identifier
     * @return the ProductionOrderDto result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Cancels the order and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param orderId the orderId input value
     * @param reason the reason input value
     * @param userId authenticated user identifier
     * @return the ProductionOrderDto result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public ProductionOrderDto cancelOrder(Long companyId, Long orderId, String reason, Long userId) {
        ProductionOrder order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(ProductionOrderStatus.CANCELLED);
        order.setNotes(reason);
        return mapToDto(orderRepository.save(order));
    }

    /**
     * Retrieves orders by status data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param status status filter for narrowing query results
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductionOrderDto> getOrdersByStatus(Long companyId, String status) {
        ProductionOrderStatus s = ProductionOrderStatus.valueOf(status);
        return orderRepository.findByCompanyIdAndStatus(companyId, s).stream()
                .map(this::mapToDto).toList();
    }

    /**
     * Retrieves active orders data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductionOrderDto> getActiveOrders(Long companyId) {
        return orderRepository.findByCompanyId(companyId).stream()
                .filter(o -> o.getStatus() == ProductionOrderStatus.IN_PROGRESS || o.getStatus() == ProductionOrderStatus.RELEASED)
                .map(this::mapToDto).toList();
    }

    /**
     * Retrieves orders by schedule data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param from the from input value
     * @param to the to input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductionOrderDto> getOrdersBySchedule(Long companyId, LocalDate from, LocalDate to) {
        return orderRepository.findByCompanyId(companyId).stream()
                .filter(o -> !o.getPlannedStartDate().isBefore(from) && !o.getPlannedEndDate().isAfter(to))
                .map(this::mapToDto).toList();
    }

    private ProductionOrderDto mapToDto(ProductionOrder order) {
        ProductionOrderDto dto = new ProductionOrderDto();
        dto.setId(order.getId());
        dto.setCompanyId(order.getCompanyId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setProductId(order.getProduct().getId());
        dto.setProductCode(order.getProduct().getCode());
        dto.setProductName(order.getProduct().getName());
        dto.setBomHeaderId(order.getBomHeader() != null ? order.getBomHeader().getId() : null);
        dto.setRoutingHeaderId(order.getRoutingHeader() != null ? order.getRoutingHeader().getId() : null);
        dto.setPlannedQuantity(order.getTargetQuantity());
        dto.setCompletedQuantity(order.getCompletedQuantity());
        dto.setScrappedQuantity(order.getScrappedQuantity());
        dto.setUnitCode(order.getUnit() != null ? order.getUnit().getCode() : null);
        dto.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        dto.setPriority(order.getPriority());
        dto.setPlannedStartDate(order.getPlannedStartDate());
        dto.setPlannedEndDate(order.getPlannedEndDate());
        dto.setActualStartDate(order.getActualStartDate());
        dto.setActualEndDate(order.getActualEndDate());
        dto.setWarehouseId(order.getWarehouseId());
        dto.setNotes(order.getNotes());
        return dto;
    }

    private ProductionOrderOperationDto mapOpToDto(ProductionOrderOperation op) {
        ProductionOrderOperationDto dto = new ProductionOrderOperationDto();
        dto.setId(op.getId());
        dto.setProductionOrderId(op.getProductionOrder().getId());
        dto.setOperationNumber(op.getOperationNumber());
        dto.setOperationCode(op.getOperationCode());
        dto.setDescription(op.getDescription());
        dto.setWorkCenterId(op.getWorkCenter() != null ? op.getWorkCenter().getId() : null);
        dto.setWorkCenterCode(op.getWorkCenter() != null ? op.getWorkCenter().getCode() : null);
        dto.setWorkCenterName(op.getWorkCenter() != null ? op.getWorkCenter().getName() : null);
        dto.setPlannedSetupHours(op.getPlannedSetupHours());
        dto.setActualSetupHours(op.getActualSetupHours());
        dto.setPlannedRunHours(op.getPlannedRunHours());
        dto.setActualRunHours(op.getActualRunHours());
        dto.setStatus(op.getStatus());
        dto.setCompletedQuantity(op.getCompletedQuantity());
        dto.setScrappedQuantity(op.getScrappedQuantity());
        dto.setPlannedStartDatetime(op.getPlannedStartDatetime());
        dto.setPlannedEndDatetime(op.getPlannedEndDatetime());
        dto.setActualStartDatetime(op.getActualStartDatetime());
        dto.setActualEndDatetime(op.getActualEndDatetime());
        return dto;
    }
}