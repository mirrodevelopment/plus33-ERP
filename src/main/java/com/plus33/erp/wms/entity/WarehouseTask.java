/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : WarehouseTask.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseTaskController
 * Related Service   : WarehouseTaskService, WarehouseTaskServiceImpl
 * Related Repository: WarehouseTaskRepository
 * Related Entity    : WarehouseTask
 * Related DTO       : N/A
 * Related Mapper    : WarehouseTaskMapper
 * Related DB Table  : warehouse_tasks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseTaskRepository, WarehouseTaskMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'warehouse_tasks'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseTask}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'warehouse_tasks'.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_tasks}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "warehouse_tasks")
public class WarehouseTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    /**
     * PUT_AWAY, PICKING, PACKING, LOADING, CYCLE_COUNT, TRANSFER, REPLENISHMENT, RECEIVING
     */
    @Column(name = "task_type", nullable = false, length = 30)
    private String taskType;

    /**
     * PENDING, ASSIGNED, IN_PROGRESS, PAUSED, COMPLETED, CANCELLED
     */
    @Column(name = "task_status", nullable = false, length = 30)
    private String taskStatus = "PENDING";

    @Column(nullable = false)
    private int priority = 5;

    /** e.g. PUT_AWAY_WORK, PICKING_WORK, CYCLE_COUNT_TASK */
    @Column(name = "ref_type", length = 30)
    private String refType;

    @Column(name = "ref_id")
    private Long refId;

    @Column(name = "assigned_to")
    private Long assignedTo;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "paused_at")
    private LocalDateTime pausedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by")
    private Long createdBy;

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
     * Retrieves task type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTaskType() { return taskType; }
    /**
     * Performs the setTaskType operation in this module.
     *
     * @param taskType the taskType input value
     */
    public void setTaskType(String taskType) { this.taskType = taskType; }
    /**
     * Retrieves task status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTaskStatus() { return taskStatus; }
    /**
     * Performs the setTaskStatus operation in this module.
     *
     * @param taskStatus the taskStatus input value
     */
    public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(int priority) { this.priority = priority; }
    /**
     * Retrieves ref type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRefType() { return refType; }
    /**
     * Performs the setRefType operation in this module.
     *
     * @param refType the refType input value
     */
    public void setRefType(String refType) { this.refType = refType; }
    /**
     * Retrieves ref id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRefId() { return refId; }
    /**
     * Performs the setRefId operation in this module.
     *
     * @param refId the refId input value
     */
    public void setRefId(Long refId) { this.refId = refId; }
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
     * Retrieves assigned at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAssignedAt() { return assignedAt; }
    /**
     * Performs the setAssignedAt operation in this module.
     *
     * @param assignedAt the assignedAt input value
     */
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
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
     * Retrieves paused at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getPausedAt() { return pausedAt; }
    /**
     * Performs the setPausedAt operation in this module.
     *
     * @param pausedAt the pausedAt input value
     */
    public void setPausedAt(LocalDateTime pausedAt) { this.pausedAt = pausedAt; }
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
     * Retrieves cancelled at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    /**
     * Performs the setCancelledAt operation in this module.
     *
     * @param cancelledAt the cancelledAt input value
     */
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
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