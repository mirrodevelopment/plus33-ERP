/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDispatchPolicy.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDispatchPolicyController
 * Related Service   : PlatformDispatchPolicyService, PlatformDispatchPolicyServiceImpl
 * Related Repository: PlatformDispatchPolicyRepository
 * Related Entity    : PlatformDispatchPolicy
 * Related DTO       : N/A
 * Related Mapper    : PlatformDispatchPolicyMapper
 * Related DB Table  : platform_dispatch_policy
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDispatchPolicyRepository, PlatformDispatchPolicyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_dispatch_policy'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformDispatchPolicy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_dispatch_policy'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_dispatch_policy}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves dispatch strategy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDispatchStrategy() { return dispatchStrategy; }
    /**
     * Performs the setDispatchStrategy operation in this module.
     *
     * @param dispatchStrategy the dispatchStrategy input value
     */
    public void setDispatchStrategy(String dispatchStrategy) { this.dispatchStrategy = dispatchStrategy; }
    /**
     * Retrieves priority weight data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPriorityWeight() { return priorityWeight; }
    /**
     * Performs the setPriorityWeight operation in this module.
     *
     * @param priorityWeight the priorityWeight input value
     */
    public void setPriorityWeight(BigDecimal priorityWeight) { this.priorityWeight = priorityWeight; }
    /**
     * Retrieves vehicle selection strategy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getVehicleSelectionStrategy() { return vehicleSelectionStrategy; }
    /**
     * Performs the setVehicleSelectionStrategy operation in this module.
     *
     * @param vehicleSelectionStrategy the vehicleSelectionStrategy input value
     */
    public void setVehicleSelectionStrategy(String vehicleSelectionStrategy) { this.vehicleSelectionStrategy = vehicleSelectionStrategy; }
    /**
     * Retrieves driver selection strategy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDriverSelectionStrategy() { return driverSelectionStrategy; }
    /**
     * Performs the setDriverSelectionStrategy operation in this module.
     *
     * @param driverSelectionStrategy the driverSelectionStrategy input value
     */
    public void setDriverSelectionStrategy(String driverSelectionStrategy) { this.driverSelectionStrategy = driverSelectionStrategy; }
    /**
     * Retrieves optimization goal data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOptimizationGoal() { return optimizationGoal; }
    /**
     * Performs the setOptimizationGoal operation in this module.
     *
     * @param optimizationGoal the optimizationGoal input value
     */
    public void setOptimizationGoal(String optimizationGoal) { this.optimizationGoal = optimizationGoal; }
    /**
     * Retrieves planning horizon mins data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPlanningHorizonMins() { return planningHorizonMins; }
    /**
     * Performs the setPlanningHorizonMins operation in this module.
     *
     * @param planningHorizonMins the planningHorizonMins input value
     */
    public void setPlanningHorizonMins(Integer planningHorizonMins) { this.planningHorizonMins = planningHorizonMins; }
    /**
     * Retrieves a paginated list of allow partial load records.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getAllowPartialLoad() { return allowPartialLoad; }
    /**
     * Performs the setAllowPartialLoad operation in this module.
     *
     * @param allowPartialLoad the allowPartialLoad input value
     */
    public void setAllowPartialLoad(Boolean allowPartialLoad) { this.allowPartialLoad = allowPartialLoad; }
    /**
     * Retrieves a paginated list of allow split delivery records.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getAllowSplitDelivery() { return allowSplitDelivery; }
    /**
     * Performs the setAllowSplitDelivery operation in this module.
     *
     * @param allowSplitDelivery the allowSplitDelivery input value
     */
    public void setAllowSplitDelivery(Boolean allowSplitDelivery) { this.allowSplitDelivery = allowSplitDelivery; }
    /**
     * Retrieves a paginated list of allow dynamic reroute records.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getAllowDynamicReroute() { return allowDynamicReroute; }
    /**
     * Performs the setAllowDynamicReroute operation in this module.
     *
     * @param allowDynamicReroute the allowDynamicReroute input value
     */
    public void setAllowDynamicReroute(Boolean allowDynamicReroute) { this.allowDynamicReroute = allowDynamicReroute; }
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