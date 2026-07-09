/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformRouteOptimizationPolicy.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRouteOptimizationPolicyController
 * Related Service   : PlatformRouteOptimizationPolicyService, PlatformRouteOptimizationPolicyServiceImpl
 * Related Repository: PlatformRouteOptimizationPolicyRepository
 * Related Entity    : PlatformRouteOptimizationPolicy
 * Related DTO       : N/A
 * Related Mapper    : PlatformRouteOptimizationPolicyMapper
 * Related DB Table  : platform_route_optimization_policy
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRouteOptimizationPolicyRepository, PlatformRouteOptimizationPolicyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_route_optimization_policy'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformRouteOptimizationPolicy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_route_optimization_policy'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_route_optimization_policy}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_route_optimization_policy")
public class PlatformRouteOptimizationPolicy {
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

    @Column(name = "policy_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String policyName;

    @Column(name = "optimization_strategy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String optimizationStrategy; // ShortestDistance, MinCost, EcoFriendly

    @Column(name = "vehicle_constraints", columnDefinition = "TEXT")
    private String vehicleConstraints;

    @Column(name = "driver_constraints", columnDefinition = "TEXT")
    private String driverConstraints;

    @Column(nullable = false)
    @NotNull
    private Integer priority = 0;

    @Column(name = "time_window_minutes", nullable = false)
    @NotNull
    private Integer timeWindowMinutes;

    @Column(name = "maximum_distance_km", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal maximumDistanceKm;

    @Column(name = "maximum_duration_mins", nullable = false)
    @NotNull
    private Integer maximumDurationMins;

    @Column(name = "maximum_load_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal maximumLoadKg;

    @Column(name = "traffic_weight", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal trafficWeight;

    @Column(name = "fuel_weight", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal fuelWeight;

    @Column(name = "carbon_weight", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal carbonWeight;

    @Column(name = "cost_weight", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal costWeight;

    @Column(nullable = false)
    @NotNull
    private Boolean enabled = true;

    @Column(name = "created_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves policy name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPolicyName() { return policyName; }
    /**
     * Performs the setPolicyName operation in this module.
     *
     * @param policyName the policyName input value
     */
    public void setPolicyName(String policyName) { this.policyName = policyName; }
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
     * Retrieves vehicle constraints data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getVehicleConstraints() { return vehicleConstraints; }
    /**
     * Performs the setVehicleConstraints operation in this module.
     *
     * @param vehicleConstraints the vehicleConstraints input value
     */
    public void setVehicleConstraints(String vehicleConstraints) { this.vehicleConstraints = vehicleConstraints; }
    /**
     * Retrieves driver constraints data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDriverConstraints() { return driverConstraints; }
    /**
     * Performs the setDriverConstraints operation in this module.
     *
     * @param driverConstraints the driverConstraints input value
     */
    public void setDriverConstraints(String driverConstraints) { this.driverConstraints = driverConstraints; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(Integer priority) { this.priority = priority; }
    /**
     * Retrieves time window minutes data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getTimeWindowMinutes() { return timeWindowMinutes; }
    /**
     * Performs the setTimeWindowMinutes operation in this module.
     *
     * @param timeWindowMinutes the timeWindowMinutes input value
     */
    public void setTimeWindowMinutes(Integer timeWindowMinutes) { this.timeWindowMinutes = timeWindowMinutes; }
    /**
     * Retrieves maximum distance km data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaximumDistanceKm() { return maximumDistanceKm; }
    /**
     * Performs the setMaximumDistanceKm operation in this module.
     *
     * @param maximumDistanceKm the maximumDistanceKm input value
     */
    public void setMaximumDistanceKm(BigDecimal maximumDistanceKm) { this.maximumDistanceKm = maximumDistanceKm; }
    /**
     * Retrieves maximum duration mins data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getMaximumDurationMins() { return maximumDurationMins; }
    /**
     * Performs the setMaximumDurationMins operation in this module.
     *
     * @param maximumDurationMins the maximumDurationMins input value
     */
    public void setMaximumDurationMins(Integer maximumDurationMins) { this.maximumDurationMins = maximumDurationMins; }
    /**
     * Retrieves maximum load kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaximumLoadKg() { return maximumLoadKg; }
    /**
     * Performs the setMaximumLoadKg operation in this module.
     *
     * @param maximumLoadKg the maximumLoadKg input value
     */
    public void setMaximumLoadKg(BigDecimal maximumLoadKg) { this.maximumLoadKg = maximumLoadKg; }
    /**
     * Retrieves traffic weight data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTrafficWeight() { return trafficWeight; }
    /**
     * Performs the setTrafficWeight operation in this module.
     *
     * @param trafficWeight the trafficWeight input value
     */
    public void setTrafficWeight(BigDecimal trafficWeight) { this.trafficWeight = trafficWeight; }
    /**
     * Retrieves fuel weight data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getFuelWeight() { return fuelWeight; }
    /**
     * Performs the setFuelWeight operation in this module.
     *
     * @param fuelWeight the fuelWeight input value
     */
    public void setFuelWeight(BigDecimal fuelWeight) { this.fuelWeight = fuelWeight; }
    /**
     * Retrieves carbon weight data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCarbonWeight() { return carbonWeight; }
    /**
     * Performs the setCarbonWeight operation in this module.
     *
     * @param carbonWeight the carbonWeight input value
     */
    public void setCarbonWeight(BigDecimal carbonWeight) { this.carbonWeight = carbonWeight; }
    /**
     * Retrieves cost weight data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCostWeight() { return costWeight; }
    /**
     * Performs the setCostWeight operation in this module.
     *
     * @param costWeight the costWeight input value
     */
    public void setCostWeight(BigDecimal costWeight) { this.costWeight = costWeight; }
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
     * Retrieves created by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCreatedBy() { return createdBy; }
    /**
     * Performs the setCreatedBy operation in this module.
     *
     * @param createdBy the createdBy input value
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}