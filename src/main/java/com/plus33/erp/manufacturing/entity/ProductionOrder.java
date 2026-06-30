package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public BomHeader getBomHeader() { return bomHeader; }
    public void setBomHeader(BomHeader bomHeader) { this.bomHeader = bomHeader; }
    public RoutingHeader getRoutingHeader() { return routingHeader; }
    public void setRoutingHeader(RoutingHeader routingHeader) { this.routingHeader = routingHeader; }
    public Long getPlannedOrderId() { return plannedOrderId; }
    public void setPlannedOrderId(Long plannedOrderId) { this.plannedOrderId = plannedOrderId; }
    public ProductionOrderStatus getStatus() { return status; }
    public void setStatus(ProductionOrderStatus status) { this.status = status; }
    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public UnitOfMeasure getUnit() { return unit; }
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    public BigDecimal getTargetQuantity() { return targetQuantity; }
    public void setTargetQuantity(BigDecimal targetQuantity) { this.targetQuantity = targetQuantity; }
    public BigDecimal getCompletedQuantity() { return completedQuantity; }
    public void setCompletedQuantity(BigDecimal completedQuantity) { this.completedQuantity = completedQuantity; }
    public BigDecimal getScrappedQuantity() { return scrappedQuantity; }
    public void setScrappedQuantity(BigDecimal scrappedQuantity) { this.scrappedQuantity = scrappedQuantity; }
    public LocalDate getPlannedStartDate() { return plannedStartDate; }
    public void setPlannedStartDate(LocalDate plannedStartDate) { this.plannedStartDate = plannedStartDate; }
    public LocalDate getPlannedEndDate() { return plannedEndDate; }
    public void setPlannedEndDate(LocalDate plannedEndDate) { this.plannedEndDate = plannedEndDate; }
    public LocalDate getActualStartDate() { return actualStartDate; }
    public void setActualStartDate(LocalDate actualStartDate) { this.actualStartDate = actualStartDate; }
    public LocalDate getActualEndDate() { return actualEndDate; }
    public void setActualEndDate(LocalDate actualEndDate) { this.actualEndDate = actualEndDate; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public BigDecimal getStandardCost() { return standardCost; }
    public void setStandardCost(BigDecimal standardCost) { this.standardCost = standardCost; }
    public BigDecimal getActualCost() { return actualCost; }
    public void setActualCost(BigDecimal actualCost) { this.actualCost = actualCost; }
    public CostingMethod getCostingMethod() { return costingMethod; }
    public void setCostingMethod(CostingMethod costingMethod) { this.costingMethod = costingMethod; }
    public Long getReleasedBy() { return releasedBy; }
    public void setReleasedBy(Long releasedBy) { this.releasedBy = releasedBy; }
    public LocalDateTime getReleasedAt() { return releasedAt; }
    public void setReleasedAt(LocalDateTime releasedAt) { this.releasedAt = releasedAt; }
    public Long getClosedBy() { return closedBy; }
    public void setClosedBy(Long closedBy) { this.closedBy = closedBy; }
    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
