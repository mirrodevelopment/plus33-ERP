/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformComplianceAuditLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformComplianceAuditLogController
 * Related Service   : PlatformComplianceAuditLogService, PlatformComplianceAuditLogServiceImpl
 * Related Repository: PlatformComplianceAuditLogRepository
 * Related Entity    : PlatformComplianceAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformComplianceAuditLogMapper
 * Related DB Table  : platform_compliance_audit_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformComplianceAuditLogRepository, PlatformComplianceAuditLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_compliance_audit_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformComplianceAuditLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_compliance_audit_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_compliance_audit_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_compliance_audit_log")
public class PlatformComplianceAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    @NotNull
    private Long deviceId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(name = "action_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String actionType; // UPDATE_POLICY, REMEDIATION, OVERRIDE

    @Column(name = "previous_state", columnDefinition = "TEXT")
    private String previousState;

    @Column(name = "new_state", columnDefinition = "TEXT")
    private String newState;

    @Column(name = "approval_id")
    @Size(max = 100)
    private String approvalId;

    @Column(name = "trace_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String traceId;

    @Size(max = 500)
    private String reason;

    @Column(name = "ip_address")
    @Size(max = 45)
    private String ipAddress;

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
     * Retrieves device id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDeviceId() { return deviceId; }
    /**
     * Performs the setDeviceId operation in this module.
     *
     * @param deviceId the deviceId input value
     */
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
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
     * Retrieves previous state data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPreviousState() { return previousState; }
    /**
     * Performs the setPreviousState operation in this module.
     *
     * @param previousState the previousState input value
     */
    public void setPreviousState(String previousState) { this.previousState = previousState; }
    /**
     * Retrieves new state data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNewState() { return newState; }
    /**
     * Performs the setNewState operation in this module.
     *
     * @param newState the newState input value
     */
    public void setNewState(String newState) { this.newState = newState; }
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
     * Retrieves ip address data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIpAddress() { return ipAddress; }
    /**
     * Performs the setIpAddress operation in this module.
     *
     * @param ipAddress the ipAddress input value
     */
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
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