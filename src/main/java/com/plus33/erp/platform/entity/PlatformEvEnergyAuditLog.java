package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_ev_energy_audit_log")
public class PlatformEvEnergyAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "optimization_algorithm", nullable = false)
    @NotNull
    @Size(max = 100)
    private String optimizationAlgorithm;

    @Column(name = "energy_before_kwh", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal energyBeforeKwh;

    @Column(name = "energy_after_kwh", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal energyAfterKwh;

    @Column(name = "estimated_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal estimatedCost;

    @Column(name = "estimated_savings", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal estimatedSavings;

    @Column(name = "carbon_offset_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal carbonOffsetKg;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(name = "trace_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String traceId;

    @Column(name = "execution_duration_ms", nullable = false)
    @NotNull
    private Long executionDurationMs;

    @Column(name = "audited_at", nullable = false)
    @NotNull
    private LocalDateTime auditedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOptimizationAlgorithm() { return optimizationAlgorithm; }
    public void setOptimizationAlgorithm(String optimizationAlgorithm) { this.optimizationAlgorithm = optimizationAlgorithm; }
    public BigDecimal getEnergyBeforeKwh() { return energyBeforeKwh; }
    public void setEnergyBeforeKwh(BigDecimal energyBeforeKwh) { this.energyBeforeKwh = energyBeforeKwh; }
    public BigDecimal getEnergyAfterKwh() { return energyAfterKwh; }
    public void setEnergyAfterKwh(BigDecimal energyAfterKwh) { this.energyAfterKwh = energyAfterKwh; }
    public BigDecimal getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(BigDecimal estimatedCost) { this.estimatedCost = estimatedCost; }
    public BigDecimal getEstimatedSavings() { return estimatedSavings; }
    public void setEstimatedSavings(BigDecimal estimatedSavings) { this.estimatedSavings = estimatedSavings; }
    public BigDecimal getCarbonOffsetKg() { return carbonOffsetKg; }
    public void setCarbonOffsetKg(BigDecimal carbonOffsetKg) { this.carbonOffsetKg = carbonOffsetKg; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public Long getExecutionDurationMs() { return executionDurationMs; }
    public void setExecutionDurationMs(Long executionDurationMs) { this.executionDurationMs = executionDurationMs; }
    public LocalDateTime getAuditedAt() { return auditedAt; }
    public void setAuditedAt(LocalDateTime auditedAt) { this.auditedAt = auditedAt; }
}