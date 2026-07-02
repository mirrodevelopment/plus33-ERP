package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPreviousRoute() { return previousRoute; }
    public void setPreviousRoute(String previousRoute) { this.previousRoute = previousRoute; }
    public String getNewRoute() { return newRoute; }
    public void setNewRoute(String newRoute) { this.newRoute = newRoute; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getOptimizer() { return optimizer; }
    public void setOptimizer(String optimizer) { this.optimizer = optimizer; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public String getExecutionStatus() { return executionStatus; }
    public void setExecutionStatus(String executionStatus) { this.executionStatus = executionStatus; }
    public String getRollbackReference() { return rollbackReference; }
    public void setRollbackReference(String rollbackReference) { this.rollbackReference = rollbackReference; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public LocalDateTime getAuditedAt() { return auditedAt; }
    public void setAuditedAt(LocalDateTime auditedAt) { this.auditedAt = auditedAt; }
}