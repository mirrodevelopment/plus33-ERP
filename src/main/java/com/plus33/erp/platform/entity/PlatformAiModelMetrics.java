/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAiModelMetrics.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAiModelMetricsController
 * Related Service   : PlatformAiModelMetricsService, PlatformAiModelMetricsServiceImpl
 * Related Repository: PlatformAiModelMetricsRepository
 * Related Entity    : PlatformAiModelMetrics
 * Related DTO       : N/A
 * Related Mapper    : PlatformAiModelMetricsMapper
 * Related DB Table  : platform_ai_model_metrics
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAiModelMetricsRepository, PlatformAiModelMetricsMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_ai_model_metrics'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAiModelMetrics}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_ai_model_metrics'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ai_model_metrics}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_ai_model_metrics")
public class PlatformAiModelMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_version_id", nullable = false)
    @NotNull
    private Long modelVersionId;

    @Column(precision = 10, scale = 4)
    private BigDecimal rmse;

    @Column(precision = 10, scale = 4)
    private BigDecimal mae;

    @Column(name = "recorded_at", nullable = false)
    @NotNull
    private LocalDateTime recordedAt = LocalDateTime.now();

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
     * Retrieves model version id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getModelVersionId() { return modelVersionId; }
    /**
     * Performs the setModelVersionId operation in this module.
     *
     * @param modelVersionId the modelVersionId input value
     */
    public void setModelVersionId(Long modelVersionId) { this.modelVersionId = modelVersionId; }
    /**
     * Retrieves rmse data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRmse() { return rmse; }
    /**
     * Performs the setRmse operation in this module.
     *
     * @param rmse the rmse input value
     */
    public void setRmse(BigDecimal rmse) { this.rmse = rmse; }
    /**
     * Retrieves mae data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMae() { return mae; }
    /**
     * Performs the setMae operation in this module.
     *
     * @param mae the mae input value
     */
    public void setMae(BigDecimal mae) { this.mae = mae; }
    /**
     * Retrieves recorded at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRecordedAt() { return recordedAt; }
    /**
     * Performs the setRecordedAt operation in this module.
     *
     * @param recordedAt the recordedAt input value
     */
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}