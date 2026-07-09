/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationMonitoringMetric.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationMonitoringMetricController
 * Related Service   : IntegrationMonitoringMetricService, IntegrationMonitoringMetricServiceImpl
 * Related Repository: IntegrationMonitoringMetricRepository
 * Related Entity    : IntegrationMonitoringMetric
 * Related DTO       : N/A
 * Related Mapper    : IntegrationMonitoringMetricMapper
 * Related DB Table  : integration_monitoring_metric
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationMonitoringMetricRepository, IntegrationMonitoringMetricMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_monitoring_metric'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationMonitoringMetric}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_monitoring_metric'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_monitoring_metric}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_monitoring_metric")
public class IntegrationMonitoringMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metric_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String metricName;

    @Column(name = "metric_value", nullable = false)
    @NotNull
    private java.math.BigDecimal metricValue;

    @Column(name = "dimensions_json", columnDefinition = "TEXT")
    private String dimensionsJson;

    @Column(nullable = false, updatable = false)
    @NotNull
    private LocalDateTime timestamp = LocalDateTime.now();

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
     * Retrieves metric value data from the database.
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.math.BigDecimal getMetricValue() { return metricValue; }
    /**
     * Performs the setMetricValue operation in this module.
     *
     * @param metricValue the metricValue input value
     */
    public void setMetricValue(java.math.BigDecimal metricValue) { this.metricValue = metricValue; }
    /**
     * Retrieves dimensions json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDimensionsJson() { return dimensionsJson; }
    /**
     * Performs the setDimensionsJson operation in this module.
     *
     * @param dimensionsJson the dimensionsJson input value
     */
    public void setDimensionsJson(String dimensionsJson) { this.dimensionsJson = dimensionsJson; }
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
}