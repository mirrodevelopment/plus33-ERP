package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public String getBatteryPackId() { return batteryPackId; }
    public void setBatteryPackId(String batteryPackId) { this.batteryPackId = batteryPackId; }
    public BigDecimal getStateOfChargePct() { return stateOfChargePct; }
    public void setStateOfChargePct(BigDecimal stateOfChargePct) { this.stateOfChargePct = stateOfChargePct; }
    public BigDecimal getStateOfHealthPct() { return stateOfHealthPct; }
    public void setStateOfHealthPct(BigDecimal stateOfHealthPct) { this.stateOfHealthPct = stateOfHealthPct; }
    public BigDecimal getBatteryVoltage() { return batteryVoltage; }
    public void setBatteryVoltage(BigDecimal batteryVoltage) { this.batteryVoltage = batteryVoltage; }
    public BigDecimal getBatteryCurrent() { return batteryCurrent; }
    public void setBatteryCurrent(BigDecimal batteryCurrent) { this.batteryCurrent = batteryCurrent; }
    public BigDecimal getBatteryTemperatureC() { return batteryTemperatureC; }
    public void setBatteryTemperatureC(BigDecimal batteryTemperatureC) { this.batteryTemperatureC = batteryTemperatureC; }
    public BigDecimal getChargingPowerKw() { return chargingPowerKw; }
    public void setChargingPowerKw(BigDecimal chargingPowerKw) { this.chargingPowerKw = chargingPowerKw; }
    public BigDecimal getDischargePowerKw() { return dischargePowerKw; }
    public void setDischargePowerKw(BigDecimal dischargePowerKw) { this.dischargePowerKw = dischargePowerKw; }
    public BigDecimal getRegenerativePowerKw() { return regenerativePowerKw; }
    public void setRegenerativePowerKw(BigDecimal regenerativePowerKw) { this.regenerativePowerKw = regenerativePowerKw; }
    public BigDecimal getRemainingRangeKm() { return remainingRangeKm; }
    public void setRemainingRangeKm(BigDecimal remainingRangeKm) { this.remainingRangeKm = remainingRangeKm; }
    public BigDecimal getEnergyConsumedKwh() { return energyConsumedKwh; }
    public void setEnergyConsumedKwh(BigDecimal energyConsumedKwh) { this.energyConsumedKwh = energyConsumedKwh; }
    public BigDecimal getEnergyRegeneratedKwh() { return energyRegeneratedKwh; }
    public void setEnergyRegeneratedKwh(BigDecimal energyRegeneratedKwh) { this.energyRegeneratedKwh = energyRegeneratedKwh; }
    public BigDecimal getOdometerKm() { return odometerKm; }
    public void setOdometerKm(BigDecimal odometerKm) { this.odometerKm = odometerKm; }
    public BigDecimal getGpsLatitude() { return gpsLatitude; }
    public void setGpsLatitude(BigDecimal gpsLatitude) { this.gpsLatitude = gpsLatitude; }
    public BigDecimal getGpsLongitude() { return gpsLongitude; }
    public void setGpsLongitude(BigDecimal gpsLongitude) { this.gpsLongitude = gpsLongitude; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}