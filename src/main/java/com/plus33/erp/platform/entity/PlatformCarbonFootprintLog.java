package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_carbon_footprint_log")
public class PlatformCarbonFootprintLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(name = "route_id", nullable = false)
    @NotNull
    private Long routeId;

    @Column(name = "fuel_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String fuelType;

    @Column(name = "fuel_consumption_liters", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal fuelConsumptionLiters;

    @Column(name = "co2_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal co2Kg;

    @Column(name = "co2e_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal co2eKg;

    @Column(name = "nox_g", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal noxG;

    @Column(name = "pm10_g", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal pm10G;

    @Column(name = "distance_km", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal distanceKm;

    @Column(name = "idle_time_mins", nullable = false)
    @NotNull
    private Integer idleTimeMins;

    @Column(name = "average_speed_kmh", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal averageSpeedKmh;

    @Column(name = "calculation_method", nullable = false)
    @NotNull
    @Size(max = 100)
    private String calculationMethod;

    @Column(name = "emission_factor_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String emissionFactorVersion;

    @Column(name = "logged_at", nullable = false)
    @NotNull
    private LocalDateTime loggedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public BigDecimal getFuelConsumptionLiters() { return fuelConsumptionLiters; }
    public void setFuelConsumptionLiters(BigDecimal fuelConsumptionLiters) { this.fuelConsumptionLiters = fuelConsumptionLiters; }
    public BigDecimal getCo2Kg() { return co2Kg; }
    public void setCo2Kg(BigDecimal co2Kg) { this.co2Kg = co2Kg; }
    public BigDecimal getCo2eKg() { return co2eKg; }
    public void setCo2eKg(BigDecimal co2eKg) { this.co2eKg = co2eKg; }
    public BigDecimal getNoxG() { return noxG; }
    public void setNoxG(BigDecimal noxG) { this.noxG = noxG; }
    public BigDecimal getPm10G() { return pm10G; }
    public void setPm10G(BigDecimal pm10G) { this.pm10G = pm10G; }
    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
    public Integer getIdleTimeMins() { return idleTimeMins; }
    public void setIdleTimeMins(Integer idleTimeMins) { this.idleTimeMins = idleTimeMins; }
    public BigDecimal getAverageSpeedKmh() { return averageSpeedKmh; }
    public void setAverageSpeedKmh(BigDecimal averageSpeedKmh) { this.averageSpeedKmh = averageSpeedKmh; }
    public String getCalculationMethod() { return calculationMethod; }
    public void setCalculationMethod(String calculationMethod) { this.calculationMethod = calculationMethod; }
    public String getEmissionFactorVersion() { return emissionFactorVersion; }
    public void setEmissionFactorVersion(String emissionFactorVersion) { this.emissionFactorVersion = emissionFactorVersion; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}