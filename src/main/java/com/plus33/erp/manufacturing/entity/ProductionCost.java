/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ProductionCost.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionCostController
 * Related Service   : ProductionCostService, ProductionCostServiceImpl
 * Related Repository: ProductionCostRepository
 * Related Entity    : ProductionCost
 * Related DTO       : N/A
 * Related Mapper    : ProductionCostMapper
 * Related DB Table  : production_costs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionCostRepository, ProductionCostMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'production_costs'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionCost}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'production_costs'.</p>
 *
 * <p><b>Database Table   :</b> {@code production_costs}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public ProductionCost() {}

    // Computed helper: recalculate variances based on current actual vs standard
    /**
     * Performs the recalculateVariances operation in this module.
     *
     */
    public void recalculateVariances() {
        this.materialVariance = this.actualMaterialCost.subtract(this.standardMaterialCost);
        this.laborVariance = this.actualLaborCost.subtract(this.standardLaborCost);
        this.machineVariance = this.actualMachineCost.subtract(this.standardMachineCost);
        this.overheadVariance = this.actualOverheadCost.subtract(this.standardOverheadCost);
        this.totalVariance = this.actualTotalCost.subtract(this.standardTotalCost);
    }

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
     * Retrieves production order data from the database.
     *
     * @return the ProductionOrder result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ProductionOrder getProductionOrder() { return productionOrder; }
    /**
     * Performs the setProductionOrder operation in this module.
     *
     * @param productionOrder the productionOrder input value
     */
    public void setProductionOrder(ProductionOrder productionOrder) { this.productionOrder = productionOrder; }
    /**
     * Retrieves costing method data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCostingMethod() { return costingMethod; }
    /**
     * Performs the setCostingMethod operation in this module.
     *
     * @param costingMethod the costingMethod input value
     */
    public void setCostingMethod(String costingMethod) { this.costingMethod = costingMethod; }
    /**
     * Retrieves standard material cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardMaterialCost() { return standardMaterialCost; }
    /**
     * Performs the setStandardMaterialCost operation in this module.
     *
     * @param v the v input value
     */
    public void setStandardMaterialCost(BigDecimal v) { this.standardMaterialCost = v; }
    /**
     * Retrieves standard labor cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardLaborCost() { return standardLaborCost; }
    /**
     * Performs the setStandardLaborCost operation in this module.
     *
     * @param v the v input value
     */
    public void setStandardLaborCost(BigDecimal v) { this.standardLaborCost = v; }
    /**
     * Retrieves standard machine cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardMachineCost() { return standardMachineCost; }
    /**
     * Performs the setStandardMachineCost operation in this module.
     *
     * @param v the v input value
     */
    public void setStandardMachineCost(BigDecimal v) { this.standardMachineCost = v; }
    /**
     * Retrieves standard overhead cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardOverheadCost() { return standardOverheadCost; }
    /**
     * Performs the setStandardOverheadCost operation in this module.
     *
     * @param v the v input value
     */
    public void setStandardOverheadCost(BigDecimal v) { this.standardOverheadCost = v; }
    /**
     * Retrieves standard subcontract cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardSubcontractCost() { return standardSubcontractCost; }
    /**
     * Performs the setStandardSubcontractCost operation in this module.
     *
     * @param v the v input value
     */
    public void setStandardSubcontractCost(BigDecimal v) { this.standardSubcontractCost = v; }
    /**
     * Retrieves standard total cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStandardTotalCost() { return standardTotalCost; }
    /**
     * Performs the setStandardTotalCost operation in this module.
     *
     * @param v the v input value
     */
    public void setStandardTotalCost(BigDecimal v) { this.standardTotalCost = v; }
    /**
     * Retrieves actual material cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualMaterialCost() { return actualMaterialCost; }
    /**
     * Performs the setActualMaterialCost operation in this module.
     *
     * @param v the v input value
     */
    public void setActualMaterialCost(BigDecimal v) { this.actualMaterialCost = v; }
    /**
     * Retrieves a paginated list of actual labor cost records.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualLaborCost() { return actualLaborCost; }
    /**
     * Performs the setActualLaborCost operation in this module.
     *
     * @param v the v input value
     */
    public void setActualLaborCost(BigDecimal v) { this.actualLaborCost = v; }
    /**
     * Retrieves actual machine cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualMachineCost() { return actualMachineCost; }
    /**
     * Performs the setActualMachineCost operation in this module.
     *
     * @param v the v input value
     */
    public void setActualMachineCost(BigDecimal v) { this.actualMachineCost = v; }
    /**
     * Retrieves actual overhead cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualOverheadCost() { return actualOverheadCost; }
    /**
     * Performs the setActualOverheadCost operation in this module.
     *
     * @param v the v input value
     */
    public void setActualOverheadCost(BigDecimal v) { this.actualOverheadCost = v; }
    /**
     * Retrieves actual subcontract cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualSubcontractCost() { return actualSubcontractCost; }
    /**
     * Performs the setActualSubcontractCost operation in this module.
     *
     * @param v the v input value
     */
    public void setActualSubcontractCost(BigDecimal v) { this.actualSubcontractCost = v; }
    /**
     * Retrieves actual total cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualTotalCost() { return actualTotalCost; }
    /**
     * Performs the setActualTotalCost operation in this module.
     *
     * @param v the v input value
     */
    public void setActualTotalCost(BigDecimal v) { this.actualTotalCost = v; }
    /**
     * Retrieves byproduct credit data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getByproductCredit() { return byproductCredit; }
    /**
     * Performs the setByproductCredit operation in this module.
     *
     * @param v the v input value
     */
    public void setByproductCredit(BigDecimal v) { this.byproductCredit = v; }
    /**
     * Retrieves a paginated list of coproduct allocation records.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCoproductAllocation() { return coproductAllocation; }
    /**
     * Performs the setCoproductAllocation operation in this module.
     *
     * @param v the v input value
     */
    public void setCoproductAllocation(BigDecimal v) { this.coproductAllocation = v; }
    /**
     * Retrieves material variance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaterialVariance() { return materialVariance; }
    /**
     * Performs the setMaterialVariance operation in this module.
     *
     * @param v the v input value
     */
    public void setMaterialVariance(BigDecimal v) { this.materialVariance = v; }
    /**
     * Retrieves labor variance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLaborVariance() { return laborVariance; }
    /**
     * Performs the setLaborVariance operation in this module.
     *
     * @param v the v input value
     */
    public void setLaborVariance(BigDecimal v) { this.laborVariance = v; }
    /**
     * Retrieves machine variance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMachineVariance() { return machineVariance; }
    /**
     * Performs the setMachineVariance operation in this module.
     *
     * @param v the v input value
     */
    public void setMachineVariance(BigDecimal v) { this.machineVariance = v; }
    /**
     * Retrieves overhead variance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getOverheadVariance() { return overheadVariance; }
    /**
     * Performs the setOverheadVariance operation in this module.
     *
     * @param v the v input value
     */
    public void setOverheadVariance(BigDecimal v) { this.overheadVariance = v; }
    /**
     * Retrieves total variance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalVariance() { return totalVariance; }
    /**
     * Performs the setTotalVariance operation in this module.
     *
     * @param v the v input value
     */
    public void setTotalVariance(BigDecimal v) { this.totalVariance = v; }
    /**
     * Retrieves wip balance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getWipBalance() { return wipBalance; }
    /**
     * Performs the setWipBalance operation in this module.
     *
     * @param wipBalance the wipBalance input value
     */
    public void setWipBalance(BigDecimal wipBalance) { this.wipBalance = wipBalance; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Retrieves finalized at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getFinalizedAt() { return finalizedAt; }
    /**
     * Performs the setFinalizedAt operation in this module.
     *
     * @param finalizedAt the finalizedAt input value
     */
    public void setFinalizedAt(LocalDateTime finalizedAt) { this.finalizedAt = finalizedAt; }
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