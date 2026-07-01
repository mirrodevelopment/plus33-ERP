package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_ai_model_metrics")
public class PlatformAiModelMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_version_id", nullable = false)
    @NotNull
    private Long modelVersionId;

    @Column(precision = 10, scale = 4)
    private BigDecimal rmse;

    @Column(precision = 10, scale = 4)
    private BigDecimal mae;

    @Column(name = "recorded_at", nullable = false)
    @NotNull
    private LocalDateTime recordedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getModelVersionId() { return modelVersionId; }
    public void setModelVersionId(Long modelVersionId) { this.modelVersionId = modelVersionId; }
    public BigDecimal getRmse() { return rmse; }
    public void setRmse(BigDecimal rmse) { this.rmse = rmse; }
    public BigDecimal getMae() { return mae; }
    public void setMae(BigDecimal mae) { this.mae = mae; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}