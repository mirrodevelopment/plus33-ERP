package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_saga_step")
public class IntegrationSagaStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "saga_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sagaCode;

    @Column(name = "step_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String stepName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status;

    @Column(name = "action_payload", columnDefinition = "TEXT")
    private String actionPayload;

    @Column(name = "compensation_payload", columnDefinition = "TEXT")
    private String compensationPayload;

    @Column(name = "execution_order", nullable = false)
    @NotNull
    private Integer executionOrder;

    @Column(name = "executed_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime executedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSagaCode() { return sagaCode; }
    public void setSagaCode(String sagaCode) { this.sagaCode = sagaCode; }
    public String getStepName() { return stepName; }
    public void setStepName(String stepName) { this.stepName = stepName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getActionPayload() { return actionPayload; }
    public void setActionPayload(String actionPayload) { this.actionPayload = actionPayload; }
    public String getCompensationPayload() { return compensationPayload; }
    public void setCompensationPayload(String compensationPayload) { this.compensationPayload = compensationPayload; }
    public Integer getExecutionOrder() { return executionOrder; }
    public void setExecutionOrder(Integer executionOrder) { this.executionOrder = executionOrder; }
    public LocalDateTime getExecutedAt() { return executedAt; }
    public void setExecutedAt(LocalDateTime executedAt) { this.executedAt = executedAt; }
}