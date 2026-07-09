/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformScalingDecision.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformScalingDecisionController
 * Related Service   : PlatformScalingDecisionService, PlatformScalingDecisionServiceImpl
 * Related Repository: PlatformScalingDecisionRepository
 * Related Entity    : PlatformScalingDecision
 * Related DTO       : N/A
 * Related Mapper    : PlatformScalingDecisionMapper
 * Related DB Table  : platform_scaling_decision
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformScalingDecisionRepository, PlatformScalingDecisionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_scaling_decision'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformScalingDecision}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_scaling_decision'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_scaling_decision}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_scaling_decision")
public class PlatformScalingDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "metric_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String metricName;

    @Column(name = "current_value", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal currentValue;

    @Column(name = "threshold_value", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal thresholdValue;

    @Column(name = "current_replicas", nullable = false)
    @NotNull
    private Integer currentReplicas;

    @Column(name = "desired_replicas", nullable = false)
    @NotNull
    private Integer desiredReplicas;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String reason;

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
     * Retrieves timestamp data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getTimestamp() { return timestamp; }
    /**
     * Performs the setTimestamp operation in this module.
     *
     * @param timestamp the timestamp input value
     */
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
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
     * Retrieves current value data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCurrentValue() { return currentValue; }
    /**
     * Performs the setCurrentValue operation in this module.
     *
     * @param currentValue the currentValue input value
     */
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
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
     * Retrieves current replicas data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getCurrentReplicas() { return currentReplicas; }
    /**
     * Performs the setCurrentReplicas operation in this module.
     *
     * @param currentReplicas the currentReplicas input value
     */
    public void setCurrentReplicas(Integer currentReplicas) { this.currentReplicas = currentReplicas; }
    /**
     * Retrieves desired replicas data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getDesiredReplicas() { return desiredReplicas; }
    /**
     * Performs the setDesiredReplicas operation in this module.
     *
     * @param desiredReplicas the desiredReplicas input value
     */
    public void setDesiredReplicas(Integer desiredReplicas) { this.desiredReplicas = desiredReplicas; }
    /**
     * Retrieves reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReason() { return reason; }
    /**
     * Performs the setReason operation in this module.
     *
     * @param reason the reason input value
     */
    public void setReason(String reason) { this.reason = reason; }
}