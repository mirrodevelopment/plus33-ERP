/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : ReplenishmentTask.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentTaskController
 * Related Service   : ReplenishmentTaskService, ReplenishmentTaskServiceImpl
 * Related Repository: ReplenishmentTaskRepository
 * Related Entity    : ReplenishmentTask
 * Related DTO       : N/A
 * Related Mapper    : ReplenishmentTaskMapper
 * Related DB Table  : replenishment_tasks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReplenishmentTaskRepository, ReplenishmentTaskMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'replenishment_tasks'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentTask}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'replenishment_tasks'.</p>
 *
 * <p><b>Database Table   :</b> {@code replenishment_tasks}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "replenishment_tasks")
public class ReplenishmentTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replenishment_plan_id")
    private ReplenishmentPlan replenishmentPlan;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location_id")
    private WarehouseLocation fromLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id")
    private WarehouseLocation toLocation;

    @Column(name = "lot_number", length = 50)
    private String lotNumber;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal quantity;

    @Column(name = "moved_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal movedQuantity = BigDecimal.ZERO;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(name = "trigger_reason", length = 100)
    private String triggerReason;

    /** PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED */
    @Column(nullable = false, length = 30)
    private String status = "PENDING";

    @Column(name = "assigned_to")
    private Long assignedTo;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

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

    // Getters and setters
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
     * Retrieves replenishment plan data from the database.
     *
     * @return the ReplenishmentPlan result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ReplenishmentPlan getReplenishmentPlan() { return replenishmentPlan; }
    /**
     * Performs the setReplenishmentPlan operation in this module.
     *
     * @param replenishmentPlan the replenishmentPlan input value
     */
    public void setReplenishmentPlan(ReplenishmentPlan replenishmentPlan) { this.replenishmentPlan = replenishmentPlan; }
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
     * Retrieves product id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProductId() { return productId; }
    /**
     * Performs the setProductId operation in this module.
     *
     * @param productId the productId input value
     */
    public void setProductId(Long productId) { this.productId = productId; }
    /**
     * Retrieves from location data from the database.
     *
     * @return the WarehouseLocation result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public WarehouseLocation getFromLocation() { return fromLocation; }
    /**
     * Performs the setFromLocation operation in this module.
     *
     * @param fromLocation the fromLocation input value
     */
    public void setFromLocation(WarehouseLocation fromLocation) { this.fromLocation = fromLocation; }
    /**
     * Retrieves to location data from the database.
     *
     * @return the WarehouseLocation result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public WarehouseLocation getToLocation() { return toLocation; }
    /**
     * Performs the setToLocation operation in this module.
     *
     * @param toLocation the toLocation input value
     */
    public void setToLocation(WarehouseLocation toLocation) { this.toLocation = toLocation; }
    /**
     * Retrieves lot number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLotNumber() { return lotNumber; }
    /**
     * Performs the setLotNumber operation in this module.
     *
     * @param lotNumber the lotNumber input value
     */
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    /**
     * Retrieves quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getQuantity() { return quantity; }
    /**
     * Performs the setQuantity operation in this module.
     *
     * @param quantity the quantity input value
     */
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    /**
     * Retrieves moved quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMovedQuantity() { return movedQuantity; }
    /**
     * Performs the setMovedQuantity operation in this module.
     *
     * @param movedQuantity the movedQuantity input value
     */
    public void setMovedQuantity(BigDecimal movedQuantity) { this.movedQuantity = movedQuantity; }
    /**
     * Retrieves unit id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getUnitId() { return unitId; }
    /**
     * Performs the setUnitId operation in this module.
     *
     * @param unitId the unitId input value
     */
    public void setUnitId(Long unitId) { this.unitId = unitId; }
    /**
     * Retrieves trigger reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTriggerReason() { return triggerReason; }
    /**
     * Performs the setTriggerReason operation in this module.
     *
     * @param triggerReason the triggerReason input value
     */
    public void setTriggerReason(String triggerReason) { this.triggerReason = triggerReason; }
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
     * Retrieves assigned to data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getAssignedTo() { return assignedTo; }
    /**
     * Performs the setAssignedTo operation in this module.
     *
     * @param assignedTo the assignedTo input value
     */
    public void setAssignedTo(Long assignedTo) { this.assignedTo = assignedTo; }
    /**
     * Retrieves completed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletedAt() { return completedAt; }
    /**
     * Performs the setCompletedAt operation in this module.
     *
     * @param completedAt the completedAt input value
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
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
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}