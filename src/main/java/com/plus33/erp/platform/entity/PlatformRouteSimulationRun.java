/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformRouteSimulationRun.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRouteSimulationRunController
 * Related Service   : PlatformRouteSimulationRunService, PlatformRouteSimulationRunServiceImpl
 * Related Repository: PlatformRouteSimulationRunRepository
 * Related Entity    : PlatformRouteSimulationRun
 * Related DTO       : N/A
 * Related Mapper    : PlatformRouteSimulationRunMapper
 * Related DB Table  : platform_route_simulation_run
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRouteSimulationRunRepository, PlatformRouteSimulationRunMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_route_simulation_run'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformRouteSimulationRun}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_route_simulation_run'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_route_simulation_run}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_route_simulation_run")
public class PlatformRouteSimulationRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scenario_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String scenarioName;

    @Column(name = "baseline_route", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String baselineRoute;

    @Column(name = "optimized_route", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String optimizedRoute;

    @Column(name = "travel_time_mins", nullable = false)
    @NotNull
    private Integer travelTimeMins;

    @Column(name = "fuel_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal fuelCost;

    @Column(name = "carbon_output_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal carbonOutputKg;

    @Column(name = "delay_probability", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal delayProbability;

    @Column(name = "simulation_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal simulationScore;

    @Column(name = "algorithm_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String algorithmVersion;

    @Column(name = "simulated_at", nullable = false)
    @NotNull
    private LocalDateTime simulatedAt = LocalDateTime.now();

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
     * Retrieves scenario name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getScenarioName() { return scenarioName; }
    /**
     * Performs the setScenarioName operation in this module.
     *
     * @param scenarioName the scenarioName input value
     */
    public void setScenarioName(String scenarioName) { this.scenarioName = scenarioName; }
    /**
     * Retrieves baseline route data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBaselineRoute() { return baselineRoute; }
    /**
     * Performs the setBaselineRoute operation in this module.
     *
     * @param baselineRoute the baselineRoute input value
     */
    public void setBaselineRoute(String baselineRoute) { this.baselineRoute = baselineRoute; }
    /**
     * Retrieves optimized route data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOptimizedRoute() { return optimizedRoute; }
    /**
     * Performs the setOptimizedRoute operation in this module.
     *
     * @param optimizedRoute the optimizedRoute input value
     */
    public void setOptimizedRoute(String optimizedRoute) { this.optimizedRoute = optimizedRoute; }
    /**
     * Retrieves travel time mins data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getTravelTimeMins() { return travelTimeMins; }
    /**
     * Performs the setTravelTimeMins operation in this module.
     *
     * @param travelTimeMins the travelTimeMins input value
     */
    public void setTravelTimeMins(Integer travelTimeMins) { this.travelTimeMins = travelTimeMins; }
    /**
     * Retrieves fuel cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getFuelCost() { return fuelCost; }
    /**
     * Performs the setFuelCost operation in this module.
     *
     * @param fuelCost the fuelCost input value
     */
    public void setFuelCost(BigDecimal fuelCost) { this.fuelCost = fuelCost; }
    /**
     * Retrieves carbon output kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCarbonOutputKg() { return carbonOutputKg; }
    /**
     * Performs the setCarbonOutputKg operation in this module.
     *
     * @param carbonOutputKg the carbonOutputKg input value
     */
    public void setCarbonOutputKg(BigDecimal carbonOutputKg) { this.carbonOutputKg = carbonOutputKg; }
    /**
     * Retrieves delay probability data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDelayProbability() { return delayProbability; }
    /**
     * Performs the setDelayProbability operation in this module.
     *
     * @param delayProbability the delayProbability input value
     */
    public void setDelayProbability(BigDecimal delayProbability) { this.delayProbability = delayProbability; }
    /**
     * Retrieves simulation score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSimulationScore() { return simulationScore; }
    /**
     * Performs the setSimulationScore operation in this module.
     *
     * @param simulationScore the simulationScore input value
     */
    public void setSimulationScore(BigDecimal simulationScore) { this.simulationScore = simulationScore; }
    /**
     * Retrieves algorithm version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAlgorithmVersion() { return algorithmVersion; }
    /**
     * Performs the setAlgorithmVersion operation in this module.
     *
     * @param algorithmVersion the algorithmVersion input value
     */
    public void setAlgorithmVersion(String algorithmVersion) { this.algorithmVersion = algorithmVersion; }
    /**
     * Retrieves simulated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getSimulatedAt() { return simulatedAt; }
    /**
     * Performs the setSimulatedAt operation in this module.
     *
     * @param simulatedAt the simulatedAt input value
     */
    public void setSimulatedAt(LocalDateTime simulatedAt) { this.simulatedAt = simulatedAt; }
}