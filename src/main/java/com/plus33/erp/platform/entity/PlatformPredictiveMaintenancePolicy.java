/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformPredictiveMaintenancePolicy.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformPredictiveMaintenancePolicyController
 * Related Service   : PlatformPredictiveMaintenancePolicyService, PlatformPredictiveMaintenancePolicyServiceImpl
 * Related Repository: PlatformPredictiveMaintenancePolicyRepository
 * Related Entity    : PlatformPredictiveMaintenancePolicy
 * Related DTO       : N/A
 * Related Mapper    : PlatformPredictiveMaintenancePolicyMapper
 * Related DB Table  : platform_predictive_maintenance_policy
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformPredictiveMaintenancePolicyRepository, PlatformPredictiveMaintenancePolicyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_predictive_maintenance_policy'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformPredictiveMaintenancePolicy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_predictive_maintenance_policy'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_predictive_maintenance_policy}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_predictive_maintenance_policy")
public class PlatformPredictiveMaintenancePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "policy_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String policyCode;

    @Column(name = "policy_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String policyName;

    @Column(name = "asset_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String assetType;

    @Column(name = "prediction_model", nullable = false)
    @NotNull
    @Size(max = 100)
    private String predictionModel;

    @Column(name = "minimum_health_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal minimumHealthScore;

    @Column(name = "remaining_useful_life_threshold", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal remainingUsefulLifeThreshold;

    @Column(name = "failure_probability_threshold", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal failureProbabilityThreshold;

    @Column(name = "maintenance_strategy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String maintenanceStrategy; // Preventive, Predictive, Condition-Based

    @Column(nullable = false)
    @NotNull
    private Integer priority = 0;

    @Column(nullable = false)
    @NotNull
    private Boolean enabled = true;

    @Column(name = "created_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String createdBy;

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
     * Retrieves policy code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPolicyCode() { return policyCode; }
    /**
     * Performs the setPolicyCode operation in this module.
     *
     * @param policyCode the policyCode input value
     */
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    /**
     * Retrieves policy name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPolicyName() { return policyName; }
    /**
     * Performs the setPolicyName operation in this module.
     *
     * @param policyName the policyName input value
     */
    public void setPolicyName(String policyName) { this.policyName = policyName; }
    /**
     * Retrieves asset type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAssetType() { return assetType; }
    /**
     * Performs the setAssetType operation in this module.
     *
     * @param assetType the assetType input value
     */
    public void setAssetType(String assetType) { this.assetType = assetType; }
    /**
     * Retrieves prediction model data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPredictionModel() { return predictionModel; }
    /**
     * Performs the setPredictionModel operation in this module.
     *
     * @param predictionModel the predictionModel input value
     */
    public void setPredictionModel(String predictionModel) { this.predictionModel = predictionModel; }
    /**
     * Retrieves minimum health score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMinimumHealthScore() { return minimumHealthScore; }
    /**
     * Performs the setMinimumHealthScore operation in this module.
     *
     * @param minimumHealthScore the minimumHealthScore input value
     */
    public void setMinimumHealthScore(BigDecimal minimumHealthScore) { this.minimumHealthScore = minimumHealthScore; }
    /**
     * Retrieves remaining useful life threshold data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRemainingUsefulLifeThreshold() { return remainingUsefulLifeThreshold; }
    /**
     * Performs the setRemainingUsefulLifeThreshold operation in this module.
     *
     * @param remainingUsefulLifeThreshold the remainingUsefulLifeThreshold input value
     */
    public void setRemainingUsefulLifeThreshold(BigDecimal remainingUsefulLifeThreshold) { this.remainingUsefulLifeThreshold = remainingUsefulLifeThreshold; }
    /**
     * Retrieves failure probability threshold data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getFailureProbabilityThreshold() { return failureProbabilityThreshold; }
    /**
     * Performs the setFailureProbabilityThreshold operation in this module.
     *
     * @param failureProbabilityThreshold the failureProbabilityThreshold input value
     */
    public void setFailureProbabilityThreshold(BigDecimal failureProbabilityThreshold) { this.failureProbabilityThreshold = failureProbabilityThreshold; }
    /**
     * Retrieves maintenance strategy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMaintenanceStrategy() { return maintenanceStrategy; }
    /**
     * Performs the setMaintenanceStrategy operation in this module.
     *
     * @param maintenanceStrategy the maintenanceStrategy input value
     */
    public void setMaintenanceStrategy(String maintenanceStrategy) { this.maintenanceStrategy = maintenanceStrategy; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(Integer priority) { this.priority = priority; }
    /**
     * Retrieves enabled data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getEnabled() { return enabled; }
    /**
     * Performs the setEnabled operation in this module.
     *
     * @param enabled the enabled input value
     */
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    /**
     * Retrieves created by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCreatedBy() { return createdBy; }
    /**
     * Performs the setCreatedBy operation in this module.
     *
     * @param createdBy the createdBy input value
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
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