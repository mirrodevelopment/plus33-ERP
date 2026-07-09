/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformCloudResource.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCloudResourceController
 * Related Service   : PlatformCloudResourceService, PlatformCloudResourceServiceImpl
 * Related Repository: PlatformCloudResourceRepository
 * Related Entity    : PlatformCloudResource
 * Related DTO       : N/A
 * Related Mapper    : PlatformCloudResourceMapper
 * Related DB Table  : platform_cloud_resource
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCloudResourceRepository, PlatformCloudResourceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_cloud_resource'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformCloudResource}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_cloud_resource'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_cloud_resource}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_cloud_resource")
public class PlatformCloudResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "resource_id", nullable = false, unique = true)
    @NotNull
    @Size(max = 250)
    private String resourceId;

    @Column(name = "resource_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String resourceType;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String provider;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String region;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "RUNNING";

    @Column(name = "cost_daily", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal costDaily = BigDecimal.ZERO;

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
     * Retrieves resource type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getResourceType() { return resourceType; }
    /**
     * Performs the setResourceType operation in this module.
     *
     * @param resourceType the resourceType input value
     */
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    /**
     * Retrieves provider data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProvider() { return provider; }
    /**
     * Performs the setProvider operation in this module.
     *
     * @param provider the provider input value
     */
    public void setProvider(String provider) { this.provider = provider; }
    /**
     * Retrieves region data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRegion() { return region; }
    /**
     * Performs the setRegion operation in this module.
     *
     * @param region the region input value
     */
    public void setRegion(String region) { this.region = region; }
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
     * Retrieves cost daily data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCostDaily() { return costDaily; }
    /**
     * Performs the setCostDaily operation in this module.
     *
     * @param costDaily the costDaily input value
     */
    public void setCostDaily(BigDecimal costDaily) { this.costDaily = costDaily; }
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