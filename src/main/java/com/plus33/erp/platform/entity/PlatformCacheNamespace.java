/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformCacheNamespace.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCacheNamespaceController
 * Related Service   : PlatformCacheNamespaceService, PlatformCacheNamespaceServiceImpl
 * Related Repository: PlatformCacheNamespaceRepository
 * Related Entity    : PlatformCacheNamespace
 * Related DTO       : N/A
 * Related Mapper    : PlatformCacheNamespaceMapper
 * Related DB Table  : platform_cache_namespace
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCacheNamespaceRepository, PlatformCacheNamespaceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_cache_namespace'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformCacheNamespace}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_cache_namespace'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_cache_namespace}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_cache_namespace")
public class PlatformCacheNamespace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "namespace_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String namespaceName;

    @Column(name = "ttl_seconds", nullable = false)
    @NotNull
    private Integer ttlSeconds = 3600;

    @Column(name = "eviction_policy", nullable = false)
    @NotNull
    @Size(max = 50)
    private String evictionPolicy = "LRU";

    @Column(name = "compression_enabled", nullable = false)
    @NotNull
    private Boolean compressionEnabled = false;

    @Column(name = "serialization_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String serializationType = "JSON";

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
     * Retrieves namespace name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNamespaceName() { return namespaceName; }
    /**
     * Performs the setNamespaceName operation in this module.
     *
     * @param namespaceName the namespaceName input value
     */
    public void setNamespaceName(String namespaceName) { this.namespaceName = namespaceName; }
    /**
     * Retrieves ttl seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getTtlSeconds() { return ttlSeconds; }
    /**
     * Performs the setTtlSeconds operation in this module.
     *
     * @param ttlSeconds the ttlSeconds input value
     */
    public void setTtlSeconds(Integer ttlSeconds) { this.ttlSeconds = ttlSeconds; }
    /**
     * Retrieves eviction policy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEvictionPolicy() { return evictionPolicy; }
    /**
     * Performs the setEvictionPolicy operation in this module.
     *
     * @param evictionPolicy the evictionPolicy input value
     */
    public void setEvictionPolicy(String evictionPolicy) { this.evictionPolicy = evictionPolicy; }
    /**
     * Retrieves compression enabled data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getCompressionEnabled() { return compressionEnabled; }
    /**
     * Performs the setCompressionEnabled operation in this module.
     *
     * @param compressionEnabled the compressionEnabled input value
     */
    public void setCompressionEnabled(Boolean compressionEnabled) { this.compressionEnabled = compressionEnabled; }
    /**
     * Retrieves serialization type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSerializationType() { return serializationType; }
    /**
     * Performs the setSerializationType operation in this module.
     *
     * @param serializationType the serializationType input value
     */
    public void setSerializationType(String serializationType) { this.serializationType = serializationType; }
}