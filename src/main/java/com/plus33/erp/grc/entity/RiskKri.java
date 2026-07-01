package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "grc_risk_kris")
public class RiskKri {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "risk_id", nullable = false) private Long riskId;
    @Column(name = "indicator_name", nullable = false, length = 150) private String indicatorName;
    @Column(name = "threshold_value", nullable = false) private BigDecimal thresholdValue;
    @Column(name = "current_value") private BigDecimal currentValue;
    @Column(nullable = false) private Boolean breached = false;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getRiskId() { return riskId; } public void setRiskId(Long v) { this.riskId = v; }
    public String getIndicatorName() { return indicatorName; } public void setIndicatorName(String v) { this.indicatorName = v; }
    public BigDecimal getThresholdValue() { return thresholdValue; } public void setThresholdValue(BigDecimal v) { this.thresholdValue = v; }
    public BigDecimal getCurrentValue() { return currentValue; } public void setCurrentValue(BigDecimal v) { this.currentValue = v; }
    public Boolean getBreached() { return breached; } public void setBreached(Boolean v) { this.breached = v; }
}
