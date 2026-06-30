package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "planned_orders")
public class MrpPlannedOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mrp_run_id", nullable = false)
    private MrpRun mrpRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "order_type", nullable = false, length = 30)
    private String orderType; // PRODUCTION, PURCHASE, TRANSFER

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PlannedOrderStatus status = PlannedOrderStatus.OPEN;

    @Column(name = "required_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal requiredQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(name = "required_date", nullable = false)
    private LocalDate requiredDate;

    @Column(name = "planned_start_date", nullable = false)
    private LocalDate plannedStartDate;

    @Column(name = "planned_end_date", nullable = false)
    private LocalDate plannedEndDate;

    @Column(name = "demand_source", length = 50)
    private String demandSource; // SALES_ORDER, FORECAST, SAFETY_STOCK, DEPENDENT

    @Column(name = "demand_source_id")
    private Long demandSourceId;

    @Column(name = "bom_header_id")
    private Long bomHeaderId;

    @Column(name = "routing_header_id")
    private Long routingHeaderId;

    @Column(nullable = false)
    private Boolean firmed = false;

    @Column(name = "released_production_order_id")
    private Long releasedProductionOrderId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public MrpPlannedOrder() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public MrpRun getMrpRun() { return mrpRun; }
    public void setMrpRun(MrpRun mrpRun) { this.mrpRun = mrpRun; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public PlannedOrderStatus getStatus() { return status; }
    public void setStatus(PlannedOrderStatus status) { this.status = status; }
    public BigDecimal getRequiredQuantity() { return requiredQuantity; }
    public void setRequiredQuantity(BigDecimal requiredQuantity) { this.requiredQuantity = requiredQuantity; }
    public UnitOfMeasure getUnit() { return unit; }
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    public LocalDate getRequiredDate() { return requiredDate; }
    public void setRequiredDate(LocalDate requiredDate) { this.requiredDate = requiredDate; }
    public LocalDate getPlannedStartDate() { return plannedStartDate; }
    public void setPlannedStartDate(LocalDate plannedStartDate) { this.plannedStartDate = plannedStartDate; }
    public LocalDate getPlannedEndDate() { return plannedEndDate; }
    public void setPlannedEndDate(LocalDate plannedEndDate) { this.plannedEndDate = plannedEndDate; }
    public String getDemandSource() { return demandSource; }
    public void setDemandSource(String demandSource) { this.demandSource = demandSource; }
    public Long getDemandSourceId() { return demandSourceId; }
    public void setDemandSourceId(Long demandSourceId) { this.demandSourceId = demandSourceId; }
    public Long getBomHeaderId() { return bomHeaderId; }
    public void setBomHeaderId(Long bomHeaderId) { this.bomHeaderId = bomHeaderId; }
    public Long getRoutingHeaderId() { return routingHeaderId; }
    public void setRoutingHeaderId(Long routingHeaderId) { this.routingHeaderId = routingHeaderId; }
    public Boolean getFirmed() { return firmed; }
    public void setFirmed(Boolean firmed) { this.firmed = firmed; }
    public Long getReleasedProductionOrderId() { return releasedProductionOrderId; }
    public void setReleasedProductionOrderId(Long releasedProductionOrderId) { this.releasedProductionOrderId = releasedProductionOrderId; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Getter/setter aliases to prevent compile breaks
    public BigDecimal getQuantity() { return requiredQuantity; }
    public void setQuantity(BigDecimal quantity) { this.requiredQuantity = quantity; }
    public LocalDate getSuggestedStartDate() { return plannedStartDate; }
    public void setSuggestedStartDate(LocalDate suggestedStartDate) { this.plannedStartDate = suggestedStartDate; }
    public LocalDate getSuggestedDueDate() { return plannedEndDate; }
    public void setSuggestedDueDate(LocalDate suggestedDueDate) { this.plannedEndDate = suggestedDueDate; }
    public String getSourceType() { return demandSource; }
    public void setSourceType(String sourceType) { this.demandSource = sourceType; }
    public Long getSourceReferenceId() { return demandSourceId; }
    public void setSourceReferenceId(Long sourceReferenceId) { this.demandSourceId = sourceReferenceId; }
}
