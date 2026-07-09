/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDeploymentHistory.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDeploymentHistoryController
 * Related Service   : PlatformDeploymentHistoryService, PlatformDeploymentHistoryServiceImpl
 * Related Repository: PlatformDeploymentHistoryRepository
 * Related Entity    : PlatformDeploymentHistory
 * Related DTO       : N/A
 * Related Mapper    : PlatformDeploymentHistoryMapper
 * Related DB Table  : platform_deployment_history
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDeploymentHistoryRepository, PlatformDeploymentHistoryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_deployment_history'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDeploymentHistory}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_deployment_history'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_deployment_history}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_deployment_history")
public class PlatformDeploymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "deployment_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String deploymentVersion;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String changelog;

    @Column(name = "deployed_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String deployedBy;

    @Column(name = "started_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

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
     * Retrieves deployment version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDeploymentVersion() { return deploymentVersion; }
    /**
     * Performs the setDeploymentVersion operation in this module.
     *
     * @param deploymentVersion the deploymentVersion input value
     */
    public void setDeploymentVersion(String deploymentVersion) { this.deploymentVersion = deploymentVersion; }
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
     * Retrieves changelog data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChangelog() { return changelog; }
    /**
     * Performs the setChangelog operation in this module.
     *
     * @param changelog the changelog input value
     */
    public void setChangelog(String changelog) { this.changelog = changelog; }
    /**
     * Retrieves deployed by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDeployedBy() { return deployedBy; }
    /**
     * Performs the setDeployedBy operation in this module.
     *
     * @param deployedBy the deployedBy input value
     */
    public void setDeployedBy(String deployedBy) { this.deployedBy = deployedBy; }
    /**
     * Retrieves started at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartedAt() { return startedAt; }
    /**
     * Performs the setStartedAt operation in this module.
     *
     * @param startedAt the startedAt input value
     */
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    /**
     * Retrieves completed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletedAt() { return completedAt; }
    /**
     * Performs the setCompletedAt operation in this module.
     *
     * @param completedAt the completedAt input value
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}