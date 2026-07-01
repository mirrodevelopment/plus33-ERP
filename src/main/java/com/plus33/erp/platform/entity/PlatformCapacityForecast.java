package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_capacity_forecast")
public class PlatformCapacityForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metric_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String metricName;

    @Column(name = "forecast_value", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal forecastValue;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidence;

    @Column(name = "target_time", nullable = false)
    @NotNull
    private LocalDateTime targetTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }
    public BigDecimal getForecastValue() { return forecastValue; }
    public void setForecastValue(BigDecimal forecastValue) { this.forecastValue = forecastValue; }
    public BigDecimal getConfidence() { return confidence; }
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }
    public LocalDateTime getTargetTime() { return targetTime; }
    public void setTargetTime(LocalDateTime targetTime) { this.targetTime = targetTime; }
}