/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDispatchAuditLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDispatchAuditLogController
 * Related Service   : PlatformDispatchAuditLogService, PlatformDispatchAuditLogServiceImpl
 * Related Repository: PlatformDispatchAuditLogRepository
 * Related Entity    : PlatformDispatchAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformDispatchAuditLogMapper
 * Related DB Table  : platform_dispatch_audit_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDispatchAuditLogRepository, PlatformDispatchAuditLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_dispatch_audit_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDispatchAuditLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_dispatch_audit_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_dispatch_audit_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_dispatch_audit_log")
public class PlatformDispatchAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "optimizer_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String optimizerVersion;

    @Column(name = "planning_time_ms", nullable = false)
    @NotNull
    private Long planningTimeMs;

    @Column(name = "decision_trace", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String decisionTrace;

    @Column(name = "rollback_reference")
    @Size(max = 100)
    private String rollbackReference;

    @Column(name = "execution_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String executionId;

    @Column(name = "audited_at", nullable = false)
    @NotNull
    private LocalDateTime auditedAt = LocalDateTime.now();

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
     * Retrieves optimizer version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOptimizerVersion() { return optimizerVersion; }
    /**
     * Performs the setOptimizerVersion operation in this module.
     *
     * @param optimizerVersion the optimizerVersion input value
     */
    public void setOptimizerVersion(String optimizerVersion) { this.optimizerVersion = optimizerVersion; }
    /**
     * Retrieves planning time ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPlanningTimeMs() { return planningTimeMs; }
    /**
     * Performs the setPlanningTimeMs operation in this module.
     *
     * @param planningTimeMs the planningTimeMs input value
     */
    public void setPlanningTimeMs(Long planningTimeMs) { this.planningTimeMs = planningTimeMs; }
    /**
     * Retrieves decision trace data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDecisionTrace() { return decisionTrace; }
    /**
     * Performs the setDecisionTrace operation in this module.
     *
     * @param decisionTrace the decisionTrace input value
     */
    public void setDecisionTrace(String decisionTrace) { this.decisionTrace = decisionTrace; }
    /**
     * Retrieves rollback reference data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRollbackReference() { return rollbackReference; }
    /**
     * Performs the setRollbackReference operation in this module.
     *
     * @param rollbackReference the rollbackReference input value
     */
    public void setRollbackReference(String rollbackReference) { this.rollbackReference = rollbackReference; }
    /**
     * Retrieves execution id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExecutionId() { return executionId; }
    /**
     * Performs the setExecutionId operation in this module.
     *
     * @param executionId the executionId input value
     */
    public void setExecutionId(String executionId) { this.executionId = executionId; }
    /**
     * Retrieves audited at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAuditedAt() { return auditedAt; }
    /**
     * Performs the setAuditedAt operation in this module.
     *
     * @param auditedAt the auditedAt input value
     */
    public void setAuditedAt(LocalDateTime auditedAt) { this.auditedAt = auditedAt; }
}