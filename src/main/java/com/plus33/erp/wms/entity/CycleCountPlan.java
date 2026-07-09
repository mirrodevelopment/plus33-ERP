/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : CycleCountPlan.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CycleCountPlanController
 * Related Service   : CycleCountPlanService, CycleCountPlanServiceImpl
 * Related Repository: CycleCountPlanRepository
 * Related Entity    : CycleCountPlan
 * Related DTO       : N/A
 * Related Mapper    : CycleCountPlanMapper
 * Related DB Table  : cycle_count_plans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CycleCountPlanRepository, CycleCountPlanMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'cycle_count_plans'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cycle_count_plans",
       uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "plan_number"}))
/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code CycleCountPlan}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'cycle_count_plans'.</p>
 *
 * <p><b>Database Table   :</b> {@code cycle_count_plans}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class CycleCountPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "plan_number", nullable = false, length = 50)
    private String planNumber;

    /** ABC, HIGH_VALUE, RANDOM, FULL_COUNT, BLIND, ZONE_BASED */
    @Column(name = "plan_type", nullable = false, length = 30)
    private String planType = "ABC";

    @Column(name = "zone_id")
    private Long zoneId;

    @Column(name = "abc_class", length = 5)
    private String abcClass;

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";
    // DRAFT, SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /** In blind count mode, system quantities are hidden from the counter */
    @Column(name = "blind_count", nullable = false)
    private boolean blindCount = false;

    /** Variance percentage threshold below which results are auto-approved */
    @Column(name = "auto_approve_below_variance", nullable = false, precision = 5, scale = 2)
    private BigDecimal autoApproveBelowVariance = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

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
     * Retrieves plan number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPlanNumber() { return planNumber; }
    /**
     * Performs the setPlanNumber operation in this module.
     *
     * @param planNumber the planNumber input value
     */
    public void setPlanNumber(String planNumber) { this.planNumber = planNumber; }
    /**
     * Retrieves plan type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPlanType() { return planType; }
    /**
     * Performs the setPlanType operation in this module.
     *
     * @param planType the planType input value
     */
    public void setPlanType(String planType) { this.planType = planType; }
    /**
     * Retrieves zone id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getZoneId() { return zoneId; }
    /**
     * Performs the setZoneId operation in this module.
     *
     * @param zoneId the zoneId input value
     */
    public void setZoneId(Long zoneId) { this.zoneId = zoneId; }
    /**
     * Retrieves abc class data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAbcClass() { return abcClass; }
    /**
     * Performs the setAbcClass operation in this module.
     *
     * @param abcClass the abcClass input value
     */
    public void setAbcClass(String abcClass) { this.abcClass = abcClass; }
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
     * Retrieves scheduled date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getScheduledDate() { return scheduledDate; }
    /**
     * Performs the setScheduledDate operation in this module.
     *
     * @param scheduledDate the scheduledDate input value
     */
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
    /**
     * Retrieves started at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartedAt() { return startedAt; }
    /**
     * Performs the setStartedAt operation in this module.
     *
     * @param startedAt the startedAt input value
     */
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
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
     * Performs the isBlindCount operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isBlindCount() { return blindCount; }
    /**
     * Performs the setBlindCount operation in this module.
     *
     * @param blindCount the blindCount input value
     */
    public void setBlindCount(boolean blindCount) { this.blindCount = blindCount; }
    /**
     * Retrieves auto approve below variance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAutoApproveBelowVariance() { return autoApproveBelowVariance; }
    /**
     * Performs the setAutoApproveBelowVariance operation in this module.
     *
     * @param autoApproveBelowVariance the autoApproveBelowVariance input value
     */
    public void setAutoApproveBelowVariance(BigDecimal autoApproveBelowVariance) { this.autoApproveBelowVariance = autoApproveBelowVariance; }
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
     * Retrieves created by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCreatedBy() { return createdBy; }
    /**
     * Performs the setCreatedBy operation in this module.
     *
     * @param createdBy the createdBy input value
     */
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    /**
     * Retrieves approved by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getApprovedBy() { return approvedBy; }
    /**
     * Performs the setApprovedBy operation in this module.
     *
     * @param approvedBy the approvedBy input value
     */
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
    /**
     * Retrieves approved at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getApprovedAt() { return approvedAt; }
    /**
     * Performs the setApprovedAt operation in this module.
     *
     * @param approvedAt the approvedAt input value
     */
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
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