package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "platform_forecast_model_registry")
public class PlatformForecastModelRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "model_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String modelCode;

    @Column(name = "accuracy_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal accuracyScore;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getModelCode() { return modelCode; }
    public void setModelCode(String modelCode) { this.modelCode = modelCode; }
    public BigDecimal getAccuracyScore() { return accuracyScore; }
    public void setAccuracyScore(BigDecimal accuracyScore) { this.accuracyScore = accuracyScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}