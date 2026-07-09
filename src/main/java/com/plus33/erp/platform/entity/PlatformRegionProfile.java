/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformRegionProfile.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRegionProfileController
 * Related Service   : PlatformRegionProfileService, PlatformRegionProfileServiceImpl
 * Related Repository: PlatformRegionProfileRepository
 * Related Entity    : PlatformRegionProfile
 * Related DTO       : N/A
 * Related Mapper    : PlatformRegionProfileMapper
 * Related DB Table  : platform_region_profile
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRegionProfileRepository, PlatformRegionProfileMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_region_profile'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformRegionProfile}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_region_profile'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_region_profile}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_region_profile")
public class PlatformRegionProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "region_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 50)
    private String regionCode;

    @Column(name = "health_score", nullable = false)
    @NotNull
    private Integer healthScore = 100;

    @Column(name = "cpu_utilization", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal cpuUtilization = BigDecimal.ZERO;

    @Column(name = "memory_utilization", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal memoryUtilization = BigDecimal.ZERO;

    @Column(name = "network_rtt_ms", nullable = false)
    @NotNull
    private Integer networkRttMs = 5;

    @Column(name = "failed_queries", nullable = false)
    @NotNull
    private Integer failedQueries = 0;

    @Column(name = "disk_utilization", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal diskUtilization = BigDecimal.ZERO;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

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
     * Retrieves region code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRegionCode() { return regionCode; }
    /**
     * Performs the setRegionCode operation in this module.
     *
     * @param regionCode the regionCode input value
     */
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
    /**
     * Retrieves health score data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getHealthScore() { return healthScore; }
    /**
     * Performs the setHealthScore operation in this module.
     *
     * @param healthScore the healthScore input value
     */
    public void setHealthScore(Integer healthScore) { this.healthScore = healthScore; }
    /**
     * Retrieves cpu utilization data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCpuUtilization() { return cpuUtilization; }
    /**
     * Performs the setCpuUtilization operation in this module.
     *
     * @param cpuUtilization the cpuUtilization input value
     */
    public void setCpuUtilization(BigDecimal cpuUtilization) { this.cpuUtilization = cpuUtilization; }
    /**
     * Retrieves memory utilization data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMemoryUtilization() { return memoryUtilization; }
    /**
     * Performs the setMemoryUtilization operation in this module.
     *
     * @param memoryUtilization the memoryUtilization input value
     */
    public void setMemoryUtilization(BigDecimal memoryUtilization) { this.memoryUtilization = memoryUtilization; }
    /**
     * Retrieves network rtt ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getNetworkRttMs() { return networkRttMs; }
    /**
     * Performs the setNetworkRttMs operation in this module.
     *
     * @param networkRttMs the networkRttMs input value
     */
    public void setNetworkRttMs(Integer networkRttMs) { this.networkRttMs = networkRttMs; }
    /**
     * Retrieves failed queries data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getFailedQueries() { return failedQueries; }
    /**
     * Performs the setFailedQueries operation in this module.
     *
     * @param failedQueries the failedQueries input value
     */
    public void setFailedQueries(Integer failedQueries) { this.failedQueries = failedQueries; }
    /**
     * Retrieves disk utilization data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDiskUtilization() { return diskUtilization; }
    /**
     * Performs the setDiskUtilization operation in this module.
     *
     * @param diskUtilization the diskUtilization input value
     */
    public void setDiskUtilization(BigDecimal diskUtilization) { this.diskUtilization = diskUtilization; }
    /**
     * Retrieves active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(Boolean active) { this.active = active; }
}