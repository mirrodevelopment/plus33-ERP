/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEvTelemetryLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEvTelemetryLogController
 * Related Service   : PlatformEvTelemetryLogService, PlatformEvTelemetryLogServiceImpl
 * Related Repository: PlatformEvTelemetryLogRepository
 * Related Entity    : PlatformEvTelemetryLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformEvTelemetryLogMapper
 * Related DB Table  : platform_ev_telemetry_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEvTelemetryLogRepository, PlatformEvTelemetryLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_ev_telemetry_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEvTelemetryLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_ev_telemetry_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ev_telemetry_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_ev_telemetry_log")
public class PlatformEvTelemetryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(name = "battery_pack_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String batteryPackId;

    @Column(name = "state_of_charge_pct", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal stateOfChargePct;

    @Column(name = "state_of_health_pct", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal stateOfHealthPct;

    @Column(name = "battery_voltage", nullable = false, precision = 7, scale = 2)
    @NotNull
    private BigDecimal batteryVoltage;

    @Column(name = "battery_current", nullable = false, precision = 7, scale = 2)
    @NotNull
    private BigDecimal batteryCurrent;

    @Column(name = "battery_temperature_c", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal batteryTemperatureC;

    @Column(name = "charging_power_kw", nullable = false, precision = 7, scale = 2)
    @NotNull
    private BigDecimal chargingPowerKw;

    @Column(name = "discharge_power_kw", nullable = false, precision = 7, scale = 2)
    @NotNull
    private BigDecimal dischargePowerKw;

    @Column(name = "regenerative_power_kw", nullable = false, precision = 7, scale = 2)
    @NotNull
    private BigDecimal regenerativePowerKw;

    @Column(name = "remaining_range_km", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal remainingRangeKm;

    @Column(name = "energy_consumed_kwh", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal energyConsumedKwh;

    @Column(name = "energy_regenerated_kwh", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal energyRegeneratedKwh;

    @Column(name = "odometer_km", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal odometerKm;

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
     * Retrieves battery pack id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBatteryPackId() { return batteryPackId; }
    /**
     * Performs the setBatteryPackId operation in this module.
     *
     * @param batteryPackId the batteryPackId input value
     */
    public void setBatteryPackId(String batteryPackId) { this.batteryPackId = batteryPackId; }
    /**
     * Retrieves state of charge pct data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStateOfChargePct() { return stateOfChargePct; }
    /**
     * Performs the setStateOfChargePct operation in this module.
     *
     * @param stateOfChargePct the stateOfChargePct input value
     */
    public void setStateOfChargePct(BigDecimal stateOfChargePct) { this.stateOfChargePct = stateOfChargePct; }
    /**
     * Retrieves state of health pct data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getStateOfHealthPct() { return stateOfHealthPct; }
    /**
     * Performs the setStateOfHealthPct operation in this module.
     *
     * @param stateOfHealthPct the stateOfHealthPct input value
     */
    public void setStateOfHealthPct(BigDecimal stateOfHealthPct) { this.stateOfHealthPct = stateOfHealthPct; }
    /**
     * Retrieves battery voltage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getBatteryVoltage() { return batteryVoltage; }
    /**
     * Performs the setBatteryVoltage operation in this module.
     *
     * @param batteryVoltage the batteryVoltage input value
     */
    public void setBatteryVoltage(BigDecimal batteryVoltage) { this.batteryVoltage = batteryVoltage; }
    /**
     * Retrieves battery current data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getBatteryCurrent() { return batteryCurrent; }
    /**
     * Performs the setBatteryCurrent operation in this module.
     *
     * @param batteryCurrent the batteryCurrent input value
     */
    public void setBatteryCurrent(BigDecimal batteryCurrent) { this.batteryCurrent = batteryCurrent; }
    /**
     * Retrieves battery temperature c data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getBatteryTemperatureC() { return batteryTemperatureC; }
    /**
     * Performs the setBatteryTemperatureC operation in this module.
     *
     * @param batteryTemperatureC the batteryTemperatureC input value
     */
    public void setBatteryTemperatureC(BigDecimal batteryTemperatureC) { this.batteryTemperatureC = batteryTemperatureC; }
    /**
     * Retrieves charging power kw data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getChargingPowerKw() { return chargingPowerKw; }
    /**
     * Performs the setChargingPowerKw operation in this module.
     *
     * @param chargingPowerKw the chargingPowerKw input value
     */
    public void setChargingPowerKw(BigDecimal chargingPowerKw) { this.chargingPowerKw = chargingPowerKw; }
    /**
     * Retrieves discharge power kw data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDischargePowerKw() { return dischargePowerKw; }
    /**
     * Performs the setDischargePowerKw operation in this module.
     *
     * @param dischargePowerKw the dischargePowerKw input value
     */
    public void setDischargePowerKw(BigDecimal dischargePowerKw) { this.dischargePowerKw = dischargePowerKw; }
    /**
     * Retrieves regenerative power kw data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRegenerativePowerKw() { return regenerativePowerKw; }
    /**
     * Performs the setRegenerativePowerKw operation in this module.
     *
     * @param regenerativePowerKw the regenerativePowerKw input value
     */
    public void setRegenerativePowerKw(BigDecimal regenerativePowerKw) { this.regenerativePowerKw = regenerativePowerKw; }
    /**
     * Retrieves remaining range km data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRemainingRangeKm() { return remainingRangeKm; }
    /**
     * Performs the setRemainingRangeKm operation in this module.
     *
     * @param remainingRangeKm the remainingRangeKm input value
     */
    public void setRemainingRangeKm(BigDecimal remainingRangeKm) { this.remainingRangeKm = remainingRangeKm; }
    /**
     * Retrieves energy consumed kwh data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEnergyConsumedKwh() { return energyConsumedKwh; }
    /**
     * Performs the setEnergyConsumedKwh operation in this module.
     *
     * @param energyConsumedKwh the energyConsumedKwh input value
     */
    public void setEnergyConsumedKwh(BigDecimal energyConsumedKwh) { this.energyConsumedKwh = energyConsumedKwh; }
    /**
     * Retrieves energy regenerated kwh data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEnergyRegeneratedKwh() { return energyRegeneratedKwh; }
    /**
     * Performs the setEnergyRegeneratedKwh operation in this module.
     *
     * @param energyRegeneratedKwh the energyRegeneratedKwh input value
     */
    public void setEnergyRegeneratedKwh(BigDecimal energyRegeneratedKwh) { this.energyRegeneratedKwh = energyRegeneratedKwh; }
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