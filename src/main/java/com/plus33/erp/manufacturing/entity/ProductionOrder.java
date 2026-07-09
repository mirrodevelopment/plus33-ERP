/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ProductionOrder.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionOrderController
 * Related Service   : ProductionOrderService, ProductionOrderServiceImpl
 * Related Repository: ProductionOrderRepository
 * Related Entity    : ProductionOrder
 * Related DTO       : N/A
 * Related Mapper    : ProductionOrderMapper
 * Related DB Table  : production_orders
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : ProductionOrderRepository, ProductionOrderMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'production_orders'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionOrder}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'production_orders'.</p>
 *
 * <p><b>Database Table   :</b> {@code production_orders}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "production_orders")
public class ProductionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "order_number", nullable = false, length = 50)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_header_id", nullable = false)
    private BomHeader bomHeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routing_header_id")
    private RoutingHeader routingHeader;

    @Column(name = "planned_order_id")
    private Long plannedOrderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProductionOrderStatus status = ProductionOrderStatus.DRAFT;

    @Column(name = "order_type", nullable = false, length = 30)
    private String orderType = "STANDARD"; // STANDARD, REWORK, OFF_CYCLE, CORRECTION

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(name = "target_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal targetQuantity;

    @Column(name = "completed_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal completedQuantity = BigDecimal.ZERO;

    @Column(name = "scrapped_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal scrappedQuantity = BigDecimal.ZERO;

    @Column(name = "planned_start_date", nullable = false)
    private LocalDate plannedStartDate;

    @Column(name = "planned_end_date", nullable = false)
    private LocalDate plannedEndDate;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @Column(nullable = false)
    private Integer priority = 5;

    @Column(name = "standard_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal standardCost = BigDecimal.ZERO;

    @Column(name = "actual_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal actualCost = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "costing_method", nullable = false, length = 30)
    private CostingMethod costingMethod = CostingMethod.STANDARD;

    @Column(name = "released_by")
    private Long releasedBy;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @Column(name = "closed_by")
    private Long closedBy;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Version
    private LocalDateTime updatedAt = LocalDateTime.now();

    public ProductionOrder() {}

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves order number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOrderNumber() { return orderNumber; }
    /**
     * Performs the setOrderNumber operation in this module.
     *
     * @param orderNumber the orderNumber input value
     */
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    /**
     * Retrieves product data from the database.
     *
     * @return the Product result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Product getProduct() { return product; }
    /**
     * Performs the setProduct operation in this module.
     *
     * @param product the product input value
     */
    public void setProduct(Product product) { this.product = product; }
    /**
     * Retrieves bom header data from the database.
     *
     * @return the BomHeader result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BomHeader getBomHeader() { return bomHeader; }
    /**
     * Performs the setBomHeader operation in this module.
     *
     * @param bomHeader the bomHeader input value
     */
    public void setBomHeader(BomHeader bomHeader) { this.bomHeader = bomHeader; }
    /**
     * Retrieves routing header data from the database.
     *
     * @return the RoutingHeader result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public RoutingHeader getRoutingHeader() { return routingHeader; }
    /**
     * Performs the setRoutingHeader operation in this module.
     *
     * @param routingHeader the routingHeader input value
     */
    public void setRoutingHeader(RoutingHeader routingHeader) { this.routingHeader = routingHeader; }
    /**
     * Retrieves planned order id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPlannedOrderId() { return plannedOrderId; }
    /**
     * Performs the setPlannedOrderId operation in this module.
     *
     * @param plannedOrderId the plannedOrderId input value
     */
    public void setPlannedOrderId(Long plannedOrderId) { this.plannedOrderId = plannedOrderId; }
    /**
     * Retrieves status data from the database.
     *
     * @return the ProductionOrderStatus result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ProductionOrderStatus getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(ProductionOrderStatus status) { this.status = status; }
    /**
     * Retrieves order type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOrderType() { return orderType; }
    /**
     * Performs the setOrderType operation in this module.
     *
     * @param orderType the orderType input value
     */
    public void setOrderType(String orderType) { this.orderType = orderType; }
    /**
     * Retrieves unit data from the database.
     *
     * @return the UnitOfMeasure result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public UnitOfMeasure getUnit() { return unit; }
    /**
     * Performs the setUnit operation in this module.
     *
     * @param unit the unit input value
     */
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    /**
     * Retrieves target quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTargetQuantity() { return targetQuantity; }
    /**
     * Performs the setTargetQuantity operation in this module.
     *
     * @param targetQuantity the targetQuantity input value
     */
    public void setTargetQuantity(BigDecimal targetQuantity) { this.targetQuantity = targetQuantity; }
    /**
     * Retrieves completed quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCompletedQuantity() { return completedQuantity; }
    /**
     * Performs the setCompletedQuantity operation in this module.
     *
     * @param completedQuantity the completedQuantity input value
     */
    public void setCompletedQuantity(BigDecimal completedQuantity) { this.completedQuantity = completedQuantity; }
    /**
     * Retrieves scrapped quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getScrappedQuantity() { return scrappedQuantity; }
    /**
     * Performs the setScrappedQuantity operation in this module.
     *
     * @param scrappedQuantity the scrappedQuantity input value
     */
    public void setScrappedQuantity(BigDecimal scrappedQuantity) { this.scrappedQuantity = scrappedQuantity; }
    /**
     * Retrieves planned start date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getPlannedStartDate() { return plannedStartDate; }
    /**
     * Performs the setPlannedStartDate operation in this module.
     *
     * @param plannedStartDate the plannedStartDate input value
     */
    public void setPlannedStartDate(LocalDate plannedStartDate) { this.plannedStartDate = plannedStartDate; }
    /**
     * Retrieves planned end date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getPlannedEndDate() { return plannedEndDate; }
    /**
     * Performs the setPlannedEndDate operation in this module.
     *
     * @param plannedEndDate the plannedEndDate input value
     */
    public void setPlannedEndDate(LocalDate plannedEndDate) { this.plannedEndDate = plannedEndDate; }
    /**
     * Retrieves actual start date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getActualStartDate() { return actualStartDate; }
    /**
     * Performs the setActualStartDate operation in this module.
     *
     * @param actualStartDate the actualStartDate input value
     */
    public void setActualStartDate(LocalDate actualStartDate) { this.actualStartDate = actualStartDate; }
    /**
     * Retrieves actual end date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getActualEndDate() { return actualEndDate; }
    /**
     * Performs the setActualEndDate operation in this module.
     *
     * @param actualEndDate the actualEndDate input value
     */
    public void setActualEndDate(LocalDate actualEndDate) { this.actualEndDate = actualEndDate; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(Integer priority) { this.priority = priority; }
    /**
     * Retrieves standard cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardCost() { return standardCost; }
    /**
     * Performs the setStandardCost operation in this module.
     *
     * @param standardCost the standardCost input value
     */
    public void setStandardCost(BigDecimal standardCost) { this.standardCost = standardCost; }
    /**
     * Retrieves actual cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualCost() { return actualCost; }
    /**
     * Performs the setActualCost operation in this module.
     *
     * @param actualCost the actualCost input value
     */
    public void setActualCost(BigDecimal actualCost) { this.actualCost = actualCost; }
    /**
     * Retrieves costing method data from the database.
     *
     * @return the CostingMethod result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public CostingMethod getCostingMethod() { return costingMethod; }
    /**
     * Performs the setCostingMethod operation in this module.
     *
     * @param costingMethod the costingMethod input value
     */
    public void setCostingMethod(CostingMethod costingMethod) { this.costingMethod = costingMethod; }
    /**
     * Retrieves released by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReleasedBy() { return releasedBy; }
    /**
     * Performs the setReleasedBy operation in this module.
     *
     * @param releasedBy the releasedBy input value
     */
    public void setReleasedBy(Long releasedBy) { this.releasedBy = releasedBy; }
    /**
     * Retrieves released at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getReleasedAt() { return releasedAt; }
    /**
     * Performs the setReleasedAt operation in this module.
     *
     * @param releasedAt the releasedAt input value
     */
    public void setReleasedAt(LocalDateTime releasedAt) { this.releasedAt = releasedAt; }
    /**
     * Retrieves closed by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getClosedBy() { return closedBy; }
    /**
     * Performs the setClosedBy operation in this module.
     *
     * @param closedBy the closedBy input value
     */
    public void setClosedBy(Long closedBy) { this.closedBy = closedBy; }
    /**
     * Retrieves closed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getClosedAt() { return closedAt; }
    /**
     * Performs the setClosedAt operation in this module.
     *
     * @param closedAt the closedAt input value
     */
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }
    /**
     * Retrieves warehouse id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWarehouseId() { return warehouseId; }
    /**
     * Performs the setWarehouseId operation in this module.
     *
     * @param warehouseId the warehouseId input value
     */
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    /**
     * Retrieves notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNotes() { return notes; }
    /**
     * Performs the setNotes operation in this module.
     *
     * @param notes the notes input value
     */
    public void setNotes(String notes) { this.notes = notes; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}