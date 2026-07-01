package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_forecast_model_registry")
public class BiForecastModelRegistry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "model_code", nullable = false, unique = true, length = 100) private String modelCode;
    @Column(name = "model_name", nullable = false, length = 200) private String modelName;
    @Column(name = "model_type", nullable = false, length = 50) private String modelType = "LINEAR";
    @Column(name = "forecast_domain", nullable = false, length = 50) private String forecastDomain;
    @Column(name = "is_active", nullable = false) private Boolean isActive = true;
    @Column(name = "is_default", nullable = false) private Boolean isDefault = false;
    @Column(name = "accuracy_score", precision = 5, scale = 2) private BigDecimal accuracyScore;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getModelCode() { return modelCode; } public void setModelCode(String v) { this.modelCode = v; }
    public String getModelName() { return modelName; } public void setModelName(String v) { this.modelName = v; }
    public String getModelType() { return modelType; } public void setModelType(String v) { this.modelType = v; }
    public String getForecastDomain() { return forecastDomain; } public void setForecastDomain(String v) { this.forecastDomain = v; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean v) { this.isActive = v; }
    public Boolean getIsDefault() { return isDefault; } public void setIsDefault(Boolean v) { this.isDefault = v; }
    public BigDecimal getAccuracyScore() { return accuracyScore; } public void setAccuracyScore(BigDecimal v) { this.accuracyScore = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
