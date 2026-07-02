package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_eco_driving_log")
public class PlatformEcoDrivingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driver_id", nullable = false)
    @NotNull
    private Long driverId;

    @Column(name = "trip_id", nullable = false)
    @NotNull
    private Long tripId;

    @Column(name = "harsh_acceleration_count", nullable = false)
    @NotNull
    private Integer harshAccelerationCount;

    @Column(name = "harsh_braking_count", nullable = false)
    @NotNull
    private Integer harshBrakingCount;

    @Column(name = "harsh_cornering_count", nullable = false)
    @NotNull
    private Integer harshCorneringCount;

    @Column(name = "excessive_idle_seconds", nullable = false)
    @NotNull
    private Integer excessiveIdleSeconds;

    @Column(name = "overspeed_duration_secs", nullable = false)
    @NotNull
    private Integer overspeedDurationSecs;

    @Column(name = "cruise_control_usage_pct", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal cruiseControlUsagePct;

    @Column(name = "driver_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal driverScore;

    @Column(name = "trip_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal tripScore;

    @Column(name = "coaching_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String coachingStatus; // OK, NEEDS_COACHING, COACHED

    @Column(name = "logged_at", nullable = false)
    @NotNull
    private LocalDateTime loggedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    public Integer getHarshAccelerationCount() { return harshAccelerationCount; }
    public void setHarshAccelerationCount(Integer harshAccelerationCount) { this.harshAccelerationCount = harshAccelerationCount; }
    public Integer getHarshBrakingCount() { return harshBrakingCount; }
    public void setHarshBrakingCount(Integer harshBrakingCount) { this.harshBrakingCount = harshBrakingCount; }
    public Integer getHarshCorneringCount() { return harshCorneringCount; }
    public void setHarshCorneringCount(Integer harshCorneringCount) { this.harshCorneringCount = harshCorneringCount; }
    public Integer getExcessiveIdleSeconds() { return excessiveIdleSeconds; }
    public void setExcessiveIdleSeconds(Integer excessiveIdleSeconds) { this.excessiveIdleSeconds = excessiveIdleSeconds; }
    public Integer getOverspeedDurationSecs() { return overspeedDurationSecs; }
    public void setOverspeedDurationSecs(Integer overspeedDurationSecs) { this.overspeedDurationSecs = overspeedDurationSecs; }
    public BigDecimal getCruiseControlUsagePct() { return cruiseControlUsagePct; }
    public void setCruiseControlUsagePct(BigDecimal cruiseControlUsagePct) { this.cruiseControlUsagePct = cruiseControlUsagePct; }
    public BigDecimal getDriverScore() { return driverScore; }
    public void setDriverScore(BigDecimal driverScore) { this.driverScore = driverScore; }
    public BigDecimal getTripScore() { return tripScore; }
    public void setTripScore(BigDecimal tripScore) { this.tripScore = tripScore; }
    public String getCoachingStatus() { return coachingStatus; }
    public void setCoachingStatus(String coachingStatus) { this.coachingStatus = coachingStatus; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}