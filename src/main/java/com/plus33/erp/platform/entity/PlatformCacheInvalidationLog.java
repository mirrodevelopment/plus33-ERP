/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformCacheInvalidationLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCacheInvalidationLogController
 * Related Service   : PlatformCacheInvalidationLogService, PlatformCacheInvalidationLogServiceImpl
 * Related Repository: PlatformCacheInvalidationLogRepository
 * Related Entity    : PlatformCacheInvalidationLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformCacheInvalidationLogMapper
 * Related DB Table  : platform_cache_invalidation_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCacheInvalidationLogRepository, PlatformCacheInvalidationLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_cache_invalidation_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformCacheInvalidationLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_cache_invalidation_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_cache_invalidation_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_cache_invalidation_log")
public class PlatformCacheInvalidationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "namespace_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String namespaceName;

    @Column(name = "cache_key", nullable = false)
    @NotNull
    @Size(max = 250)
    private String cacheKey;

    @Column(name = "invalidated_at", nullable = false)
    @NotNull
    private LocalDateTime invalidatedAt = LocalDateTime.now();

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
     * Retrieves cache key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCacheKey() { return cacheKey; }
    /**
     * Performs the setCacheKey operation in this module.
     *
     * @param cacheKey the cacheKey input value
     */
    public void setCacheKey(String cacheKey) { this.cacheKey = cacheKey; }
    /**
     * Retrieves invalidated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getInvalidatedAt() { return invalidatedAt; }
    /**
     * Performs the setInvalidatedAt operation in this module.
     *
     * @param invalidatedAt the invalidatedAt input value
     */
    public void setInvalidatedAt(LocalDateTime invalidatedAt) { this.invalidatedAt = invalidatedAt; }
}