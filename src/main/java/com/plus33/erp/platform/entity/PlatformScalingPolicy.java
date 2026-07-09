/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformScalingPolicy.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformScalingPolicyController
 * Related Service   : PlatformScalingPolicyService, PlatformScalingPolicyServiceImpl
 * Related Repository: PlatformScalingPolicyRepository
 * Related Entity    : PlatformScalingPolicy
 * Related DTO       : N/A
 * Related Mapper    : PlatformScalingPolicyMapper
 * Related DB Table  : platform_scaling_policy
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformScalingPolicyRepository, PlatformScalingPolicyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_scaling_policy'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformScalingPolicy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_scaling_policy'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_scaling_policy}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_scaling_policy")
public class PlatformScalingPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "metric_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String metricName;

    @Column(name = "threshold_value", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal thresholdValue;

    @Column(name = "min_replicas", nullable = false)
    @NotNull
    private Integer minReplicas = 1;

    @Column(name = "max_replicas", nullable = false)
    @NotNull
    private Integer maxReplicas = 10;

    @Column(name = "cooldown_seconds", nullable = false)
    @NotNull
    private Integer cooldownSeconds = 300;

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
     * Retrieves metric name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMetricName() { return metricName; }
    /**
     * Performs the setMetricName operation in this module.
     *
     * @param metricName the metricName input value
     */
    public void setMetricName(String metricName) { this.metricName = metricName; }
    /**
     * Retrieves threshold value data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getThresholdValue() { return thresholdValue; }
    /**
     * Performs the setThresholdValue operation in this module.
     *
     * @param thresholdValue the thresholdValue input value
     */
    public void setThresholdValue(BigDecimal thresholdValue) { this.thresholdValue = thresholdValue; }
    /**
     * Retrieves min replicas data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getMinReplicas() { return minReplicas; }
    /**
     * Performs the setMinReplicas operation in this module.
     *
     * @param minReplicas the minReplicas input value
     */
    public void setMinReplicas(Integer minReplicas) { this.minReplicas = minReplicas; }
    /**
     * Retrieves max replicas data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getMaxReplicas() { return maxReplicas; }
    /**
     * Performs the setMaxReplicas operation in this module.
     *
     * @param maxReplicas the maxReplicas input value
     */
    public void setMaxReplicas(Integer maxReplicas) { this.maxReplicas = maxReplicas; }
    /**
     * Retrieves cooldown seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getCooldownSeconds() { return cooldownSeconds; }
    /**
     * Performs the setCooldownSeconds operation in this module.
     *
     * @param cooldownSeconds the cooldownSeconds input value
     */
    public void setCooldownSeconds(Integer cooldownSeconds) { this.cooldownSeconds = cooldownSeconds; }
}