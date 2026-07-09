/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformCacheRegion.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCacheRegionController
 * Related Service   : PlatformCacheRegionService, PlatformCacheRegionServiceImpl
 * Related Repository: PlatformCacheRegionRepository
 * Related Entity    : PlatformCacheRegion
 * Related DTO       : N/A
 * Related Mapper    : PlatformCacheRegionMapper
 * Related DB Table  : platform_cache_region
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCacheRegionRepository, PlatformCacheRegionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_cache_region'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformCacheRegion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_cache_region'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_cache_region}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_cache_region")
public class PlatformCacheRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "region_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String regionName;

    @Column(name = "replication_mode", nullable = false)
    @NotNull
    @Size(max = 50)
    private String replicationMode = "ASYNCHRONOUS";

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
     * Retrieves region name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRegionName() { return regionName; }
    /**
     * Performs the setRegionName operation in this module.
     *
     * @param regionName the regionName input value
     */
    public void setRegionName(String regionName) { this.regionName = regionName; }
    /**
     * Retrieves replication mode data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReplicationMode() { return replicationMode; }
    /**
     * Performs the setReplicationMode operation in this module.
     *
     * @param replicationMode the replicationMode input value
     */
    public void setReplicationMode(String replicationMode) { this.replicationMode = replicationMode; }
}