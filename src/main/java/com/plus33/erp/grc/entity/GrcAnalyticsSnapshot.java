package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "grc_analytics_snapshots")
public class GrcAnalyticsSnapshot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "metric_name", nullable = false, length = 80) private String metricName;
    @Column(name = "metric_value", nullable = false) private BigDecimal metricValue;
    @Column(name = "recorded_date", nullable = false) private LocalDate recordedDate;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getMetricName() { return metricName; } public void setMetricName(String v) { this.metricName = v; }
    public BigDecimal getMetricValue() { return metricValue; } public void setMetricValue(BigDecimal v) { this.metricValue = v; }
    public LocalDate getRecordedDate() { return recordedDate; } public void setRecordedDate(LocalDate v) { this.recordedDate = v; }
}
