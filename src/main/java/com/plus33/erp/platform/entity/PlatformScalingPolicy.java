package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "platform_scaling_policy")
public class PlatformScalingPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "metric_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String metricName;

    @Column(name = "threshold_value", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal thresholdValue;

    @Column(name = "min_replicas", nullable = false)
    @NotNull
    private Integer minReplicas = 1;

    @Column(name = "max_replicas", nullable = false)
    @NotNull
    private Integer maxReplicas = 10;

    @Column(name = "cooldown_seconds", nullable = false)
    @NotNull
    private Integer cooldownSeconds = 300;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }
    public BigDecimal getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(BigDecimal thresholdValue) { this.thresholdValue = thresholdValue; }
    public Integer getMinReplicas() { return minReplicas; }
    public void setMinReplicas(Integer minReplicas) { this.minReplicas = minReplicas; }
    public Integer getMaxReplicas() { return maxReplicas; }
    public void setMaxReplicas(Integer maxReplicas) { this.maxReplicas = maxReplicas; }
    public Integer getCooldownSeconds() { return cooldownSeconds; }
    public void setCooldownSeconds(Integer cooldownSeconds) { this.cooldownSeconds = cooldownSeconds; }
}