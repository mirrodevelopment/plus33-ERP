package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_fuel_audit_log")
public class PlatformFuelAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_version", nullable = false)
    @NotNull
    private Integer policyVersion;

    @Column(name = "optimization_algorithm", nullable = false)
    @NotNull
    @Size(max = 100)
    private String optimizationAlgorithm;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(name = "approval_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String approvalStatus;

    @Column(name = "execution_time_ms", nullable = false)
    @NotNull
    private Long executionTimeMs;

    @Column(name = "previous_configuration", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String previousConfiguration;

    @Column(name = "new_configuration", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String newConfiguration;

    @Column(name = "trace_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String traceId;

    @Column(name = "audited_at", nullable = false)
    @NotNull
    private LocalDateTime auditedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getPolicyVersion() { return policyVersion; }
    public void setPolicyVersion(Integer policyVersion) { this.policyVersion = policyVersion; }
    public String getOptimizationAlgorithm() { return optimizationAlgorithm; }
    public void setOptimizationAlgorithm(String optimizationAlgorithm) { this.optimizationAlgorithm = optimizationAlgorithm; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    public String getPreviousConfiguration() { return previousConfiguration; }
    public void setPreviousConfiguration(String previousConfiguration) { this.previousConfiguration = previousConfiguration; }
    public String getNewConfiguration() { return newConfiguration; }
    public void setNewConfiguration(String newConfiguration) { this.newConfiguration = newConfiguration; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public LocalDateTime getAuditedAt() { return auditedAt; }
    public void setAuditedAt(LocalDateTime auditedAt) { this.auditedAt = auditedAt; }
}