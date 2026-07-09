/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformOtaAuditLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformOtaAuditLogController
 * Related Service   : PlatformOtaAuditLogService, PlatformOtaAuditLogServiceImpl
 * Related Repository: PlatformOtaAuditLogRepository
 * Related Entity    : PlatformOtaAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformOtaAuditLogMapper
 * Related DB Table  : platform_ota_audit_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformOtaAuditLogRepository, PlatformOtaAuditLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_ota_audit_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformOtaAuditLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_ota_audit_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ota_audit_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_ota_audit_log")
public class PlatformOtaAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "campaign_id", nullable = false)
    @NotNull
    private Long campaignId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(name = "action_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String actionType; // CREATE_CAMPAIGN, START_CAMPAIGN, CANCEL_CAMPAIGN, FORCE_ROLLBACK

    @Column(name = "previous_config", columnDefinition = "TEXT")
    private String previousConfig;

    @Column(name = "new_config", columnDefinition = "TEXT")
    private String newConfig;

    @Column(name = "approval_id")
    @Size(max = 100)
    private String approvalId;

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
     * Retrieves campaign id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCampaignId() { return campaignId; }
    /**
     * Performs the setCampaignId operation in this module.
     *
     * @param campaignId the campaignId input value
     */
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
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
     * Retrieves previous config data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPreviousConfig() { return previousConfig; }
    /**
     * Performs the setPreviousConfig operation in this module.
     *
     * @param previousConfig the previousConfig input value
     */
    public void setPreviousConfig(String previousConfig) { this.previousConfig = previousConfig; }
    /**
     * Retrieves new config data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNewConfig() { return newConfig; }
    /**
     * Performs the setNewConfig operation in this module.
     *
     * @param newConfig the newConfig input value
     */
    public void setNewConfig(String newConfig) { this.newConfig = newConfig; }
    /**
     * Retrieves approval id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getApprovalId() { return approvalId; }
    /**
     * Performs the setApprovalId operation in this module.
     *
     * @param approvalId the approvalId input value
     */
    public void setApprovalId(String approvalId) { this.approvalId = approvalId; }
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