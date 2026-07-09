/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformRootCauseAnalysis.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRootCauseAnalysisController
 * Related Service   : PlatformRootCauseAnalysisService, PlatformRootCauseAnalysisServiceImpl
 * Related Repository: PlatformRootCauseAnalysisRepository
 * Related Entity    : PlatformRootCauseAnalysis
 * Related DTO       : N/A
 * Related Mapper    : PlatformRootCauseAnalysisMapper
 * Related DB Table  : platform_root_cause_analysis
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRootCauseAnalysisRepository, PlatformRootCauseAnalysisMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_root_cause_analysis'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformRootCauseAnalysis}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_root_cause_analysis'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_root_cause_analysis}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_root_cause_analysis")
public class PlatformRootCauseAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "causal_model_id", nullable = false)
    @NotNull
    private Long causalModelId;

    @Column(name = "anomaly_event", nullable = false)
    @NotNull
    @Size(max = 150)
    private String anomalyEvent;

    @Column(name = "probabilities_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String probabilitiesJson;

    @Column(name = "root_cause_node", nullable = false)
    @NotNull
    @Size(max = 150)
    private String rootCauseNode;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String explanation;

    @Column(name = "analyzed_at", nullable = false)
    @NotNull
    private LocalDateTime analyzedAt = LocalDateTime.now();

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
     * Retrieves causal model id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCausalModelId() { return causalModelId; }
    /**
     * Performs the setCausalModelId operation in this module.
     *
     * @param causalModelId the causalModelId input value
     */
    public void setCausalModelId(Long causalModelId) { this.causalModelId = causalModelId; }
    /**
     * Retrieves anomaly event data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAnomalyEvent() { return anomalyEvent; }
    /**
     * Performs the setAnomalyEvent operation in this module.
     *
     * @param anomalyEvent the anomalyEvent input value
     */
    public void setAnomalyEvent(String anomalyEvent) { this.anomalyEvent = anomalyEvent; }
    /**
     * Retrieves probabilities json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProbabilitiesJson() { return probabilitiesJson; }
    /**
     * Performs the setProbabilitiesJson operation in this module.
     *
     * @param probabilitiesJson the probabilitiesJson input value
     */
    public void setProbabilitiesJson(String probabilitiesJson) { this.probabilitiesJson = probabilitiesJson; }
    /**
     * Retrieves root cause node data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRootCauseNode() { return rootCauseNode; }
    /**
     * Performs the setRootCauseNode operation in this module.
     *
     * @param rootCauseNode the rootCauseNode input value
     */
    public void setRootCauseNode(String rootCauseNode) { this.rootCauseNode = rootCauseNode; }
    /**
     * Retrieves explanation data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExplanation() { return explanation; }
    /**
     * Performs the setExplanation operation in this module.
     *
     * @param explanation the explanation input value
     */
    public void setExplanation(String explanation) { this.explanation = explanation; }
    /**
     * Retrieves analyzed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAnalyzedAt() { return analyzedAt; }
    /**
     * Performs the setAnalyzedAt operation in this module.
     *
     * @param analyzedAt the analyzedAt input value
     */
    public void setAnalyzedAt(LocalDateTime analyzedAt) { this.analyzedAt = analyzedAt; }
}