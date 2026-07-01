package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_ai_prediction_explanation")
public class PlatformAiPredictionExplanation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prediction_time", nullable = false)
    @NotNull
    private LocalDateTime predictionTime = LocalDateTime.now();

    @Column(name = "prediction_target", nullable = false)
    @NotNull
    @Size(max = 150)
    private String predictionTarget;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String reasoning;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidence;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getPredictionTime() { return predictionTime; }
    public void setPredictionTime(LocalDateTime predictionTime) { this.predictionTime = predictionTime; }
    public String getPredictionTarget() { return predictionTarget; }
    public void setPredictionTarget(String predictionTarget) { this.predictionTarget = predictionTarget; }
    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
    public BigDecimal getConfidence() { return confidence; }
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }
}