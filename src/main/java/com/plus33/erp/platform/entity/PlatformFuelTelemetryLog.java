package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_fuel_telemetry_log")
public class PlatformFuelTelemetryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(name = "gateway_id", nullable = false)
    @NotNull
    private Long gatewayId;

    @Column(name = "engine_rpm", nullable = false, precision = 7, scale = 2)
    @NotNull
    private BigDecimal engineRpm;

    @Column(name = "throttle_position_pct", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal throttlePositionPct;

    @Column(name = "instantaneous_fuel_rate", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal instantaneousFuelRate;

    @Column(name = "average_fuel_rate", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal averageFuelRate;

    @Column(name = "fuel_level_pct", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal fuelLevelPct;

    @Column(name = "coolant_temperature_c", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal coolantTemperatureC;

    @Column(name = "engine_load_pct", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal engineLoadPct;

    @Column(name = "odometer_km", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal odometerKm;

    @Column(name = "trip_distance_km", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal tripDistanceKm;

    @Column(name = "idle_time_seconds", nullable = false)
    @NotNull
    private Integer idleTimeSeconds;

    @Column(name = "gps_latitude", nullable = false, precision = 10, scale = 6)
    @NotNull
    private BigDecimal gpsLatitude;

    @Column(name = "gps_longitude", nullable = false, precision = 10, scale = 6)
    @NotNull
    private BigDecimal gpsLongitude;

    @Column(name = "logged_at", nullable = false)
    @NotNull
    private LocalDateTime loggedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public Long getGatewayId() { return gatewayId; }
    public void setGatewayId(Long gatewayId) { this.gatewayId = gatewayId; }
    public BigDecimal getEngineRpm() { return engineRpm; }
    public void setEngineRpm(BigDecimal engineRpm) { this.engineRpm = engineRpm; }
    public BigDecimal getThrottlePositionPct() { return throttlePositionPct; }
    public void setThrottlePositionPct(BigDecimal throttlePositionPct) { this.throttlePositionPct = throttlePositionPct; }
    public BigDecimal getInstantaneousFuelRate() { return instantaneousFuelRate; }
    public void setInstantaneousFuelRate(BigDecimal instantaneousFuelRate) { this.instantaneousFuelRate = instantaneousFuelRate; }
    public BigDecimal getAverageFuelRate() { return averageFuelRate; }
    public void setAverageFuelRate(BigDecimal averageFuelRate) { this.averageFuelRate = averageFuelRate; }
    public BigDecimal getFuelLevelPct() { return fuelLevelPct; }
    public void setFuelLevelPct(BigDecimal fuelLevelPct) { this.fuelLevelPct = fuelLevelPct; }
    public BigDecimal getCoolantTemperatureC() { return coolantTemperatureC; }
    public void setCoolantTemperatureC(BigDecimal coolantTemperatureC) { this.coolantTemperatureC = coolantTemperatureC; }
    public BigDecimal getEngineLoadPct() { return engineLoadPct; }
    public void setEngineLoadPct(BigDecimal engineLoadPct) { this.engineLoadPct = engineLoadPct; }
    public BigDecimal getOdometerKm() { return odometerKm; }
    public void setOdometerKm(BigDecimal odometerKm) { this.odometerKm = odometerKm; }
    public BigDecimal getTripDistanceKm() { return tripDistanceKm; }
    public void setTripDistanceKm(BigDecimal tripDistanceKm) { this.tripDistanceKm = tripDistanceKm; }
    public Integer getIdleTimeSeconds() { return idleTimeSeconds; }
    public void setIdleTimeSeconds(Integer idleTimeSeconds) { this.idleTimeSeconds = idleTimeSeconds; }
    public BigDecimal getGpsLatitude() { return gpsLatitude; }
    public void setGpsLatitude(BigDecimal gpsLatitude) { this.gpsLatitude = gpsLatitude; }
    public BigDecimal getGpsLongitude() { return gpsLongitude; }
    public void setGpsLongitude(BigDecimal gpsLongitude) { this.gpsLongitude = gpsLongitude; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}