/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformCarbonFootprintLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCarbonFootprintLogController
 * Related Service   : PlatformCarbonFootprintLogService, PlatformCarbonFootprintLogServiceImpl
 * Related Repository: PlatformCarbonFootprintLogRepository
 * Related Entity    : PlatformCarbonFootprintLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformCarbonFootprintLogMapper
 * Related DB Table  : platform_carbon_footprint_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCarbonFootprintLogRepository, PlatformCarbonFootprintLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_carbon_footprint_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformCarbonFootprintLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_carbon_footprint_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_carbon_footprint_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves route id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRouteId() { return routeId; }
    /**
     * Performs the setRouteId operation in this module.
     *
     * @param routeId the routeId input value
     */
    public void setRouteId(Long routeId) { this.routeId = routeId; }
    /**
     * Retrieves fuel type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFuelType() { return fuelType; }
    /**
     * Performs the setFuelType operation in this module.
     *
     * @param fuelType the fuelType input value
     */
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    /**
     * Retrieves fuel consumption liters data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getFuelConsumptionLiters() { return fuelConsumptionLiters; }
    /**
     * Performs the setFuelConsumptionLiters operation in this module.
     *
     * @param fuelConsumptionLiters the fuelConsumptionLiters input value
     */
    public void setFuelConsumptionLiters(BigDecimal fuelConsumptionLiters) { this.fuelConsumptionLiters = fuelConsumptionLiters; }
    /**
     * Retrieves co2 kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCo2Kg() { return co2Kg; }
    /**
     * Performs the setCo2Kg operation in this module.
     *
     * @param co2Kg the co2Kg input value
     */
    public void setCo2Kg(BigDecimal co2Kg) { this.co2Kg = co2Kg; }
    /**
     * Retrieves co2e kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCo2eKg() { return co2eKg; }
    /**
     * Performs the setCo2eKg operation in this module.
     *
     * @param co2eKg the co2eKg input value
     */
    public void setCo2eKg(BigDecimal co2eKg) { this.co2eKg = co2eKg; }
    /**
     * Retrieves nox g data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getNoxG() { return noxG; }
    /**
     * Performs the setNoxG operation in this module.
     *
     * @param noxG the noxG input value
     */
    public void setNoxG(BigDecimal noxG) { this.noxG = noxG; }
    /**
     * Retrieves pm10 g data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPm10G() { return pm10G; }
    /**
     * Performs the setPm10G operation in this module.
     *
     * @param pm10G the pm10G input value
     */
    public void setPm10G(BigDecimal pm10G) { this.pm10G = pm10G; }
    /**
     * Retrieves distance km data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDistanceKm() { return distanceKm; }
    /**
     * Performs the setDistanceKm operation in this module.
     *
     * @param distanceKm the distanceKm input value
     */
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
    /**
     * Retrieves idle time mins data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getIdleTimeMins() { return idleTimeMins; }
    /**
     * Performs the setIdleTimeMins operation in this module.
     *
     * @param idleTimeMins the idleTimeMins input value
     */
    public void setIdleTimeMins(Integer idleTimeMins) { this.idleTimeMins = idleTimeMins; }
    /**
     * Retrieves average speed kmh data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAverageSpeedKmh() { return averageSpeedKmh; }
    /**
     * Performs the setAverageSpeedKmh operation in this module.
     *
     * @param averageSpeedKmh the averageSpeedKmh input value
     */
    public void setAverageSpeedKmh(BigDecimal averageSpeedKmh) { this.averageSpeedKmh = averageSpeedKmh; }
    /**
     * Retrieves calculation method data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCalculationMethod() { return calculationMethod; }
    /**
     * Performs the setCalculationMethod operation in this module.
     *
     * @param calculationMethod the calculationMethod input value
     */
    public void setCalculationMethod(String calculationMethod) { this.calculationMethod = calculationMethod; }
    /**
     * Retrieves emission factor version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEmissionFactorVersion() { return emissionFactorVersion; }
    /**
     * Performs the setEmissionFactorVersion operation in this module.
     *
     * @param emissionFactorVersion the emissionFactorVersion input value
     */
    public void setEmissionFactorVersion(String emissionFactorVersion) { this.emissionFactorVersion = emissionFactorVersion; }
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