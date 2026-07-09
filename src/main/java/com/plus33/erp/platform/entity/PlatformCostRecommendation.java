/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformCostRecommendation.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCostRecommendationController
 * Related Service   : PlatformCostRecommendationService, PlatformCostRecommendationServiceImpl
 * Related Repository: PlatformCostRecommendationRepository
 * Related Entity    : PlatformCostRecommendation
 * Related DTO       : N/A
 * Related Mapper    : PlatformCostRecommendationMapper
 * Related DB Table  : platform_cost_recommendation
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCostRecommendationRepository, PlatformCostRecommendationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_cost_recommendation'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformCostRecommendation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_cost_recommendation'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_cost_recommendation}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_cost_recommendation")
public class PlatformCostRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "resource_id", nullable = false)
    @NotNull
    @Size(max = 250)
    private String resourceId;

    @Column(name = "recommendation_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String recommendationType;

    @Column(name = "savings_potential", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal savingsPotential;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String reason;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false, updatable = false)
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
     * Retrieves resource id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getResourceId() { return resourceId; }
    /**
     * Performs the setResourceId operation in this module.
     *
     * @param resourceId the resourceId input value
     */
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    /**
     * Retrieves recommendation type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRecommendationType() { return recommendationType; }
    /**
     * Performs the setRecommendationType operation in this module.
     *
     * @param recommendationType the recommendationType input value
     */
    public void setRecommendationType(String recommendationType) { this.recommendationType = recommendationType; }
    /**
     * Retrieves savings potential data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSavingsPotential() { return savingsPotential; }
    /**
     * Performs the setSavingsPotential operation in this module.
     *
     * @param savingsPotential the savingsPotential input value
     */
    public void setSavingsPotential(BigDecimal savingsPotential) { this.savingsPotential = savingsPotential; }
    /**
     * Retrieves reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReason() { return reason; }
    /**
     * Performs the setReason operation in this module.
     *
     * @param reason the reason input value
     */
    public void setReason(String reason) { this.reason = reason; }
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