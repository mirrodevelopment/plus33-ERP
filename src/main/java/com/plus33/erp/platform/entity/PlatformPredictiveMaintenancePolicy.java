package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_predictive_maintenance_policy")
public class PlatformPredictiveMaintenancePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "policy_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String policyCode;

    @Column(name = "policy_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String policyName;

    @Column(name = "asset_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String assetType;

    @Column(name = "prediction_model", nullable = false)
    @NotNull
    @Size(max = 100)
    private String predictionModel;

    @Column(name = "minimum_health_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal minimumHealthScore;

    @Column(name = "remaining_useful_life_threshold", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal remainingUsefulLifeThreshold;

    @Column(name = "failure_probability_threshold", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal failureProbabilityThreshold;

    @Column(name = "maintenance_strategy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String maintenanceStrategy; // Preventive, Predictive, Condition-Based

    @Column(nullable = false)
    @NotNull
    private Integer priority = 0;

    @Column(nullable = false)
    @NotNull
    private Boolean enabled = true;

    @Column(name = "created_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getPolicyCode() { return policyCode; }
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    public String getPolicyName() { return policyName; }
    public void setPolicyName(String policyName) { this.policyName = policyName; }
    public String getAssetType() { return assetType; }
    public void setAssetType(String assetType) { this.assetType = assetType; }
    public String getPredictionModel() { return predictionModel; }
    public void setPredictionModel(String predictionModel) { this.predictionModel = predictionModel; }
    public BigDecimal getMinimumHealthScore() { return minimumHealthScore; }
    public void setMinimumHealthScore(BigDecimal minimumHealthScore) { this.minimumHealthScore = minimumHealthScore; }
    public BigDecimal getRemainingUsefulLifeThreshold() { return remainingUsefulLifeThreshold; }
    public void setRemainingUsefulLifeThreshold(BigDecimal remainingUsefulLifeThreshold) { this.remainingUsefulLifeThreshold = remainingUsefulLifeThreshold; }
    public BigDecimal getFailureProbabilityThreshold() { return failureProbabilityThreshold; }
    public void setFailureProbabilityThreshold(BigDecimal failureProbabilityThreshold) { this.failureProbabilityThreshold = failureProbabilityThreshold; }
    public String getMaintenanceStrategy() { return maintenanceStrategy; }
    public void setMaintenanceStrategy(String maintenanceStrategy) { this.maintenanceStrategy = maintenanceStrategy; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}