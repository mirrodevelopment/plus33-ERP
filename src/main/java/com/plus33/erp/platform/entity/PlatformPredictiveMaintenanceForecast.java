package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_predictive_maintenance_forecast")
public class PlatformPredictiveMaintenanceForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    @NotNull
    private Long modelId;

    @Column(name = "twin_instance_id", nullable = false)
    @NotNull
    private Long twinInstanceId;

    @Column(name = "failure_probability", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal failureProbability;

    @Column(name = "expected_failure_at", nullable = false)
    @NotNull
    private LocalDateTime expectedFailureAt;

    @Column(name = "generated_at", nullable = false)
    @NotNull
    private LocalDateTime generatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }
    public Long getTwinInstanceId() { return twinInstanceId; }
    public void setTwinInstanceId(Long twinInstanceId) { this.twinInstanceId = twinInstanceId; }
    public BigDecimal getFailureProbability() { return failureProbability; }
    public void setFailureProbability(BigDecimal failureProbability) { this.failureProbability = failureProbability; }
    public LocalDateTime getExpectedFailureAt() { return expectedFailureAt; }
    public void setExpectedFailureAt(LocalDateTime expectedFailureAt) { this.expectedFailureAt = expectedFailureAt; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}