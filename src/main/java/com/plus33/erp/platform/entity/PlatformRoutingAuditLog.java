/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformRoutingAuditLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRoutingAuditLogController
 * Related Service   : PlatformRoutingAuditLogService, PlatformRoutingAuditLogServiceImpl
 * Related Repository: PlatformRoutingAuditLogRepository
 * Related Entity    : PlatformRoutingAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformRoutingAuditLogMapper
 * Related DB Table  : platform_routing_audit_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRoutingAuditLogRepository, PlatformRoutingAuditLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_routing_audit_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformRoutingAuditLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_routing_audit_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_routing_audit_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_routing_audit_log")
public class PlatformRoutingAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "previous_route", columnDefinition = "TEXT")
    private String previousRoute;

    @Column(name = "new_route", columnDefinition = "TEXT")
    private String newRoute;

    @Size(max = 500)
    private String reason;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String optimizer;

    @Column(name = "approved_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String approvedBy;

    @Column(name = "execution_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String executionStatus; // COMPLETED, FAILED, ROLLBACK

    @Column(name = "rollback_reference")
    @Size(max = 100)
    private String rollbackReference;

    @Column(name = "trace_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String traceId;

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
     * Retrieves previous route data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPreviousRoute() { return previousRoute; }
    /**
     * Performs the setPreviousRoute operation in this module.
     *
     * @param previousRoute the previousRoute input value
     */
    public void setPreviousRoute(String previousRoute) { this.previousRoute = previousRoute; }
    /**
     * Retrieves new route data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNewRoute() { return newRoute; }
    /**
     * Performs the setNewRoute operation in this module.
     *
     * @param newRoute the newRoute input value
     */
    public void setNewRoute(String newRoute) { this.newRoute = newRoute; }
    /**
     * Retrieves reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReason() { return reason; }
    /**
     * Performs the setReason operation in this module.
     *
     * @param reason the reason input value
     */
    public void setReason(String reason) { this.reason = reason; }
    /**
     * Retrieves optimizer data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOptimizer() { return optimizer; }
    /**
     * Performs the setOptimizer operation in this module.
     *
     * @param optimizer the optimizer input value
     */
    public void setOptimizer(String optimizer) { this.optimizer = optimizer; }
    /**
     * Retrieves approved by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getApprovedBy() { return approvedBy; }
    /**
     * Performs the setApprovedBy operation in this module.
     *
     * @param approvedBy the approvedBy input value
     */
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    /**
     * Retrieves execution status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExecutionStatus() { return executionStatus; }
    /**
     * Performs the setExecutionStatus operation in this module.
     *
     * @param executionStatus the executionStatus input value
     */
    public void setExecutionStatus(String executionStatus) { this.executionStatus = executionStatus; }
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
     * Retrieves trace id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTraceId() { return traceId; }
    /**
     * Performs the setTraceId operation in this module.
     *
     * @param traceId the traceId input value
     */
    public void setTraceId(String traceId) { this.traceId = traceId; }
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