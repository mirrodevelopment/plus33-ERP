/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformMulticloudSyncHistory.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformMulticloudSyncHistoryController
 * Related Service   : PlatformMulticloudSyncHistoryService, PlatformMulticloudSyncHistoryServiceImpl
 * Related Repository: PlatformMulticloudSyncHistoryRepository
 * Related Entity    : PlatformMulticloudSyncHistory
 * Related DTO       : N/A
 * Related Mapper    : PlatformMulticloudSyncHistoryMapper
 * Related DB Table  : platform_multicloud_sync_history
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformMulticloudSyncHistoryRepository, PlatformMulticloudSyncHistoryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_multicloud_sync_history'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformMulticloudSyncHistory}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_multicloud_sync_history'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_multicloud_sync_history}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_multicloud_sync_history")
public class PlatformMulticloudSyncHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String providerName;

    @Column(name = "records_synced", nullable = false)
    @NotNull
    private Integer recordsSynced;

    @Column(name = "latency_ms", nullable = false)
    @NotNull
    private Long latencyMs;

    @Column(name = "sync_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String syncStatus = "SUCCESS";

    @Column(nullable = false)
    @NotNull
    private LocalDateTime timestamp = LocalDateTime.now();

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
     * Retrieves provider name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProviderName() { return providerName; }
    /**
     * Performs the setProviderName operation in this module.
     *
     * @param providerName the providerName input value
     */
    public void setProviderName(String providerName) { this.providerName = providerName; }
    /**
     * Retrieves records synced data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRecordsSynced() { return recordsSynced; }
    /**
     * Performs the setRecordsSynced operation in this module.
     *
     * @param recordsSynced the recordsSynced input value
     */
    public void setRecordsSynced(Integer recordsSynced) { this.recordsSynced = recordsSynced; }
    /**
     * Retrieves latency ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getLatencyMs() { return latencyMs; }
    /**
     * Performs the setLatencyMs operation in this module.
     *
     * @param latencyMs the latencyMs input value
     */
    public void setLatencyMs(Long latencyMs) { this.latencyMs = latencyMs; }
    /**
     * Retrieves sync status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSyncStatus() { return syncStatus; }
    /**
     * Performs the setSyncStatus operation in this module.
     *
     * @param syncStatus the syncStatus input value
     */
    public void setSyncStatus(String syncStatus) { this.syncStatus = syncStatus; }
    /**
     * Retrieves timestamp data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getTimestamp() { return timestamp; }
    /**
     * Performs the setTimestamp operation in this module.
     *
     * @param timestamp the timestamp input value
     */
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}