package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getPolicyCode() { return policyCode; }
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    public String getPolicyName() { return policyName; }
    public void setPolicyName(String policyName) { this.policyName = policyName; }
    public String getOptimizationStrategy() { return optimizationStrategy; }
    public void setOptimizationStrategy(String optimizationStrategy) { this.optimizationStrategy = optimizationStrategy; }
    public String getVehicleConstraints() { return vehicleConstraints; }
    public void setVehicleConstraints(String vehicleConstraints) { this.vehicleConstraints = vehicleConstraints; }
    public String getDriverConstraints() { return driverConstraints; }
    public void setDriverConstraints(String driverConstraints) { this.driverConstraints = driverConstraints; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Integer getTimeWindowMinutes() { return timeWindowMinutes; }
    public void setTimeWindowMinutes(Integer timeWindowMinutes) { this.timeWindowMinutes = timeWindowMinutes; }
    public BigDecimal getMaximumDistanceKm() { return maximumDistanceKm; }
    public void setMaximumDistanceKm(BigDecimal maximumDistanceKm) { this.maximumDistanceKm = maximumDistanceKm; }
    public Integer getMaximumDurationMins() { return maximumDurationMins; }
    public void setMaximumDurationMins(Integer maximumDurationMins) { this.maximumDurationMins = maximumDurationMins; }
    public BigDecimal getMaximumLoadKg() { return maximumLoadKg; }
    public void setMaximumLoadKg(BigDecimal maximumLoadKg) { this.maximumLoadKg = maximumLoadKg; }
    public BigDecimal getTrafficWeight() { return trafficWeight; }
    public void setTrafficWeight(BigDecimal trafficWeight) { this.trafficWeight = trafficWeight; }
    public BigDecimal getFuelWeight() { return fuelWeight; }
    public void setFuelWeight(BigDecimal fuelWeight) { this.fuelWeight = fuelWeight; }
    public BigDecimal getCarbonWeight() { return carbonWeight; }
    public void setCarbonWeight(BigDecimal carbonWeight) { this.carbonWeight = carbonWeight; }
    public BigDecimal getCostWeight() { return costWeight; }
    public void setCostWeight(BigDecimal costWeight) { this.costWeight = costWeight; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}