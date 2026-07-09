/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformRouteAuditLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRouteAuditLogController
 * Related Service   : PlatformRouteAuditLogService, PlatformRouteAuditLogServiceImpl
 * Related Repository: PlatformRouteAuditLogRepository
 * Related Entity    : PlatformRouteAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformRouteAuditLogMapper
 * Related DB Table  : platform_route_audit_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRouteAuditLogRepository, PlatformRouteAuditLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_route_audit_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformRouteAuditLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_route_audit_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_route_audit_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_route_audit_log")
public class PlatformRouteAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transit_route_id", nullable = false)
    @NotNull
    private Long transitRouteId;

    @Column(name = "old_route_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String oldRouteJson;

    @Column(name = "new_route_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String newRouteJson;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String reason;

    @Column(name = "trigger_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String triggerType;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(name = "changed_at", nullable = false)
    @NotNull
    private LocalDateTime changedAt = LocalDateTime.now();

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
     * Retrieves transit route id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTransitRouteId() { return transitRouteId; }
    /**
     * Performs the setTransitRouteId operation in this module.
     *
     * @param transitRouteId the transitRouteId input value
     */
    public void setTransitRouteId(Long transitRouteId) { this.transitRouteId = transitRouteId; }
    /**
     * Retrieves old route json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOldRouteJson() { return oldRouteJson; }
    /**
     * Performs the setOldRouteJson operation in this module.
     *
     * @param oldRouteJson the oldRouteJson input value
     */
    public void setOldRouteJson(String oldRouteJson) { this.oldRouteJson = oldRouteJson; }
    /**
     * Retrieves new route json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNewRouteJson() { return newRouteJson; }
    /**
     * Performs the setNewRouteJson operation in this module.
     *
     * @param newRouteJson the newRouteJson input value
     */
    public void setNewRouteJson(String newRouteJson) { this.newRouteJson = newRouteJson; }
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
     * Retrieves trigger type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTriggerType() { return triggerType; }
    /**
     * Performs the setTriggerType operation in this module.
     *
     * @param triggerType the triggerType input value
     */
    public void setTriggerType(String triggerType) { this.triggerType = triggerType; }
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
     * Retrieves changed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getChangedAt() { return changedAt; }
    /**
     * Performs the setChangedAt operation in this module.
     *
     * @param changedAt the changedAt input value
     */
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}