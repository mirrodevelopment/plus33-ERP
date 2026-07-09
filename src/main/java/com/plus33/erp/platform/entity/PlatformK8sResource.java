/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformK8sResource.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformK8sResourceController
 * Related Service   : PlatformK8sResourceService, PlatformK8sResourceServiceImpl
 * Related Repository: PlatformK8sResourceRepository
 * Related Entity    : PlatformK8sResource
 * Related DTO       : N/A
 * Related Mapper    : PlatformK8sResourceMapper
 * Related DB Table  : platform_k8s_resource
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformK8sResourceRepository, PlatformK8sResourceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_k8s_resource'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformK8sResource}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_k8s_resource'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_k8s_resource}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_k8s_resource", uniqueConstraints = @UniqueConstraint(columnNames = {"resource_name", "resource_type", "namespace"}))
public class PlatformK8sResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "resource_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String resourceName;

    @Column(name = "resource_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String resourceType;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String namespace;

    @Column(name = "manifest_yaml", columnDefinition = "TEXT")
    private String manifestYaml;

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
     * Retrieves resource name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getResourceName() { return resourceName; }
    /**
     * Performs the setResourceName operation in this module.
     *
     * @param resourceName the resourceName input value
     */
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
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
     * Retrieves namespace data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNamespace() { return namespace; }
    /**
     * Performs the setNamespace operation in this module.
     *
     * @param namespace the namespace input value
     */
    public void setNamespace(String namespace) { this.namespace = namespace; }
    /**
     * Retrieves manifest yaml data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getManifestYaml() { return manifestYaml; }
    /**
     * Performs the setManifestYaml operation in this module.
     *
     * @param manifestYaml the manifestYaml input value
     */
    public void setManifestYaml(String manifestYaml) { this.manifestYaml = manifestYaml; }
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