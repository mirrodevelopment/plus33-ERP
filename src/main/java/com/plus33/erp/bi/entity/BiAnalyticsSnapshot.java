package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_analytics_snapshot")
public class BiAnalyticsSnapshot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "snapshot_date", nullable = false) private LocalDate snapshotDate;
    @Column(name = "snapshot_period", nullable = false, length = 20) private String snapshotPeriod = "MONTHLY";
    @Column(name = "kpi_code", nullable = false, length = 100) private String kpiCode;
    @Column(name = "kpi_value", precision = 19, scale = 4) private BigDecimal kpiValue;
    @Column(name = "kpi_unit", length = 50) private String kpiUnit;
    @Column(name = "dimension_filters", columnDefinition = "TEXT") private String dimensionFilters;
    @Column(name = "source_job_run_id") private Long sourceJobRunId;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public LocalDate getSnapshotDate() { return snapshotDate; } public void setSnapshotDate(LocalDate v) { this.snapshotDate = v; }
    public String getSnapshotPeriod() { return snapshotPeriod; } public void setSnapshotPeriod(String v) { this.snapshotPeriod = v; }
    public String getKpiCode() { return kpiCode; } public void setKpiCode(String v) { this.kpiCode = v; }
    public BigDecimal getKpiValue() { return kpiValue; } public void setKpiValue(BigDecimal v) { this.kpiValue = v; }
    public String getKpiUnit() { return kpiUnit; } public void setKpiUnit(String v) { this.kpiUnit = v; }
    public String getDimensionFilters() { return dimensionFilters; } public void setDimensionFilters(String v) { this.dimensionFilters = v; }
    public Long getSourceJobRunId() { return sourceJobRunId; } public void setSourceJobRunId(Long v) { this.sourceJobRunId = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
