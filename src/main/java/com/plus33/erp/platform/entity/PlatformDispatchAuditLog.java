package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_dispatch_audit_log")
public class PlatformDispatchAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "optimizer_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String optimizerVersion;

    @Column(name = "planning_time_ms", nullable = false)
    @NotNull
    private Long planningTimeMs;

    @Column(name = "decision_trace", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String decisionTrace;

    @Column(name = "rollback_reference")
    @Size(max = 100)
    private String rollbackReference;

    @Column(name = "execution_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String executionId;

    @Column(name = "audited_at", nullable = false)
    @NotNull
    private LocalDateTime auditedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOptimizerVersion() { return optimizerVersion; }
    public void setOptimizerVersion(String optimizerVersion) { this.optimizerVersion = optimizerVersion; }
    public Long getPlanningTimeMs() { return planningTimeMs; }
    public void setPlanningTimeMs(Long planningTimeMs) { this.planningTimeMs = planningTimeMs; }
    public String getDecisionTrace() { return decisionTrace; }
    public void setDecisionTrace(String decisionTrace) { this.decisionTrace = decisionTrace; }
    public String getRollbackReference() { return rollbackReference; }
    public void setRollbackReference(String rollbackReference) { this.rollbackReference = rollbackReference; }
    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }
    public LocalDateTime getAuditedAt() { return auditedAt; }
    public void setAuditedAt(LocalDateTime auditedAt) { this.auditedAt = auditedAt; }
}