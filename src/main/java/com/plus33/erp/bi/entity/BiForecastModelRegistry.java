/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiForecastModelRegistry.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiForecastModelRegistryController
 * Related Service   : BiForecastModelRegistryService, BiForecastModelRegistryServiceImpl
 * Related Repository: BiForecastModelRegistryRepository
 * Related Entity    : BiForecastModelRegistry
 * Related DTO       : N/A
 * Related Mapper    : BiForecastModelRegistryMapper
 * Related DB Table  : bi_forecast_model_registry
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiForecastModelRegistryRepository, BiForecastModelRegistryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_forecast_model_registry'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiForecastModelRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_forecast_model_registry'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_forecast_model_registry}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_forecast_model_registry")
public class BiForecastModelRegistry {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "model_code", nullable = false, unique = true, length = 100) private String modelCode;
    @Column(name = "model_name", nullable = false, length = 200) private String modelName;
    @Column(name = "model_type", nullable = false, length = 50) private String modelType = "LINEAR";
    @Column(name = "forecast_domain", nullable = false, length = 50) private String forecastDomain;
    @Column(name = "is_active", nullable = false) private Boolean isActive = true;
    @Column(name = "is_default", nullable = false) private Boolean isDefault = false;
    @Column(name = "accuracy_score", precision = 5, scale = 2) private BigDecimal accuracyScore;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves model code data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getModelCode() { return modelCode; } public void setModelCode(String v) { this.modelCode = v; }
    /**
     * Retrieves model name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getModelName() { return modelName; } public void setModelName(String v) { this.modelName = v; }
    /**
     * Retrieves model type data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getModelType() { return modelType; } public void setModelType(String v) { this.modelType = v; }
    /**
     * Retrieves forecast domain data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getForecastDomain() { return forecastDomain; } public void setForecastDomain(String v) { this.forecastDomain = v; }
    /**
     * Retrieves is active data from the database.
     *
     * @param v the v input value
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean v) { this.isActive = v; }
    /**
     * Retrieves is default data from the database.
     *
     * @param v the v input value
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIsDefault() { return isDefault; } public void setIsDefault(Boolean v) { this.isDefault = v; }
    /**
     * Retrieves accuracy score data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAccuracyScore() { return accuracyScore; } public void setAccuracyScore(BigDecimal v) { this.accuracyScore = v; }
    /**
     * Retrieves description data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}