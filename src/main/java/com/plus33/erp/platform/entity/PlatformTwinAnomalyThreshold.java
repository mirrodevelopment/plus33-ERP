/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTwinAnomalyThreshold.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTwinAnomalyThresholdController
 * Related Service   : PlatformTwinAnomalyThresholdService, PlatformTwinAnomalyThresholdServiceImpl
 * Related Repository: PlatformTwinAnomalyThresholdRepository
 * Related Entity    : PlatformTwinAnomalyThreshold
 * Related DTO       : N/A
 * Related Mapper    : PlatformTwinAnomalyThresholdMapper
 * Related DB Table  : platform_twin_anomaly_threshold
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTwinAnomalyThresholdRepository, PlatformTwinAnomalyThresholdMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_twin_anomaly_threshold'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTwinAnomalyThreshold}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_twin_anomaly_threshold'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_twin_anomaly_threshold}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_twin_anomaly_threshold")
public class PlatformTwinAnomalyThreshold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "instance_id", nullable = false)
    @NotNull
    private Long instanceId;

    @Column(name = "metric_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String metricName;

    @Column(name = "min_value", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal minValue;

    @Column(name = "max_value", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal maxValue;

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
     * Retrieves instance id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getInstanceId() { return instanceId; }
    /**
     * Performs the setInstanceId operation in this module.
     *
     * @param instanceId the instanceId input value
     */
    public void setInstanceId(Long instanceId) { this.instanceId = instanceId; }
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
     * Retrieves min value data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMinValue() { return minValue; }
    /**
     * Performs the setMinValue operation in this module.
     *
     * @param minValue the minValue input value
     */
    public void setMinValue(BigDecimal minValue) { this.minValue = minValue; }
    /**
     * Retrieves max value data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaxValue() { return maxValue; }
    /**
     * Performs the setMaxValue operation in this module.
     *
     * @param maxValue the maxValue input value
     */
    public void setMaxValue(BigDecimal maxValue) { this.maxValue = maxValue; }
}