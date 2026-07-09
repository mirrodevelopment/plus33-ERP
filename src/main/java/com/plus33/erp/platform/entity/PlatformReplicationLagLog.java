/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformReplicationLagLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformReplicationLagLogController
 * Related Service   : PlatformReplicationLagLogService, PlatformReplicationLagLogServiceImpl
 * Related Repository: PlatformReplicationLagLogRepository
 * Related Entity    : PlatformReplicationLagLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformReplicationLagLogMapper
 * Related DB Table  : platform_replication_lag_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformReplicationLagLogRepository, PlatformReplicationLagLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_replication_lag_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformReplicationLagLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_replication_lag_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_replication_lag_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_replication_lag_log")
public class PlatformReplicationLagLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region_code", nullable = false)
    @NotNull
    @Size(max = 50)
    private String regionCode;

    @Column(name = "lag_ms", nullable = false)
    @NotNull
    private Long lagMs;

    @Column(name = "recorded_at", nullable = false)
    @NotNull
    private LocalDateTime recordedAt = LocalDateTime.now();

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
     * Retrieves lag ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getLagMs() { return lagMs; }
    /**
     * Performs the setLagMs operation in this module.
     *
     * @param lagMs the lagMs input value
     */
    public void setLagMs(Long lagMs) { this.lagMs = lagMs; }
    /**
     * Retrieves recorded at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRecordedAt() { return recordedAt; }
    /**
     * Performs the setRecordedAt operation in this module.
     *
     * @param recordedAt the recordedAt input value
     */
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}