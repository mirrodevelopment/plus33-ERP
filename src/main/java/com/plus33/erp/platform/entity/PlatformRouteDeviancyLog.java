package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_route_deviancy_log")
public class PlatformRouteDeviancyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transit_route_id", nullable = false)
    @NotNull
    private Long transitRouteId;

    @Column(name = "expected_route_wkt", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String expectedRouteWkt;

    @Column(name = "actual_route_wkt", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String actualRouteWkt;

    @Column(name = "deviation_distance", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal deviationDistance;

    @Column(name = "deviation_duration_minutes", nullable = false)
    @NotNull
    private Integer deviationDurationMinutes;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String severity; // Low, Medium, High

    @Column(name = "automatic_recovery", nullable = false)
    @NotNull
    private Boolean automaticRecovery = false;

    @Column(name = "reroute_triggered", nullable = false)
    @NotNull
    private Boolean rerouteTriggered = false;

    @Column(name = "detected_at", nullable = false)
    @NotNull
    private LocalDateTime detectedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTransitRouteId() { return transitRouteId; }
    public void setTransitRouteId(Long transitRouteId) { this.transitRouteId = transitRouteId; }
    public String getExpectedRouteWkt() { return expectedRouteWkt; }
    public void setExpectedRouteWkt(String expectedRouteWkt) { this.expectedRouteWkt = expectedRouteWkt; }
    public String getActualRouteWkt() { return actualRouteWkt; }
    public void setActualRouteWkt(String actualRouteWkt) { this.actualRouteWkt = actualRouteWkt; }
    public BigDecimal getDeviationDistance() { return deviationDistance; }
    public void setDeviationDistance(BigDecimal deviationDistance) { this.deviationDistance = deviationDistance; }
    public Integer getDeviationDurationMinutes() { return deviationDurationMinutes; }
    public void setDeviationDurationMinutes(Integer deviationDurationMinutes) { this.deviationDurationMinutes = deviationDurationMinutes; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public Boolean getAutomaticRecovery() { return automaticRecovery; }
    public void setAutomaticRecovery(Boolean automaticRecovery) { this.automaticRecovery = automaticRecovery; }
    public Boolean getRerouteTriggered() { return rerouteTriggered; }
    public void setRerouteTriggered(Boolean rerouteTriggered) { this.rerouteTriggered = rerouteTriggered; }
    public LocalDateTime getDetectedAt() { return detectedAt; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
}