/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformFailurePrognosticsLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformFailurePrognosticsLogController
 * Related Service   : PlatformFailurePrognosticsLogService, PlatformFailurePrognosticsLogServiceImpl
 * Related Repository: PlatformFailurePrognosticsLogRepository
 * Related Entity    : PlatformFailurePrognosticsLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformFailurePrognosticsLogMapper
 * Related DB Table  : platform_failure_prognostics_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformFailurePrognosticsLogRepository, PlatformFailurePrognosticsLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_failure_prognostics_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformFailurePrognosticsLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_failure_prognostics_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_failure_prognostics_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_failure_prognostics_log")
public class PlatformFailurePrognosticsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_id", nullable = false)
    @NotNull
    private Long assetId;

    @Column(name = "prediction_time", nullable = false)
    @NotNull
    private LocalDateTime predictionTime = LocalDateTime.now();

    @Column(name = "predicted_failure_time")
    private LocalDateTime predictedFailureTime;

    @Column(name = "remaining_useful_life_hours", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal remainingUsefulLifeHours;

    @Column(name = "failure_probability", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal failureProbability;

    @Column(name = "confidence_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidenceScore;

    @Column(name = "prediction_model_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String predictionModelVersion;

    @Column(name = "trigger_reason")
    @Size(max = 500)
    private String triggerReason;

    @Column(name = "recommended_action")
    @Size(max = 500)
    private String recommendedAction;

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
     * Retrieves asset id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getAssetId() { return assetId; }
    /**
     * Performs the setAssetId operation in this module.
     *
     * @param assetId the assetId input value
     */
    public void setAssetId(Long assetId) { this.assetId = assetId; }
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
     * Retrieves predicted failure time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getPredictedFailureTime() { return predictedFailureTime; }
    /**
     * Performs the setPredictedFailureTime operation in this module.
     *
     * @param predictedFailureTime the predictedFailureTime input value
     */
    public void setPredictedFailureTime(LocalDateTime predictedFailureTime) { this.predictedFailureTime = predictedFailureTime; }
    /**
     * Retrieves remaining useful life hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRemainingUsefulLifeHours() { return remainingUsefulLifeHours; }
    /**
     * Performs the setRemainingUsefulLifeHours operation in this module.
     *
     * @param remainingUsefulLifeHours the remainingUsefulLifeHours input value
     */
    public void setRemainingUsefulLifeHours(BigDecimal remainingUsefulLifeHours) { this.remainingUsefulLifeHours = remainingUsefulLifeHours; }
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
     * Retrieves confidence score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    /**
     * Performs the setConfidenceScore operation in this module.
     *
     * @param confidenceScore the confidenceScore input value
     */
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    /**
     * Retrieves prediction model version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPredictionModelVersion() { return predictionModelVersion; }
    /**
     * Performs the setPredictionModelVersion operation in this module.
     *
     * @param predictionModelVersion the predictionModelVersion input value
     */
    public void setPredictionModelVersion(String predictionModelVersion) { this.predictionModelVersion = predictionModelVersion; }
    /**
     * Retrieves trigger reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTriggerReason() { return triggerReason; }
    /**
     * Performs the setTriggerReason operation in this module.
     *
     * @param triggerReason the triggerReason input value
     */
    public void setTriggerReason(String triggerReason) { this.triggerReason = triggerReason; }
    /**
     * Retrieves recommended action data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRecommendedAction() { return recommendedAction; }
    /**
     * Performs the setRecommendedAction operation in this module.
     *
     * @param recommendedAction the recommendedAction input value
     */
    public void setRecommendedAction(String recommendedAction) { this.recommendedAction = recommendedAction; }
}