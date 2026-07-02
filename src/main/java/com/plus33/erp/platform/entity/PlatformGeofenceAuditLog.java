package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getGeofenceId() { return geofenceId; }
    public void setGeofenceId(Long geofenceId) { this.geofenceId = geofenceId; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public String getPreviousGeometryWkt() { return previousGeometryWkt; }
    public void setPreviousGeometryWkt(String previousGeometryWkt) { this.previousGeometryWkt = previousGeometryWkt; }
    public String getNewGeometryWkt() { return newGeometryWkt; }
    public void setNewGeometryWkt(String newGeometryWkt) { this.newGeometryWkt = newGeometryWkt; }
    public String getApprovalId() { return approvalId; }
    public void setApprovalId(String approvalId) { this.approvalId = approvalId; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public LocalDateTime getAuditedAt() { return auditedAt; }
    public void setAuditedAt(LocalDateTime auditedAt) { this.auditedAt = auditedAt; }
}