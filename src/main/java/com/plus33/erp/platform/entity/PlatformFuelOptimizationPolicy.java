/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformFuelOptimizationPolicy.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformFuelOptimizationPolicyController
 * Related Service   : PlatformFuelOptimizationPolicyService, PlatformFuelOptimizationPolicyServiceImpl
 * Related Repository: PlatformFuelOptimizationPolicyRepository
 * Related Entity    : PlatformFuelOptimizationPolicy
 * Related DTO       : N/A
 * Related Mapper    : PlatformFuelOptimizationPolicyMapper
 * Related DB Table  : platform_fuel_optimization_policy
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformFuelOptimizationPolicyRepository, PlatformFuelOptimizationPolicyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_fuel_optimization_policy'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformFuelOptimizationPolicy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_fuel_optimization_policy'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_fuel_optimization_policy}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_fuel_optimization_policy")
public class PlatformFuelOptimizationPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "policy_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String policyCode;

    @Column(name = "optimization_strategy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String optimizationStrategy; // LowRPM, EcoSpeed, CargoAware

    @Column(name = "vehicle_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String vehicleType;

    @Column(name = "engine_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String engineType;

    @Column(name = "fuel_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String fuelType;

    @Column(name = "idle_limit_seconds", nullable = false)
    @NotNull
    private Integer idleLimitSeconds;

    @Column(name = "target_fuel_consumption", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal targetFuelConsumption;

    @Column(name = "eco_speed_min", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal ecoSpeedMin;

    @Column(name = "eco_speed_max", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal ecoSpeedMax;

    @Column(nullable = false)
    @NotNull
    private Boolean enabled = true;

    @Column(name = "effective_from", nullable = false)
    @NotNull
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to", nullable = false)
    @NotNull
    private LocalDateTime effectiveTo;

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
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
    /**
     * Retrieves policy code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPolicyCode() { return policyCode; }
    /**
     * Performs the setPolicyCode operation in this module.
     *
     * @param policyCode the policyCode input value
     */
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    /**
     * Retrieves optimization strategy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOptimizationStrategy() { return optimizationStrategy; }
    /**
     * Performs the setOptimizationStrategy operation in this module.
     *
     * @param optimizationStrategy the optimizationStrategy input value
     */
    public void setOptimizationStrategy(String optimizationStrategy) { this.optimizationStrategy = optimizationStrategy; }
    /**
     * Retrieves vehicle type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getVehicleType() { return vehicleType; }
    /**
     * Performs the setVehicleType operation in this module.
     *
     * @param vehicleType the vehicleType input value
     */
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    /**
     * Retrieves engine type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEngineType() { return engineType; }
    /**
     * Performs the setEngineType operation in this module.
     *
     * @param engineType the engineType input value
     */
    public void setEngineType(String engineType) { this.engineType = engineType; }
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
     * Retrieves idle limit seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getIdleLimitSeconds() { return idleLimitSeconds; }
    /**
     * Performs the setIdleLimitSeconds operation in this module.
     *
     * @param idleLimitSeconds the idleLimitSeconds input value
     */
    public void setIdleLimitSeconds(Integer idleLimitSeconds) { this.idleLimitSeconds = idleLimitSeconds; }
    /**
     * Retrieves target fuel consumption data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTargetFuelConsumption() { return targetFuelConsumption; }
    /**
     * Performs the setTargetFuelConsumption operation in this module.
     *
     * @param targetFuelConsumption the targetFuelConsumption input value
     */
    public void setTargetFuelConsumption(BigDecimal targetFuelConsumption) { this.targetFuelConsumption = targetFuelConsumption; }
    /**
     * Retrieves eco speed min data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEcoSpeedMin() { return ecoSpeedMin; }
    /**
     * Performs the setEcoSpeedMin operation in this module.
     *
     * @param ecoSpeedMin the ecoSpeedMin input value
     */
    public void setEcoSpeedMin(BigDecimal ecoSpeedMin) { this.ecoSpeedMin = ecoSpeedMin; }
    /**
     * Retrieves eco speed max data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEcoSpeedMax() { return ecoSpeedMax; }
    /**
     * Performs the setEcoSpeedMax operation in this module.
     *
     * @param ecoSpeedMax the ecoSpeedMax input value
     */
    public void setEcoSpeedMax(BigDecimal ecoSpeedMax) { this.ecoSpeedMax = ecoSpeedMax; }
    /**
     * Retrieves enabled data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getEnabled() { return enabled; }
    /**
     * Performs the setEnabled operation in this module.
     *
     * @param enabled the enabled input value
     */
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    /**
     * Retrieves effective from data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getEffectiveFrom() { return effectiveFrom; }
    /**
     * Performs the setEffectiveFrom operation in this module.
     *
     * @param effectiveFrom the effectiveFrom input value
     */
    public void setEffectiveFrom(LocalDateTime effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    /**
     * Retrieves effective to data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getEffectiveTo() { return effectiveTo; }
    /**
     * Performs the setEffectiveTo operation in this module.
     *
     * @param effectiveTo the effectiveTo input value
     */
    public void setEffectiveTo(LocalDateTime effectiveTo) { this.effectiveTo = effectiveTo; }
}