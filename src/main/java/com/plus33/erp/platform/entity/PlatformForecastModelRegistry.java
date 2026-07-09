/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformForecastModelRegistry.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformForecastModelRegistryController
 * Related Service   : PlatformForecastModelRegistryService, PlatformForecastModelRegistryServiceImpl
 * Related Repository: PlatformForecastModelRegistryRepository
 * Related Entity    : PlatformForecastModelRegistry
 * Related DTO       : N/A
 * Related Mapper    : PlatformForecastModelRegistryMapper
 * Related DB Table  : platform_forecast_model_registry
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformForecastModelRegistryRepository, PlatformForecastModelRegistryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_forecast_model_registry'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformForecastModelRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_forecast_model_registry'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_forecast_model_registry}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_forecast_model_registry")
public class PlatformForecastModelRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "model_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String modelCode;

    @Column(name = "accuracy_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal accuracyScore;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

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
     * Retrieves model code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getModelCode() { return modelCode; }
    /**
     * Performs the setModelCode operation in this module.
     *
     * @param modelCode the modelCode input value
     */
    public void setModelCode(String modelCode) { this.modelCode = modelCode; }
    /**
     * Retrieves accuracy score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAccuracyScore() { return accuracyScore; }
    /**
     * Performs the setAccuracyScore operation in this module.
     *
     * @param accuracyScore the accuracyScore input value
     */
    public void setAccuracyScore(BigDecimal accuracyScore) { this.accuracyScore = accuracyScore; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
}