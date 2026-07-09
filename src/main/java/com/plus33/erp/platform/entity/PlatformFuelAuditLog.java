/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformFuelAuditLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformFuelAuditLogController
 * Related Service   : PlatformFuelAuditLogService, PlatformFuelAuditLogServiceImpl
 * Related Repository: PlatformFuelAuditLogRepository
 * Related Entity    : PlatformFuelAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformFuelAuditLogMapper
 * Related DB Table  : platform_fuel_audit_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformFuelAuditLogRepository, PlatformFuelAuditLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_fuel_audit_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformFuelAuditLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_fuel_audit_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_fuel_audit_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_fuel_audit_log")
public class PlatformFuelAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_version", nullable = false)
    @NotNull
    private Integer policyVersion;

    @Column(name = "optimization_algorithm", nullable = false)
    @NotNull
    @Size(max = 100)
    private String optimizationAlgorithm;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(name = "approval_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String approvalStatus;

    @Column(name = "execution_time_ms", nullable = false)
    @NotNull
    private Long executionTimeMs;

    @Column(name = "previous_configuration", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String previousConfiguration;

    @Column(name = "new_configuration", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String newConfiguration;

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
     * Retrieves policy version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPolicyVersion() { return policyVersion; }
    /**
     * Performs the setPolicyVersion operation in this module.
     *
     * @param policyVersion the policyVersion input value
     */
    public void setPolicyVersion(Integer policyVersion) { this.policyVersion = policyVersion; }
    /**
     * Retrieves optimization algorithm data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOptimizationAlgorithm() { return optimizationAlgorithm; }
    /**
     * Performs the setOptimizationAlgorithm operation in this module.
     *
     * @param optimizationAlgorithm the optimizationAlgorithm input value
     */
    public void setOptimizationAlgorithm(String optimizationAlgorithm) { this.optimizationAlgorithm = optimizationAlgorithm; }
    /**
     * Retrieves operator data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOperator() { return operator; }
    /**
     * Performs the setOperator operation in this module.
     *
     * @param operator the operator input value
     */
    public void setOperator(String operator) { this.operator = operator; }
    /**
     * Retrieves approval status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getApprovalStatus() { return approvalStatus; }
    /**
     * Performs the setApprovalStatus operation in this module.
     *
     * @param approvalStatus the approvalStatus input value
     */
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    /**
     * Retrieves execution time ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getExecutionTimeMs() { return executionTimeMs; }
    /**
     * Performs the setExecutionTimeMs operation in this module.
     *
     * @param executionTimeMs the executionTimeMs input value
     */
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    /**
     * Retrieves previous configuration data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPreviousConfiguration() { return previousConfiguration; }
    /**
     * Performs the setPreviousConfiguration operation in this module.
     *
     * @param previousConfiguration the previousConfiguration input value
     */
    public void setPreviousConfiguration(String previousConfiguration) { this.previousConfiguration = previousConfiguration; }
    /**
     * Retrieves new configuration data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNewConfiguration() { return newConfiguration; }
    /**
     * Performs the setNewConfiguration operation in this module.
     *
     * @param newConfiguration the newConfiguration input value
     */
    public void setNewConfiguration(String newConfiguration) { this.newConfiguration = newConfiguration; }
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