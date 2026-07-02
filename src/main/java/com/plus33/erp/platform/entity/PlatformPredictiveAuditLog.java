package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_predictive_audit_log")
public class PlatformPredictiveAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prediction_id", nullable = false)
    @NotNull
    private Long predictionId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(name = "action_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String actionType; // UPDATE_THRESHOLD, MODEL_REDEPLOY, STRATEGY_CHANGE

    @Column(name = "previous_threshold_config", columnDefinition = "TEXT")
    private String previousThresholdConfig;

    @Column(name = "new_threshold_config", columnDefinition = "TEXT")
    private String newThresholdConfig;

    @Column(name = "approval_reference")
    @Size(max = 100)
    private String approvalReference;

    @Column(name = "trace_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String traceId;

    @Size(max = 500)
    private String reason;

    @Column(name = "audited_at", nullable = false)
    @NotNull
    private LocalDateTime auditedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPredictionId() { return predictionId; }
    public void setPredictionId(Long predictionId) { this.predictionId = predictionId; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public String getPreviousThresholdConfig() { return previousThresholdConfig; }
    public void setPreviousThresholdConfig(String previousThresholdConfig) { this.previousThresholdConfig = previousThresholdConfig; }
    public String getNewThresholdConfig() { return newThresholdConfig; }
    public void setNewThresholdConfig(String newThresholdConfig) { this.newThresholdConfig = newThresholdConfig; }
    public String getApprovalReference() { return approvalReference; }
    public void setApprovalReference(String approvalReference) { this.approvalReference = approvalReference; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getAuditedAt() { return auditedAt; }
    public void setAuditedAt(LocalDateTime auditedAt) { this.auditedAt = auditedAt; }
}