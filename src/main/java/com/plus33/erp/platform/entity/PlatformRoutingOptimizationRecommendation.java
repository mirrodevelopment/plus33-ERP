/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformRoutingOptimizationRecommendation.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRoutingOptimizationRecommendationController
 * Related Service   : PlatformRoutingOptimizationRecommendationService, PlatformRoutingOptimizationRecommendationServiceImpl
 * Related Repository: PlatformRoutingOptimizationRecommendationRepository
 * Related Entity    : PlatformRoutingOptimizationRecommendation
 * Related DTO       : N/A
 * Related Mapper    : PlatformRoutingOptimizationRecommendationMapper
 * Related DB Table  : platform_routing_optimization_recommendation
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRoutingOptimizationRecommendationRepository, PlatformRoutingOptimizationRecommendationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_routing_optimization_recommendation'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformRoutingOptimizationRecommendation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_routing_optimization_recommendation'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_routing_optimization_recommendation}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_routing_optimization_recommendation")
public class PlatformRoutingOptimizationRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recommended_route", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String recommendedRoute;

    @Column(name = "estimated_savings_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal estimatedSavingsCost;

    @Column(name = "estimated_time_saved_m", nullable = false)
    @NotNull
    private Integer estimatedTimeSavedM;

    @Column(name = "estimated_fuel_saved_l", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal estimatedFuelSavedL;

    @Column(name = "estimated_co2_saved_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal estimatedCo2SavedKg;

    @Column(name = "confidence_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidenceScore;

    @Column(name = "algorithm_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String algorithmVersion;

    @Column(nullable = false)
    @NotNull
    private Boolean accepted = false;

    @Column(nullable = false)
    @NotNull
    private Boolean implemented = false;

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves recommended route data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRecommendedRoute() { return recommendedRoute; }
    /**
     * Performs the setRecommendedRoute operation in this module.
     *
     * @param recommendedRoute the recommendedRoute input value
     */
    public void setRecommendedRoute(String recommendedRoute) { this.recommendedRoute = recommendedRoute; }
    /**
     * Retrieves estimated savings cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEstimatedSavingsCost() { return estimatedSavingsCost; }
    /**
     * Performs the setEstimatedSavingsCost operation in this module.
     *
     * @param estimatedSavingsCost the estimatedSavingsCost input value
     */
    public void setEstimatedSavingsCost(BigDecimal estimatedSavingsCost) { this.estimatedSavingsCost = estimatedSavingsCost; }
    /**
     * Retrieves estimated time saved m data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getEstimatedTimeSavedM() { return estimatedTimeSavedM; }
    /**
     * Performs the setEstimatedTimeSavedM operation in this module.
     *
     * @param estimatedTimeSavedM the estimatedTimeSavedM input value
     */
    public void setEstimatedTimeSavedM(Integer estimatedTimeSavedM) { this.estimatedTimeSavedM = estimatedTimeSavedM; }
    /**
     * Retrieves estimated fuel saved l data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEstimatedFuelSavedL() { return estimatedFuelSavedL; }
    /**
     * Performs the setEstimatedFuelSavedL operation in this module.
     *
     * @param estimatedFuelSavedL the estimatedFuelSavedL input value
     */
    public void setEstimatedFuelSavedL(BigDecimal estimatedFuelSavedL) { this.estimatedFuelSavedL = estimatedFuelSavedL; }
    /**
     * Retrieves estimated co2 saved kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEstimatedCo2SavedKg() { return estimatedCo2SavedKg; }
    /**
     * Performs the setEstimatedCo2SavedKg operation in this module.
     *
     * @param estimatedCo2SavedKg the estimatedCo2SavedKg input value
     */
    public void setEstimatedCo2SavedKg(BigDecimal estimatedCo2SavedKg) { this.estimatedCo2SavedKg = estimatedCo2SavedKg; }
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
     * Retrieves algorithm version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAlgorithmVersion() { return algorithmVersion; }
    /**
     * Performs the setAlgorithmVersion operation in this module.
     *
     * @param algorithmVersion the algorithmVersion input value
     */
    public void setAlgorithmVersion(String algorithmVersion) { this.algorithmVersion = algorithmVersion; }
    /**
     * Retrieves accepted data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getAccepted() { return accepted; }
    /**
     * Performs the setAccepted operation in this module.
     *
     * @param accepted the accepted input value
     */
    public void setAccepted(Boolean accepted) { this.accepted = accepted; }
    /**
     * Retrieves implemented data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getImplemented() { return implemented; }
    /**
     * Performs the setImplemented operation in this module.
     *
     * @param implemented the implemented input value
     */
    public void setImplemented(Boolean implemented) { this.implemented = implemented; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}