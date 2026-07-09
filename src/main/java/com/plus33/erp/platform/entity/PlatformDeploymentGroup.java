/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDeploymentGroup.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDeploymentGroupController
 * Related Service   : PlatformDeploymentGroupService, PlatformDeploymentGroupServiceImpl
 * Related Repository: PlatformDeploymentGroupRepository
 * Related Entity    : PlatformDeploymentGroup
 * Related DTO       : N/A
 * Related Mapper    : PlatformDeploymentGroupMapper
 * Related DB Table  : platform_deployment_group
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDeploymentGroupRepository, PlatformDeploymentGroupMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_deployment_group'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDeploymentGroup}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_deployment_group'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_deployment_group}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_deployment_group")
public class PlatformDeploymentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "group_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String groupName;

    @Column(name = "active_router", nullable = false)
    @NotNull
    private Boolean activeRouter = false;

    @Column(name = "canary_weight", nullable = false)
    @NotNull
    private Integer canaryWeight = 0;

    @Column(name = "target_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String targetVersion;

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
     * Retrieves group name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getGroupName() { return groupName; }
    /**
     * Performs the setGroupName operation in this module.
     *
     * @param groupName the groupName input value
     */
    public void setGroupName(String groupName) { this.groupName = groupName; }
    /**
     * Retrieves active router data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActiveRouter() { return activeRouter; }
    /**
     * Performs the setActiveRouter operation in this module.
     *
     * @param activeRouter the activeRouter input value
     */
    public void setActiveRouter(Boolean activeRouter) { this.activeRouter = activeRouter; }
    /**
     * Retrieves canary weight data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getCanaryWeight() { return canaryWeight; }
    /**
     * Performs the setCanaryWeight operation in this module.
     *
     * @param canaryWeight the canaryWeight input value
     */
    public void setCanaryWeight(Integer canaryWeight) { this.canaryWeight = canaryWeight; }
    /**
     * Retrieves target version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTargetVersion() { return targetVersion; }
    /**
     * Performs the setTargetVersion operation in this module.
     *
     * @param targetVersion the targetVersion input value
     */
    public void setTargetVersion(String targetVersion) { this.targetVersion = targetVersion; }
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