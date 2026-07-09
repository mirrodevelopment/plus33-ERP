/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAuditLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAuditLogController
 * Related Service   : PlatformAuditLogService, PlatformAuditLogServiceImpl
 * Related Repository: PlatformAuditLogRepository
 * Related Entity    : PlatformAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformAuditLogMapper
 * Related DB Table  : platform_audit_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAuditLogRepository, PlatformAuditLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_audit_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAuditLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_audit_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_audit_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_audit_log")
public class PlatformAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String actionName;

    @Column(name = "user_identity", nullable = false)
    @NotNull
    @Size(max = 100)
    private String userIdentity;

    @Column(name = "trace_context")
    @Size(max = 250)
    private String traceContext;

    @Column(name = "payload_diff", columnDefinition = "TEXT")
    private String payloadDiff;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves action name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getActionName() { return actionName; }
    /**
     * Performs the setActionName operation in this module.
     *
     * @param actionName the actionName input value
     */
    public void setActionName(String actionName) { this.actionName = actionName; }
    /**
     * Retrieves user identity data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getUserIdentity() { return userIdentity; }
    /**
     * Performs the setUserIdentity operation in this module.
     *
     * @param userIdentity the userIdentity input value
     */
    public void setUserIdentity(String userIdentity) { this.userIdentity = userIdentity; }
    /**
     * Retrieves trace context data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTraceContext() { return traceContext; }
    /**
     * Performs the setTraceContext operation in this module.
     *
     * @param traceContext the traceContext input value
     */
    public void setTraceContext(String traceContext) { this.traceContext = traceContext; }
    /**
     * Retrieves payload diff data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayloadDiff() { return payloadDiff; }
    /**
     * Performs the setPayloadDiff operation in this module.
     *
     * @param payloadDiff the payloadDiff input value
     */
    public void setPayloadDiff(String payloadDiff) { this.payloadDiff = payloadDiff; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}