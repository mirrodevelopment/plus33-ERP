/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformCapacityForecast.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCapacityForecastController
 * Related Service   : PlatformCapacityForecastService, PlatformCapacityForecastServiceImpl
 * Related Repository: PlatformCapacityForecastRepository
 * Related Entity    : PlatformCapacityForecast
 * Related DTO       : N/A
 * Related Mapper    : PlatformCapacityForecastMapper
 * Related DB Table  : platform_capacity_forecast
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCapacityForecastRepository, PlatformCapacityForecastMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_capacity_forecast'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformCapacityForecast}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_capacity_forecast'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_capacity_forecast}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_capacity_forecast")
public class PlatformCapacityForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metric_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String metricName;

    @Column(name = "forecast_value", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal forecastValue;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidence;

    @Column(name = "target_time", nullable = false)
    @NotNull
    private LocalDateTime targetTime;

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
     * Retrieves forecast value data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getForecastValue() { return forecastValue; }
    /**
     * Performs the setForecastValue operation in this module.
     *
     * @param forecastValue the forecastValue input value
     */
    public void setForecastValue(BigDecimal forecastValue) { this.forecastValue = forecastValue; }
    /**
     * Retrieves confidence data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getConfidence() { return confidence; }
    /**
     * Performs the setConfidence operation in this module.
     *
     * @param confidence the confidence input value
     */
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }
    /**
     * Retrieves target time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getTargetTime() { return targetTime; }
    /**
     * Performs the setTargetTime operation in this module.
     *
     * @param targetTime the targetTime input value
     */
    public void setTargetTime(LocalDateTime targetTime) { this.targetTime = targetTime; }
}