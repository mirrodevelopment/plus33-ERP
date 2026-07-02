package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_failure_prognostics_log")
public class PlatformFailurePrognosticsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_id", nullable = false)
    @NotNull
    private Long assetId;

    @Column(name = "prediction_time", nullable = false)
    @NotNull
    private LocalDateTime predictionTime = LocalDateTime.now();

    @Column(name = "predicted_failure_time")
    private LocalDateTime predictedFailureTime;

    @Column(name = "remaining_useful_life_hours", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal remainingUsefulLifeHours;

    @Column(name = "failure_probability", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal failureProbability;

    @Column(name = "confidence_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidenceScore;

    @Column(name = "prediction_model_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String predictionModelVersion;

    @Column(name = "trigger_reason")
    @Size(max = 500)
    private String triggerReason;

    @Column(name = "recommended_action")
    @Size(max = 500)
    private String recommendedAction;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public LocalDateTime getPredictionTime() { return predictionTime; }
    public void setPredictionTime(LocalDateTime predictionTime) { this.predictionTime = predictionTime; }
    public LocalDateTime getPredictedFailureTime() { return predictedFailureTime; }
    public void setPredictedFailureTime(LocalDateTime predictedFailureTime) { this.predictedFailureTime = predictedFailureTime; }
    public BigDecimal getRemainingUsefulLifeHours() { return remainingUsefulLifeHours; }
    public void setRemainingUsefulLifeHours(BigDecimal remainingUsefulLifeHours) { this.remainingUsefulLifeHours = remainingUsefulLifeHours; }
    public BigDecimal getFailureProbability() { return failureProbability; }
    public void setFailureProbability(BigDecimal failureProbability) { this.failureProbability = failureProbability; }
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    public String getPredictionModelVersion() { return predictionModelVersion; }
    public void setPredictionModelVersion(String predictionModelVersion) { this.predictionModelVersion = predictionModelVersion; }
    public String getTriggerReason() { return triggerReason; }
    public void setTriggerReason(String triggerReason) { this.triggerReason = triggerReason; }
    public String getRecommendedAction() { return recommendedAction; }
    public void setRecommendedAction(String recommendedAction) { this.recommendedAction = recommendedAction; }
}