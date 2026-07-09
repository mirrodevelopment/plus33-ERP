/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformPredictiveAuditLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformPredictiveAuditLogController
 * Related Service   : PlatformPredictiveAuditLogService, PlatformPredictiveAuditLogServiceImpl
 * Related Repository: PlatformPredictiveAuditLogRepository
 * Related Entity    : PlatformPredictiveAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformPredictiveAuditLogMapper
 * Related DB Table  : platform_predictive_audit_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformPredictiveAuditLogRepository, PlatformPredictiveAuditLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_predictive_audit_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformPredictiveAuditLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_predictive_audit_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_predictive_audit_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_predictive_audit_log")
public class PlatformPredictiveAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prediction_id", nullable = false)
    @NotNull
    private Long predictionId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(name = "action_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String actionType; // UPDATE_THRESHOLD, MODEL_REDEPLOY, STRATEGY_CHANGE

    @Column(name = "previous_threshold_config", columnDefinition = "TEXT")
    private String previousThresholdConfig;

    @Column(name = "new_threshold_config", columnDefinition = "TEXT")
    private String newThresholdConfig;

    @Column(name = "approval_reference")
    @Size(max = 100)
    private String approvalReference;

    @Column(name = "trace_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String traceId;

    @Size(max = 500)
    private String reason;

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
     * Retrieves prediction id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPredictionId() { return predictionId; }
    /**
     * Performs the setPredictionId operation in this module.
     *
     * @param predictionId the predictionId input value
     */
    public void setPredictionId(Long predictionId) { this.predictionId = predictionId; }
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
     * Retrieves action type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getActionType() { return actionType; }
    /**
     * Performs the setActionType operation in this module.
     *
     * @param actionType the actionType input value
     */
    public void setActionType(String actionType) { this.actionType = actionType; }
    /**
     * Retrieves previous threshold config data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPreviousThresholdConfig() { return previousThresholdConfig; }
    /**
     * Performs the setPreviousThresholdConfig operation in this module.
     *
     * @param previousThresholdConfig the previousThresholdConfig input value
     */
    public void setPreviousThresholdConfig(String previousThresholdConfig) { this.previousThresholdConfig = previousThresholdConfig; }
    /**
     * Retrieves new threshold config data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNewThresholdConfig() { return newThresholdConfig; }
    /**
     * Performs the setNewThresholdConfig operation in this module.
     *
     * @param newThresholdConfig the newThresholdConfig input value
     */
    public void setNewThresholdConfig(String newThresholdConfig) { this.newThresholdConfig = newThresholdConfig; }
    /**
     * Retrieves approval reference data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getApprovalReference() { return approvalReference; }
    /**
     * Performs the setApprovalReference operation in this module.
     *
     * @param approvalReference the approvalReference input value
     */
    public void setApprovalReference(String approvalReference) { this.approvalReference = approvalReference; }
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