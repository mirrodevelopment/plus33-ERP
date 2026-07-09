/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformGeofenceAuditLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformGeofenceAuditLogController
 * Related Service   : PlatformGeofenceAuditLogService, PlatformGeofenceAuditLogServiceImpl
 * Related Repository: PlatformGeofenceAuditLogRepository
 * Related Entity    : PlatformGeofenceAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformGeofenceAuditLogMapper
 * Related DB Table  : platform_geofence_audit_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformGeofenceAuditLogRepository, PlatformGeofenceAuditLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_geofence_audit_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformGeofenceAuditLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_geofence_audit_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_geofence_audit_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_geofence_audit_log")
public class PlatformGeofenceAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "geofence_id", nullable = false)
    @NotNull
    private Long geofenceId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(name = "action_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String actionType; // CREATE, UPDATE, DELETE

    @Column(name = "previous_geometry_wkt", columnDefinition = "TEXT")
    private String previousGeometryWkt;

    @Column(name = "new_geometry_wkt", columnDefinition = "TEXT")
    private String newGeometryWkt;

    @Column(name = "approval_id")
    @Size(max = 100)
    private String approvalId;

    @Column(name = "trace_id")
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
     * Retrieves geofence id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getGeofenceId() { return geofenceId; }
    /**
     * Performs the setGeofenceId operation in this module.
     *
     * @param geofenceId the geofenceId input value
     */
    public void setGeofenceId(Long geofenceId) { this.geofenceId = geofenceId; }
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
     * Retrieves previous geometry wkt data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPreviousGeometryWkt() { return previousGeometryWkt; }
    /**
     * Performs the setPreviousGeometryWkt operation in this module.
     *
     * @param previousGeometryWkt the previousGeometryWkt input value
     */
    public void setPreviousGeometryWkt(String previousGeometryWkt) { this.previousGeometryWkt = previousGeometryWkt; }
    /**
     * Retrieves new geometry wkt data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNewGeometryWkt() { return newGeometryWkt; }
    /**
     * Performs the setNewGeometryWkt operation in this module.
     *
     * @param newGeometryWkt the newGeometryWkt input value
     */
    public void setNewGeometryWkt(String newGeometryWkt) { this.newGeometryWkt = newGeometryWkt; }
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