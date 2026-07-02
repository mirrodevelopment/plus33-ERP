package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_reliability_failure_log")
public class PlatformReliabilityFailureLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_id", nullable = false)
    @NotNull
    private Long assetId;

    @Column(name = "mtbf_hours", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal mtbfHours;

    @Column(name = "mttr_hours", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal mttrHours;

    @Column(name = "availability_rate", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal availabilityRate;

    @Column(name = "failure_rate", nullable = false, precision = 7, scale = 5)
    @NotNull
    private BigDecimal failureRate;

    @Column(name = "reliability_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal reliabilityScore;

    @Column(name = "repair_duration_minutes", nullable = false)
    @NotNull
    private Integer repairDurationMinutes;

    @Column(name = "downtime_duration_minutes", nullable = false)
    @NotNull
    private Integer downtimeDurationMinutes;

    @Column(name = "root_cause_category", nullable = false)
    @NotNull
    @Size(max = 200)
    private String rootCauseCategory;

    @Column(name = "failure_mode", nullable = false)
    @NotNull
    @Size(max = 200)
    private String failureMode;

    @Column(name = "corrective_action")
    @Size(max = 500)
    private String correctiveAction;

    @Column(name = "reported_at", nullable = false)
    @NotNull
    private LocalDateTime reportedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public BigDecimal getMtbfHours() { return mtbfHours; }
    public void setMtbfHours(BigDecimal mtbfHours) { this.mtbfHours = mtbfHours; }
    public BigDecimal getMttrHours() { return mttrHours; }
    public void setMttrHours(BigDecimal mttrHours) { this.mttrHours = mttrHours; }
    public BigDecimal getAvailabilityRate() { return availabilityRate; }
    public void setAvailabilityRate(BigDecimal availabilityRate) { this.availabilityRate = availabilityRate; }
    public BigDecimal getFailureRate() { return failureRate; }
    public void setFailureRate(BigDecimal failureRate) { this.failureRate = failureRate; }
    public BigDecimal getReliabilityScore() { return reliabilityScore; }
    public void setReliabilityScore(BigDecimal reliabilityScore) { this.reliabilityScore = reliabilityScore; }
    public Integer getRepairDurationMinutes() { return repairDurationMinutes; }
    public void setRepairDurationMinutes(Integer repairDurationMinutes) { this.repairDurationMinutes = repairDurationMinutes; }
    public Integer getDowntimeDurationMinutes() { return downtimeDurationMinutes; }
    public void setDowntimeDurationMinutes(Integer downtimeDurationMinutes) { this.downtimeDurationMinutes = downtimeDurationMinutes; }
    public String getRootCauseCategory() { return rootCauseCategory; }
    public void setRootCauseCategory(String rootCauseCategory) { this.rootCauseCategory = rootCauseCategory; }
    public String getFailureMode() { return failureMode; }
    public void setFailureMode(String failureMode) { this.failureMode = failureMode; }
    public String getCorrectiveAction() { return correctiveAction; }
    public void setCorrectiveAction(String correctiveAction) { this.correctiveAction = correctiveAction; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
}