/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformOtaNodeExecution.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformOtaNodeExecutionController
 * Related Service   : PlatformOtaNodeExecutionService, PlatformOtaNodeExecutionServiceImpl
 * Related Repository: PlatformOtaNodeExecutionRepository
 * Related Entity    : PlatformOtaNodeExecution
 * Related DTO       : N/A
 * Related Mapper    : PlatformOtaNodeExecutionMapper
 * Related DB Table  : platform_ota_node_execution
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformOtaNodeExecutionRepository, PlatformOtaNodeExecutionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_ota_node_execution'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformOtaNodeExecution}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_ota_node_execution'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ota_node_execution}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_ota_node_execution")
public class PlatformOtaNodeExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "campaign_id", nullable = false)
    @NotNull
    private Long campaignId;

    @Column(name = "node_id", nullable = false)
    @NotNull
    private Long nodeId;

    @Column(name = "download_started")
    private LocalDateTime downloadStarted;

    @Column(name = "download_completed")
    private LocalDateTime downloadCompleted;

    @Column(name = "install_started")
    private LocalDateTime installStarted;

    @Column(name = "install_completed")
    private LocalDateTime installCompleted;

    @Column(name = "reboot_required", nullable = false)
    @NotNull
    private Boolean rebootRequired = false;

    @Column(name = "reboot_completed", nullable = false)
    @NotNull
    private Boolean rebootCompleted = false;

    @Column(name = "execution_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String executionStatus; // QUEUED, DOWNLOADING, INSTALLING, SUCCESS, FAILED, ROLLED_BACK

    @Column(name = "failure_reason")
    @Size(max = 500)
    private String failureReason;

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
     * Retrieves campaign id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCampaignId() { return campaignId; }
    /**
     * Performs the setCampaignId operation in this module.
     *
     * @param campaignId the campaignId input value
     */
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    /**
     * Retrieves node id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getNodeId() { return nodeId; }
    /**
     * Performs the setNodeId operation in this module.
     *
     * @param nodeId the nodeId input value
     */
    public void setNodeId(Long nodeId) { this.nodeId = nodeId; }
    /**
     * Retrieves download started data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getDownloadStarted() { return downloadStarted; }
    /**
     * Performs the setDownloadStarted operation in this module.
     *
     * @param downloadStarted the downloadStarted input value
     */
    public void setDownloadStarted(LocalDateTime downloadStarted) { this.downloadStarted = downloadStarted; }
    /**
     * Retrieves download completed data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getDownloadCompleted() { return downloadCompleted; }
    /**
     * Performs the setDownloadCompleted operation in this module.
     *
     * @param downloadCompleted the downloadCompleted input value
     */
    public void setDownloadCompleted(LocalDateTime downloadCompleted) { this.downloadCompleted = downloadCompleted; }
    /**
     * Retrieves a paginated list of install started records.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getInstallStarted() { return installStarted; }
    /**
     * Performs the setInstallStarted operation in this module.
     *
     * @param installStarted the installStarted input value
     */
    public void setInstallStarted(LocalDateTime installStarted) { this.installStarted = installStarted; }
    /**
     * Retrieves a paginated list of install completed records.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getInstallCompleted() { return installCompleted; }
    /**
     * Performs the setInstallCompleted operation in this module.
     *
     * @param installCompleted the installCompleted input value
     */
    public void setInstallCompleted(LocalDateTime installCompleted) { this.installCompleted = installCompleted; }
    /**
     * Retrieves reboot required data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getRebootRequired() { return rebootRequired; }
    /**
     * Performs the setRebootRequired operation in this module.
     *
     * @param rebootRequired the rebootRequired input value
     */
    public void setRebootRequired(Boolean rebootRequired) { this.rebootRequired = rebootRequired; }
    /**
     * Retrieves reboot completed data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getRebootCompleted() { return rebootCompleted; }
    /**
     * Performs the setRebootCompleted operation in this module.
     *
     * @param rebootCompleted the rebootCompleted input value
     */
    public void setRebootCompleted(Boolean rebootCompleted) { this.rebootCompleted = rebootCompleted; }
    /**
     * Retrieves execution status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExecutionStatus() { return executionStatus; }
    /**
     * Performs the setExecutionStatus operation in this module.
     *
     * @param executionStatus the executionStatus input value
     */
    public void setExecutionStatus(String executionStatus) { this.executionStatus = executionStatus; }
    /**
     * Retrieves failure reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFailureReason() { return failureReason; }
    /**
     * Performs the setFailureReason operation in this module.
     *
     * @param failureReason the failureReason input value
     */
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
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