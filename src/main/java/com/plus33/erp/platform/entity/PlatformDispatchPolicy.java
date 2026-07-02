package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_dispatch_policy")
public class PlatformDispatchPolicy {
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

    @Column(name = "dispatch_strategy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String dispatchStrategy; // NearestVehicle, LowestCost, CarbonOptimized

    @Column(name = "priority_weight", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal priorityWeight;

    @Column(name = "vehicle_selection_strategy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String vehicleSelectionStrategy;

    @Column(name = "driver_selection_strategy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String driverSelectionStrategy;

    @Column(name = "optimization_goal", nullable = false)
    @NotNull
    @Size(max = 100)
    private String optimizationGoal;

    @Column(name = "planning_horizon_mins", nullable = false)
    @NotNull
    private Integer planningHorizonMins;

    @Column(name = "allow_partial_load", nullable = false)
    @NotNull
    private Boolean allowPartialLoad = false;

    @Column(name = "allow_split_delivery", nullable = false)
    @NotNull
    private Boolean allowSplitDelivery = false;

    @Column(name = "allow_dynamic_reroute", nullable = false)
    @NotNull
    private Boolean allowDynamicReroute = true;

    @Column(nullable = false)
    @NotNull
    private Boolean enabled = true;

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getPolicyCode() { return policyCode; }
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    public String getDispatchStrategy() { return dispatchStrategy; }
    public void setDispatchStrategy(String dispatchStrategy) { this.dispatchStrategy = dispatchStrategy; }
    public BigDecimal getPriorityWeight() { return priorityWeight; }
    public void setPriorityWeight(BigDecimal priorityWeight) { this.priorityWeight = priorityWeight; }
    public String getVehicleSelectionStrategy() { return vehicleSelectionStrategy; }
    public void setVehicleSelectionStrategy(String vehicleSelectionStrategy) { this.vehicleSelectionStrategy = vehicleSelectionStrategy; }
    public String getDriverSelectionStrategy() { return driverSelectionStrategy; }
    public void setDriverSelectionStrategy(String driverSelectionStrategy) { this.driverSelectionStrategy = driverSelectionStrategy; }
    public String getOptimizationGoal() { return optimizationGoal; }
    public void setOptimizationGoal(String optimizationGoal) { this.optimizationGoal = optimizationGoal; }
    public Integer getPlanningHorizonMins() { return planningHorizonMins; }
    public void setPlanningHorizonMins(Integer planningHorizonMins) { this.planningHorizonMins = planningHorizonMins; }
    public Boolean getAllowPartialLoad() { return allowPartialLoad; }
    public void setAllowPartialLoad(Boolean allowPartialLoad) { this.allowPartialLoad = allowPartialLoad; }
    public Boolean getAllowSplitDelivery() { return allowSplitDelivery; }
    public void setAllowSplitDelivery(Boolean allowSplitDelivery) { this.allowSplitDelivery = allowSplitDelivery; }
    public Boolean getAllowDynamicReroute() { return allowDynamicReroute; }
    public void setAllowDynamicReroute(Boolean allowDynamicReroute) { this.allowDynamicReroute = allowDynamicReroute; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}