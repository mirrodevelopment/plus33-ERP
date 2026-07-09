/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformOtaCampaign.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformOtaCampaignController
 * Related Service   : PlatformOtaCampaignService, PlatformOtaCampaignServiceImpl
 * Related Repository: PlatformOtaCampaignRepository
 * Related Entity    : PlatformOtaCampaign
 * Related DTO       : N/A
 * Related Mapper    : PlatformOtaCampaignMapper
 * Related DB Table  : platform_ota_campaign
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformOtaCampaignRepository, PlatformOtaCampaignMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_ota_campaign'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformOtaCampaign}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_ota_campaign'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ota_campaign}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_ota_campaign")
public class PlatformOtaCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "campaign_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String campaignName;

    @Column(name = "package_id", nullable = false)
    @NotNull
    private Long packageId;

    @Column(name = "scheduled_start")
    private LocalDateTime scheduledStart;

    @Column(name = "scheduled_end")
    private LocalDateTime scheduledEnd;

    @Column(name = "rollout_percentage", nullable = false)
    @NotNull
    private Integer rolloutPercentage = 100;

    @Column(name = "batch_size", nullable = false)
    @NotNull
    private Integer batchSize = 10;

    @Column(name = "retry_policy")
    @Size(max = 100)
    private String retryPolicy;

    @Column(name = "failure_threshold", nullable = false)
    @NotNull
    private Integer failureThreshold = 5;

    @Column(name = "rollback_enabled", nullable = false)
    @NotNull
    private Boolean rollbackEnabled = true;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status; // PENDING, ACTIVE, COMPLETED, ROLLED_BACK, FAILED

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
     * Retrieves campaign name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCampaignName() { return campaignName; }
    /**
     * Performs the setCampaignName operation in this module.
     *
     * @param campaignName the campaignName input value
     */
    public void setCampaignName(String campaignName) { this.campaignName = campaignName; }
    /**
     * Retrieves package id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPackageId() { return packageId; }
    /**
     * Performs the setPackageId operation in this module.
     *
     * @param packageId the packageId input value
     */
    public void setPackageId(Long packageId) { this.packageId = packageId; }
    /**
     * Retrieves scheduled start data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getScheduledStart() { return scheduledStart; }
    /**
     * Performs the setScheduledStart operation in this module.
     *
     * @param scheduledStart the scheduledStart input value
     */
    public void setScheduledStart(LocalDateTime scheduledStart) { this.scheduledStart = scheduledStart; }
    /**
     * Retrieves scheduled end data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getScheduledEnd() { return scheduledEnd; }
    /**
     * Performs the setScheduledEnd operation in this module.
     *
     * @param scheduledEnd the scheduledEnd input value
     */
    public void setScheduledEnd(LocalDateTime scheduledEnd) { this.scheduledEnd = scheduledEnd; }
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
     * Retrieves batch size data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getBatchSize() { return batchSize; }
    /**
     * Performs the setBatchSize operation in this module.
     *
     * @param batchSize the batchSize input value
     */
    public void setBatchSize(Integer batchSize) { this.batchSize = batchSize; }
    /**
     * Retrieves retry policy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRetryPolicy() { return retryPolicy; }
    /**
     * Performs the setRetryPolicy operation in this module.
     *
     * @param retryPolicy the retryPolicy input value
     */
    public void setRetryPolicy(String retryPolicy) { this.retryPolicy = retryPolicy; }
    /**
     * Retrieves failure threshold data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getFailureThreshold() { return failureThreshold; }
    /**
     * Performs the setFailureThreshold operation in this module.
     *
     * @param failureThreshold the failureThreshold input value
     */
    public void setFailureThreshold(Integer failureThreshold) { this.failureThreshold = failureThreshold; }
    /**
     * Retrieves rollback enabled data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getRollbackEnabled() { return rollbackEnabled; }
    /**
     * Performs the setRollbackEnabled operation in this module.
     *
     * @param rollbackEnabled the rollbackEnabled input value
     */
    public void setRollbackEnabled(Boolean rollbackEnabled) { this.rollbackEnabled = rollbackEnabled; }
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