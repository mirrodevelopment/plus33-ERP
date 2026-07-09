/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiAiInsightGeneration.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiAiInsightGenerationController
 * Related Service   : BiAiInsightGenerationService, BiAiInsightGenerationServiceImpl
 * Related Repository: BiAiInsightGenerationRepository
 * Related Entity    : BiAiInsightGeneration
 * Related DTO       : N/A
 * Related Mapper    : BiAiInsightGenerationMapper
 * Related DB Table  : bi_ai_insight_generation
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiAiInsightGenerationRepository, BiAiInsightGenerationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_ai_insight_generation'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiAiInsightGeneration}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_ai_insight_generation'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_ai_insight_generation}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_ai_insight_generation")
public class BiAiInsightGeneration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "insight_code", nullable = false, unique = true)
    private String insightCode;
    @Column(name = "company_id", nullable = false)
    private Long companyId;
    @Column(name = "kpi_code", nullable = false)
    private String kpiCode;
    @Column(name = "source_snapshot_id")
    private Long sourceSnapshotId;
    @Column(name = "generated_model", nullable = false)
    private String generatedModel;
    @Column(name = "confidence_score")
    private java.math.BigDecimal confidenceScore;
    @Column(name = "narrative_text", nullable = false)
    private String narrativeText;
    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();
    @Column(name = "accepted_by_user")
    private Boolean acceptedByUser = false;
    @Column(name = "feedback_rating")
    private Integer feedbackRating;

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
     * Retrieves insight code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getInsightCode() { return insightCode; }
    /**
     * Performs the setInsightCode operation in this module.
     *
     * @param insightCode the insightCode input value
     */
    public void setInsightCode(String insightCode) { this.insightCode = insightCode; }
    /**
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves kpi code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getKpiCode() { return kpiCode; }
    /**
     * Performs the setKpiCode operation in this module.
     *
     * @param kpiCode the kpiCode input value
     */
    public void setKpiCode(String kpiCode) { this.kpiCode = kpiCode; }
    /**
     * Retrieves source snapshot id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceSnapshotId() { return sourceSnapshotId; }
    /**
     * Performs the setSourceSnapshotId operation in this module.
     *
     * @param sourceSnapshotId the sourceSnapshotId input value
     */
    public void setSourceSnapshotId(Long sourceSnapshotId) { this.sourceSnapshotId = sourceSnapshotId; }
    /**
     * Retrieves generated model data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getGeneratedModel() { return generatedModel; }
    /**
     * Performs the setGeneratedModel operation in this module.
     *
     * @param generatedModel the generatedModel input value
     */
    public void setGeneratedModel(String generatedModel) { this.generatedModel = generatedModel; }
    /**
     * Retrieves confidence score data from the database.
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.math.BigDecimal getConfidenceScore() { return confidenceScore; }
    /**
     * Performs the setConfidenceScore operation in this module.
     *
     * @param confidenceScore the confidenceScore input value
     */
    public void setConfidenceScore(java.math.BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    /**
     * Retrieves narrative text data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNarrativeText() { return narrativeText; }
    /**
     * Performs the setNarrativeText operation in this module.
     *
     * @param narrativeText the narrativeText input value
     */
    public void setNarrativeText(String narrativeText) { this.narrativeText = narrativeText; }
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
    /**
     * Retrieves accepted by user data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getAcceptedByUser() { return acceptedByUser; }
    /**
     * Performs the setAcceptedByUser operation in this module.
     *
     * @param acceptedByUser the acceptedByUser input value
     */
    public void setAcceptedByUser(Boolean acceptedByUser) { this.acceptedByUser = acceptedByUser; }
    /**
     * Retrieves feedback rating data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getFeedbackRating() { return feedbackRating; }
    /**
     * Performs the setFeedbackRating operation in this module.
     *
     * @param feedbackRating the feedbackRating input value
     */
    public void setFeedbackRating(Integer feedbackRating) { this.feedbackRating = feedbackRating; }
}