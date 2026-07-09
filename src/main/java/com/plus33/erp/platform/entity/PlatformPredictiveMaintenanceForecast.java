/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformPredictiveMaintenanceForecast.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformPredictiveMaintenanceForecastController
 * Related Service   : PlatformPredictiveMaintenanceForecastService, PlatformPredictiveMaintenanceForecastServiceImpl
 * Related Repository: PlatformPredictiveMaintenanceForecastRepository
 * Related Entity    : PlatformPredictiveMaintenanceForecast
 * Related DTO       : N/A
 * Related Mapper    : PlatformPredictiveMaintenanceForecastMapper
 * Related DB Table  : platform_predictive_maintenance_forecast
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformPredictiveMaintenanceForecastRepository, PlatformPredictiveMaintenanceForecastMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_predictive_maintenance_forecast'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformPredictiveMaintenanceForecast}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_predictive_maintenance_forecast'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_predictive_maintenance_forecast}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_predictive_maintenance_forecast")
public class PlatformPredictiveMaintenanceForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    @NotNull
    private Long modelId;

    @Column(name = "twin_instance_id", nullable = false)
    @NotNull
    private Long twinInstanceId;

    @Column(name = "failure_probability", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal failureProbability;

    @Column(name = "expected_failure_at", nullable = false)
    @NotNull
    private LocalDateTime expectedFailureAt;

    @Column(name = "generated_at", nullable = false)
    @NotNull
    private LocalDateTime generatedAt = LocalDateTime.now();

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
     * Retrieves model id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getModelId() { return modelId; }
    /**
     * Performs the setModelId operation in this module.
     *
     * @param modelId the modelId input value
     */
    public void setModelId(Long modelId) { this.modelId = modelId; }
    /**
     * Retrieves twin instance id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTwinInstanceId() { return twinInstanceId; }
    /**
     * Performs the setTwinInstanceId operation in this module.
     *
     * @param twinInstanceId the twinInstanceId input value
     */
    public void setTwinInstanceId(Long twinInstanceId) { this.twinInstanceId = twinInstanceId; }
    /**
     * Retrieves failure probability data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getFailureProbability() { return failureProbability; }
    /**
     * Performs the setFailureProbability operation in this module.
     *
     * @param failureProbability the failureProbability input value
     */
    public void setFailureProbability(BigDecimal failureProbability) { this.failureProbability = failureProbability; }
    /**
     * Retrieves expected failure at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getExpectedFailureAt() { return expectedFailureAt; }
    /**
     * Performs the setExpectedFailureAt operation in this module.
     *
     * @param expectedFailureAt the expectedFailureAt input value
     */
    public void setExpectedFailureAt(LocalDateTime expectedFailureAt) { this.expectedFailureAt = expectedFailureAt; }
    /**
     * Retrieves generated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    /**
     * Performs the setGeneratedAt operation in this module.
     *
     * @param generatedAt the generatedAt input value
     */
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}