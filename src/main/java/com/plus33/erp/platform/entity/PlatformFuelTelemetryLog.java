/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformFuelTelemetryLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformFuelTelemetryLogController
 * Related Service   : PlatformFuelTelemetryLogService, PlatformFuelTelemetryLogServiceImpl
 * Related Repository: PlatformFuelTelemetryLogRepository
 * Related Entity    : PlatformFuelTelemetryLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformFuelTelemetryLogMapper
 * Related DB Table  : platform_fuel_telemetry_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformFuelTelemetryLogRepository, PlatformFuelTelemetryLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_fuel_telemetry_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformFuelTelemetryLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_fuel_telemetry_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_fuel_telemetry_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves vehicle id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getVehicleId() { return vehicleId; }
    /**
     * Performs the setVehicleId operation in this module.
     *
     * @param vehicleId the vehicleId input value
     */
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    /**
     * Retrieves gateway id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getGatewayId() { return gatewayId; }
    /**
     * Performs the setGatewayId operation in this module.
     *
     * @param gatewayId the gatewayId input value
     */
    public void setGatewayId(Long gatewayId) { this.gatewayId = gatewayId; }
    /**
     * Retrieves engine rpm data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEngineRpm() { return engineRpm; }
    /**
     * Performs the setEngineRpm operation in this module.
     *
     * @param engineRpm the engineRpm input value
     */
    public void setEngineRpm(BigDecimal engineRpm) { this.engineRpm = engineRpm; }
    /**
     * Retrieves throttle position pct data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getThrottlePositionPct() { return throttlePositionPct; }
    /**
     * Performs the setThrottlePositionPct operation in this module.
     *
     * @param throttlePositionPct the throttlePositionPct input value
     */
    public void setThrottlePositionPct(BigDecimal throttlePositionPct) { this.throttlePositionPct = throttlePositionPct; }
    /**
     * Retrieves instantaneous fuel rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getInstantaneousFuelRate() { return instantaneousFuelRate; }
    /**
     * Performs the setInstantaneousFuelRate operation in this module.
     *
     * @param instantaneousFuelRate the instantaneousFuelRate input value
     */
    public void setInstantaneousFuelRate(BigDecimal instantaneousFuelRate) { this.instantaneousFuelRate = instantaneousFuelRate; }
    /**
     * Retrieves average fuel rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAverageFuelRate() { return averageFuelRate; }
    /**
     * Performs the setAverageFuelRate operation in this module.
     *
     * @param averageFuelRate the averageFuelRate input value
     */
    public void setAverageFuelRate(BigDecimal averageFuelRate) { this.averageFuelRate = averageFuelRate; }
    /**
     * Retrieves fuel level pct data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getFuelLevelPct() { return fuelLevelPct; }
    /**
     * Performs the setFuelLevelPct operation in this module.
     *
     * @param fuelLevelPct the fuelLevelPct input value
     */
    public void setFuelLevelPct(BigDecimal fuelLevelPct) { this.fuelLevelPct = fuelLevelPct; }
    /**
     * Retrieves coolant temperature c data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCoolantTemperatureC() { return coolantTemperatureC; }
    /**
     * Performs the setCoolantTemperatureC operation in this module.
     *
     * @param coolantTemperatureC the coolantTemperatureC input value
     */
    public void setCoolantTemperatureC(BigDecimal coolantTemperatureC) { this.coolantTemperatureC = coolantTemperatureC; }
    /**
     * Retrieves engine load pct data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEngineLoadPct() { return engineLoadPct; }
    /**
     * Performs the setEngineLoadPct operation in this module.
     *
     * @param engineLoadPct the engineLoadPct input value
     */
    public void setEngineLoadPct(BigDecimal engineLoadPct) { this.engineLoadPct = engineLoadPct; }
    /**
     * Retrieves odometer km data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getOdometerKm() { return odometerKm; }
    /**
     * Performs the setOdometerKm operation in this module.
     *
     * @param odometerKm the odometerKm input value
     */
    public void setOdometerKm(BigDecimal odometerKm) { this.odometerKm = odometerKm; }
    /**
     * Retrieves trip distance km data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTripDistanceKm() { return tripDistanceKm; }
    /**
     * Performs the setTripDistanceKm operation in this module.
     *
     * @param tripDistanceKm the tripDistanceKm input value
     */
    public void setTripDistanceKm(BigDecimal tripDistanceKm) { this.tripDistanceKm = tripDistanceKm; }
    /**
     * Retrieves idle time seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getIdleTimeSeconds() { return idleTimeSeconds; }
    /**
     * Performs the setIdleTimeSeconds operation in this module.
     *
     * @param idleTimeSeconds the idleTimeSeconds input value
     */
    public void setIdleTimeSeconds(Integer idleTimeSeconds) { this.idleTimeSeconds = idleTimeSeconds; }
    /**
     * Retrieves gps latitude data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getGpsLatitude() { return gpsLatitude; }
    /**
     * Performs the setGpsLatitude operation in this module.
     *
     * @param gpsLatitude the gpsLatitude input value
     */
    public void setGpsLatitude(BigDecimal gpsLatitude) { this.gpsLatitude = gpsLatitude; }
    /**
     * Retrieves gps longitude data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getGpsLongitude() { return gpsLongitude; }
    /**
     * Performs the setGpsLongitude operation in this module.
     *
     * @param gpsLongitude the gpsLongitude input value
     */
    public void setGpsLongitude(BigDecimal gpsLongitude) { this.gpsLongitude = gpsLongitude; }
    /**
     * Retrieves logged at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLoggedAt() { return loggedAt; }
    /**
     * Performs the setLoggedAt operation in this module.
     *
     * @param loggedAt the loggedAt input value
     */
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}