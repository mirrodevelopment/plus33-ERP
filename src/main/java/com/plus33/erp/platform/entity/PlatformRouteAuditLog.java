package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTransitRouteId() { return transitRouteId; }
    public void setTransitRouteId(Long transitRouteId) { this.transitRouteId = transitRouteId; }
    public String getOldRouteJson() { return oldRouteJson; }
    public void setOldRouteJson(String oldRouteJson) { this.oldRouteJson = oldRouteJson; }
    public String getNewRouteJson() { return newRouteJson; }
    public void setNewRouteJson(String newRouteJson) { this.newRouteJson = newRouteJson; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getTriggerType() { return triggerType; }
    public void setTriggerType(String triggerType) { this.triggerType = triggerType; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}