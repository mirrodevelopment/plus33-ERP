/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformFeatureFlag.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformFeatureFlagController
 * Related Service   : PlatformFeatureFlagService, PlatformFeatureFlagServiceImpl
 * Related Repository: PlatformFeatureFlagRepository
 * Related Entity    : PlatformFeatureFlag
 * Related DTO       : N/A
 * Related Mapper    : PlatformFeatureFlagMapper
 * Related DB Table  : platform_feature_flag
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformFeatureFlagRepository, PlatformFeatureFlagMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_feature_flag'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformFeatureFlag}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_feature_flag'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_feature_flag}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_feature_flag")
public class PlatformFeatureFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "flag_key", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String flagKey;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "DISABLED";

    @Column(name = "rollout_percentage", nullable = false)
    @NotNull
    private Integer rolloutPercentage = 0;

    @Column(name = "rules_json", columnDefinition = "TEXT")
    private String rulesJson;

    @Column(length = 500)
    @Size(max = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

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
     * Retrieves flag key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFlagKey() { return flagKey; }
    /**
     * Performs the setFlagKey operation in this module.
     *
     * @param flagKey the flagKey input value
     */
    public void setFlagKey(String flagKey) { this.flagKey = flagKey; }
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
     * Retrieves rollout percentage data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRolloutPercentage() { return rolloutPercentage; }
    /**
     * Performs the setRolloutPercentage operation in this module.
     *
     * @param rolloutPercentage the rolloutPercentage input value
     */
    public void setRolloutPercentage(Integer rolloutPercentage) { this.rolloutPercentage = rolloutPercentage; }
    /**
     * Retrieves rules json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRulesJson() { return rulesJson; }
    /**
     * Performs the setRulesJson operation in this module.
     *
     * @param rulesJson the rulesJson input value
     */
    public void setRulesJson(String rulesJson) { this.rulesJson = rulesJson; }
    /**
     * Retrieves description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; }
    /**
     * Performs the setDescription operation in this module.
     *
     * @param description the description input value
     */
    public void setDescription(String description) { this.description = description; }
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
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}