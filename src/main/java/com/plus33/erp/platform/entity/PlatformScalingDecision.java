package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_scaling_decision")
public class PlatformScalingDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "metric_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String metricName;

    @Column(name = "current_value", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal currentValue;

    @Column(name = "threshold_value", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal thresholdValue;

    @Column(name = "current_replicas", nullable = false)
    @NotNull
    private Integer currentReplicas;

    @Column(name = "desired_replicas", nullable = false)
    @NotNull
    private Integer desiredReplicas;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String reason;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
    public BigDecimal getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(BigDecimal thresholdValue) { this.thresholdValue = thresholdValue; }
    public Integer getCurrentReplicas() { return currentReplicas; }
    public void setCurrentReplicas(Integer currentReplicas) { this.currentReplicas = currentReplicas; }
    public Integer getDesiredReplicas() { return desiredReplicas; }
    public void setDesiredReplicas(Integer desiredReplicas) { this.desiredReplicas = desiredReplicas; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}