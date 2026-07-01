package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_slo_measurement")
public class PlatformSloMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slo_id", nullable = false)
    @NotNull
    private Long sloId;

    @Column(name = "current_percentage", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal currentPercentage;

    @Column(name = "error_budget", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal errorBudget;

    @Column(name = "recorded_at", nullable = false)
    @NotNull
    private LocalDateTime recordedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSloId() { return sloId; }
    public void setSloId(Long sloId) { this.sloId = sloId; }
    public BigDecimal getCurrentPercentage() { return currentPercentage; }
    public void setCurrentPercentage(BigDecimal currentPercentage) { this.currentPercentage = currentPercentage; }
    public BigDecimal getErrorBudget() { return errorBudget; }
    public void setErrorBudget(BigDecimal errorBudget) { this.errorBudget = errorBudget; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}