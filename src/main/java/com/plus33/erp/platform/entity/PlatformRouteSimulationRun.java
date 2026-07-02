package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getScenarioName() { return scenarioName; }
    public void setScenarioName(String scenarioName) { this.scenarioName = scenarioName; }
    public String getBaselineRoute() { return baselineRoute; }
    public void setBaselineRoute(String baselineRoute) { this.baselineRoute = baselineRoute; }
    public String getOptimizedRoute() { return optimizedRoute; }
    public void setOptimizedRoute(String optimizedRoute) { this.optimizedRoute = optimizedRoute; }
    public Integer getTravelTimeMins() { return travelTimeMins; }
    public void setTravelTimeMins(Integer travelTimeMins) { this.travelTimeMins = travelTimeMins; }
    public BigDecimal getFuelCost() { return fuelCost; }
    public void setFuelCost(BigDecimal fuelCost) { this.fuelCost = fuelCost; }
    public BigDecimal getCarbonOutputKg() { return carbonOutputKg; }
    public void setCarbonOutputKg(BigDecimal carbonOutputKg) { this.carbonOutputKg = carbonOutputKg; }
    public BigDecimal getDelayProbability() { return delayProbability; }
    public void setDelayProbability(BigDecimal delayProbability) { this.delayProbability = delayProbability; }
    public BigDecimal getSimulationScore() { return simulationScore; }
    public void setSimulationScore(BigDecimal simulationScore) { this.simulationScore = simulationScore; }
    public String getAlgorithmVersion() { return algorithmVersion; }
    public void setAlgorithmVersion(String algorithmVersion) { this.algorithmVersion = algorithmVersion; }
    public LocalDateTime getSimulatedAt() { return simulatedAt; }
    public void setSimulatedAt(LocalDateTime simulatedAt) { this.simulatedAt = simulatedAt; }
}