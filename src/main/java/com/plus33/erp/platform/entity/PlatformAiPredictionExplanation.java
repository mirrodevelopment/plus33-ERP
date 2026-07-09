/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAiPredictionExplanation.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAiPredictionExplanationController
 * Related Service   : PlatformAiPredictionExplanationService, PlatformAiPredictionExplanationServiceImpl
 * Related Repository: PlatformAiPredictionExplanationRepository
 * Related Entity    : PlatformAiPredictionExplanation
 * Related DTO       : N/A
 * Related Mapper    : PlatformAiPredictionExplanationMapper
 * Related DB Table  : platform_ai_prediction_explanation
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAiPredictionExplanationRepository, PlatformAiPredictionExplanationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_ai_prediction_explanation'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformAiPredictionExplanation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_ai_prediction_explanation'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ai_prediction_explanation}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_ai_prediction_explanation")
public class PlatformAiPredictionExplanation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prediction_time", nullable = false)
    @NotNull
    private LocalDateTime predictionTime = LocalDateTime.now();

    @Column(name = "prediction_target", nullable = false)
    @NotNull
    @Size(max = 150)
    private String predictionTarget;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String reasoning;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidence;

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
     * Retrieves prediction time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getPredictionTime() { return predictionTime; }
    /**
     * Performs the setPredictionTime operation in this module.
     *
     * @param predictionTime the predictionTime input value
     */
    public void setPredictionTime(LocalDateTime predictionTime) { this.predictionTime = predictionTime; }
    /**
     * Retrieves prediction target data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPredictionTarget() { return predictionTarget; }
    /**
     * Performs the setPredictionTarget operation in this module.
     *
     * @param predictionTarget the predictionTarget input value
     */
    public void setPredictionTarget(String predictionTarget) { this.predictionTarget = predictionTarget; }
    /**
     * Retrieves reasoning data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReasoning() { return reasoning; }
    /**
     * Performs the setReasoning operation in this module.
     *
     * @param reasoning the reasoning input value
     */
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
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
}