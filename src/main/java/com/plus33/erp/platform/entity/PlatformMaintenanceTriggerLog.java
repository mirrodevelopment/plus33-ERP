/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformMaintenanceTriggerLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformMaintenanceTriggerLogController
 * Related Service   : PlatformMaintenanceTriggerLogService, PlatformMaintenanceTriggerLogServiceImpl
 * Related Repository: PlatformMaintenanceTriggerLogRepository
 * Related Entity    : PlatformMaintenanceTriggerLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformMaintenanceTriggerLogMapper
 * Related DB Table  : platform_maintenance_trigger_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformMaintenanceTriggerLogRepository, PlatformMaintenanceTriggerLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_maintenance_trigger_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformMaintenanceTriggerLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_maintenance_trigger_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_maintenance_trigger_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_maintenance_trigger_log")
public class PlatformMaintenanceTriggerLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trigger_source", nullable = false)
    @NotNull
    @Size(max = 100)
    private String triggerSource; // PREDICTIVE_ENGINE, MANUAL, THRESHOLD

    @Column(name = "predicted_failure_id")
    private Long predictedFailureId;

    @Column(name = "work_order_reference", nullable = false)
    @NotNull
    @Size(max = 100)
    private String workOrderReference;

    @Column(name = "maintenance_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String maintenanceStatus; // SCHEDULED, ASSIGNED, COMPLETED, CANCELLED

    @Column(name = "scheduled_time", nullable = false)
    @NotNull
    private LocalDateTime scheduledTime;

    @Column(name = "completion_time")
    private LocalDateTime completionTime;

    @Column(name = "technician_assignment")
    @Size(max = 100)
    private String technicianAssignment;

    @Column(name = "automatic_execution", nullable = false)
    @NotNull
    private Boolean automaticExecution = true;

    @Column(name = "rollback_status")
    @Size(max = 50)
    private String rollbackStatus;

    @Column(name = "triggered_at", nullable = false)
    @NotNull
    private LocalDateTime triggeredAt = LocalDateTime.now();

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
     * Retrieves trigger source data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTriggerSource() { return triggerSource; }
    /**
     * Performs the setTriggerSource operation in this module.
     *
     * @param triggerSource the triggerSource input value
     */
    public void setTriggerSource(String triggerSource) { this.triggerSource = triggerSource; }
    /**
     * Retrieves predicted failure id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPredictedFailureId() { return predictedFailureId; }
    /**
     * Performs the setPredictedFailureId operation in this module.
     *
     * @param predictedFailureId the predictedFailureId input value
     */
    public void setPredictedFailureId(Long predictedFailureId) { this.predictedFailureId = predictedFailureId; }
    /**
     * Retrieves work order reference data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getWorkOrderReference() { return workOrderReference; }
    /**
     * Performs the setWorkOrderReference operation in this module.
     *
     * @param workOrderReference the workOrderReference input value
     */
    public void setWorkOrderReference(String workOrderReference) { this.workOrderReference = workOrderReference; }
    /**
     * Retrieves maintenance status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMaintenanceStatus() { return maintenanceStatus; }
    /**
     * Performs the setMaintenanceStatus operation in this module.
     *
     * @param maintenanceStatus the maintenanceStatus input value
     */
    public void setMaintenanceStatus(String maintenanceStatus) { this.maintenanceStatus = maintenanceStatus; }
    /**
     * Retrieves scheduled time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    /**
     * Performs the setScheduledTime operation in this module.
     *
     * @param scheduledTime the scheduledTime input value
     */
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    /**
     * Retrieves completion time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletionTime() { return completionTime; }
    /**
     * Performs the setCompletionTime operation in this module.
     *
     * @param completionTime the completionTime input value
     */
    public void setCompletionTime(LocalDateTime completionTime) { this.completionTime = completionTime; }
    /**
     * Retrieves technician assignment data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTechnicianAssignment() { return technicianAssignment; }
    /**
     * Performs the setTechnicianAssignment operation in this module.
     *
     * @param technicianAssignment the technicianAssignment input value
     */
    public void setTechnicianAssignment(String technicianAssignment) { this.technicianAssignment = technicianAssignment; }
    /**
     * Retrieves automatic execution data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getAutomaticExecution() { return automaticExecution; }
    /**
     * Performs the setAutomaticExecution operation in this module.
     *
     * @param automaticExecution the automaticExecution input value
     */
    public void setAutomaticExecution(Boolean automaticExecution) { this.automaticExecution = automaticExecution; }
    /**
     * Retrieves rollback status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRollbackStatus() { return rollbackStatus; }
    /**
     * Performs the setRollbackStatus operation in this module.
     *
     * @param rollbackStatus the rollbackStatus input value
     */
    public void setRollbackStatus(String rollbackStatus) { this.rollbackStatus = rollbackStatus; }
    /**
     * Retrieves triggered at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getTriggeredAt() { return triggeredAt; }
    /**
     * Performs the setTriggeredAt operation in this module.
     *
     * @param triggeredAt the triggeredAt input value
     */
    public void setTriggeredAt(LocalDateTime triggeredAt) { this.triggeredAt = triggeredAt; }
}