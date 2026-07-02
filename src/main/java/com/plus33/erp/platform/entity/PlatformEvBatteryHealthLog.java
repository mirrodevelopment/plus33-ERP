package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_ev_battery_health_log")
public class PlatformEvBatteryHealthLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(name = "degradation_percentage", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal degradationPercentage;

    @Column(name = "internal_resistance_mohm", nullable = false, precision = 7, scale = 2)
    @NotNull
    private BigDecimal internalResistanceMohm;

    @Column(name = "cell_voltage_variance", nullable = false, precision = 5, scale = 3)
    @NotNull
    private BigDecimal cellVoltageVariance;

    @Column(name = "thermal_balance_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal thermalBalanceScore;

    @Column(name = "estimated_remaining_cycles", nullable = false)
    @NotNull
    private Integer estimatedRemainingCycles;

    @Column(name = "estimated_end_of_life", nullable = false)
    @NotNull
    private LocalDateTime estimatedEndOfLife;

    @Column(name = "health_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal healthScore;

    @Column(name = "diagnostic_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String diagnosticVersion;

    @Column(name = "prediction_confidence", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal predictionConfidence;

    @Column(name = "logged_at", nullable = false)
    @NotNull
    private LocalDateTime loggedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public BigDecimal getDegradationPercentage() { return degradationPercentage; }
    public void setDegradationPercentage(BigDecimal degradationPercentage) { this.degradationPercentage = degradationPercentage; }
    public BigDecimal getInternalResistanceMohm() { return internalResistanceMohm; }
    public void setInternalResistanceMohm(BigDecimal internalResistanceMohm) { this.internalResistanceMohm = internalResistanceMohm; }
    public BigDecimal getCellVoltageVariance() { return cellVoltageVariance; }
    public void setCellVoltageVariance(BigDecimal cellVoltageVariance) { this.cellVoltageVariance = cellVoltageVariance; }
    public BigDecimal getThermalBalanceScore() { return thermalBalanceScore; }
    public void setThermalBalanceScore(BigDecimal thermalBalanceScore) { this.thermalBalanceScore = thermalBalanceScore; }
    public Integer getEstimatedRemainingCycles() { return estimatedRemainingCycles; }
    public void setEstimatedRemainingCycles(Integer estimatedRemainingCycles) { this.estimatedRemainingCycles = estimatedRemainingCycles; }
    public LocalDateTime getEstimatedEndOfLife() { return estimatedEndOfLife; }
    public void setEstimatedEndOfLife(LocalDateTime estimatedEndOfLife) { this.estimatedEndOfLife = estimatedEndOfLife; }
    public BigDecimal getHealthScore() { return healthScore; }
    public void setHealthScore(BigDecimal healthScore) { this.healthScore = healthScore; }
    public String getDiagnosticVersion() { return diagnosticVersion; }
    public void setDiagnosticVersion(String diagnosticVersion) { this.diagnosticVersion = diagnosticVersion; }
    public BigDecimal getPredictionConfidence() { return predictionConfidence; }
    public void setPredictionConfidence(BigDecimal predictionConfidence) { this.predictionConfidence = predictionConfidence; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}