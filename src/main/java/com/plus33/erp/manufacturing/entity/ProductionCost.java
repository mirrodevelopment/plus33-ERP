package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_costs")
public class ProductionCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id", nullable = false, unique = true)
    private ProductionOrder productionOrder;

    @Column(name = "costing_method", nullable = false, length = 30)
    private String costingMethod = "STANDARD";

    // --- Standard Costs ---
    @Column(name = "standard_material_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal standardMaterialCost = BigDecimal.ZERO;

    @Column(name = "standard_labor_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal standardLaborCost = BigDecimal.ZERO;

    @Column(name = "standard_machine_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal standardMachineCost = BigDecimal.ZERO;

    @Column(name = "standard_overhead_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal standardOverheadCost = BigDecimal.ZERO;

    @Column(name = "standard_subcontract_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal standardSubcontractCost = BigDecimal.ZERO;

    @Column(name = "standard_total_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal standardTotalCost = BigDecimal.ZERO;

    // --- Actual Costs ---
    @Column(name = "actual_material_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal actualMaterialCost = BigDecimal.ZERO;

    @Column(name = "actual_labor_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal actualLaborCost = BigDecimal.ZERO;

    @Column(name = "actual_machine_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal actualMachineCost = BigDecimal.ZERO;

    @Column(name = "actual_overhead_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal actualOverheadCost = BigDecimal.ZERO;

    @Column(name = "actual_subcontract_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal actualSubcontractCost = BigDecimal.ZERO;

    @Column(name = "actual_total_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal actualTotalCost = BigDecimal.ZERO;

    // --- By-product / Co-product ---
    @Column(name = "byproduct_credit", nullable = false, precision = 22, scale = 6)
    private BigDecimal byproductCredit = BigDecimal.ZERO;

    @Column(name = "coproduct_allocation", nullable = false, precision = 22, scale = 6)
    private BigDecimal coproductAllocation = BigDecimal.ZERO;

    // --- Variances ---
    @Column(name = "material_variance", nullable = false, precision = 22, scale = 6)
    private BigDecimal materialVariance = BigDecimal.ZERO;

    @Column(name = "labor_variance", nullable = false, precision = 22, scale = 6)
    private BigDecimal laborVariance = BigDecimal.ZERO;

    @Column(name = "machine_variance", nullable = false, precision = 22, scale = 6)
    private BigDecimal machineVariance = BigDecimal.ZERO;

    @Column(name = "overhead_variance", nullable = false, precision = 22, scale = 6)
    private BigDecimal overheadVariance = BigDecimal.ZERO;

    @Column(name = "total_variance", nullable = false, precision = 22, scale = 6)
    private BigDecimal totalVariance = BigDecimal.ZERO;

    @Column(name = "wip_balance", nullable = false, precision = 22, scale = 6)
    private BigDecimal wipBalance = BigDecimal.ZERO;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "IN_PROGRESS"; // IN_PROGRESS, FINALIZED, REVERSED

    @Column(name = "finalized_at")
    private LocalDateTime finalizedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public ProductionCost() {}

    // Computed helper: recalculate variances based on current actual vs standard
    public void recalculateVariances() {
        this.materialVariance = this.actualMaterialCost.subtract(this.standardMaterialCost);
        this.laborVariance = this.actualLaborCost.subtract(this.standardLaborCost);
        this.machineVariance = this.actualMachineCost.subtract(this.standardMachineCost);
        this.overheadVariance = this.actualOverheadCost.subtract(this.standardOverheadCost);
        this.totalVariance = this.actualTotalCost.subtract(this.standardTotalCost);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductionOrder getProductionOrder() { return productionOrder; }
    public void setProductionOrder(ProductionOrder productionOrder) { this.productionOrder = productionOrder; }
    public String getCostingMethod() { return costingMethod; }
    public void setCostingMethod(String costingMethod) { this.costingMethod = costingMethod; }
    public BigDecimal getStandardMaterialCost() { return standardMaterialCost; }
    public void setStandardMaterialCost(BigDecimal v) { this.standardMaterialCost = v; }
    public BigDecimal getStandardLaborCost() { return standardLaborCost; }
    public void setStandardLaborCost(BigDecimal v) { this.standardLaborCost = v; }
    public BigDecimal getStandardMachineCost() { return standardMachineCost; }
    public void setStandardMachineCost(BigDecimal v) { this.standardMachineCost = v; }
    public BigDecimal getStandardOverheadCost() { return standardOverheadCost; }
    public void setStandardOverheadCost(BigDecimal v) { this.standardOverheadCost = v; }
    public BigDecimal getStandardSubcontractCost() { return standardSubcontractCost; }
    public void setStandardSubcontractCost(BigDecimal v) { this.standardSubcontractCost = v; }
    public BigDecimal getStandardTotalCost() { return standardTotalCost; }
    public void setStandardTotalCost(BigDecimal v) { this.standardTotalCost = v; }
    public BigDecimal getActualMaterialCost() { return actualMaterialCost; }
    public void setActualMaterialCost(BigDecimal v) { this.actualMaterialCost = v; }
    public BigDecimal getActualLaborCost() { return actualLaborCost; }
    public void setActualLaborCost(BigDecimal v) { this.actualLaborCost = v; }
    public BigDecimal getActualMachineCost() { return actualMachineCost; }
    public void setActualMachineCost(BigDecimal v) { this.actualMachineCost = v; }
    public BigDecimal getActualOverheadCost() { return actualOverheadCost; }
    public void setActualOverheadCost(BigDecimal v) { this.actualOverheadCost = v; }
    public BigDecimal getActualSubcontractCost() { return actualSubcontractCost; }
    public void setActualSubcontractCost(BigDecimal v) { this.actualSubcontractCost = v; }
    public BigDecimal getActualTotalCost() { return actualTotalCost; }
    public void setActualTotalCost(BigDecimal v) { this.actualTotalCost = v; }
    public BigDecimal getByproductCredit() { return byproductCredit; }
    public void setByproductCredit(BigDecimal v) { this.byproductCredit = v; }
    public BigDecimal getCoproductAllocation() { return coproductAllocation; }
    public void setCoproductAllocation(BigDecimal v) { this.coproductAllocation = v; }
    public BigDecimal getMaterialVariance() { return materialVariance; }
    public void setMaterialVariance(BigDecimal v) { this.materialVariance = v; }
    public BigDecimal getLaborVariance() { return laborVariance; }
    public void setLaborVariance(BigDecimal v) { this.laborVariance = v; }
    public BigDecimal getMachineVariance() { return machineVariance; }
    public void setMachineVariance(BigDecimal v) { this.machineVariance = v; }
    public BigDecimal getOverheadVariance() { return overheadVariance; }
    public void setOverheadVariance(BigDecimal v) { this.overheadVariance = v; }
    public BigDecimal getTotalVariance() { return totalVariance; }
    public void setTotalVariance(BigDecimal v) { this.totalVariance = v; }
    public BigDecimal getWipBalance() { return wipBalance; }
    public void setWipBalance(BigDecimal wipBalance) { this.wipBalance = wipBalance; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getFinalizedAt() { return finalizedAt; }
    public void setFinalizedAt(LocalDateTime finalizedAt) { this.finalizedAt = finalizedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
