/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTelemetryArchiveLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTelemetryArchiveLogController
 * Related Service   : PlatformTelemetryArchiveLogService, PlatformTelemetryArchiveLogServiceImpl
 * Related Repository: PlatformTelemetryArchiveLogRepository
 * Related Entity    : PlatformTelemetryArchiveLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformTelemetryArchiveLogMapper
 * Related DB Table  : platform_telemetry_archive_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTelemetryArchiveLogRepository, PlatformTelemetryArchiveLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_telemetry_archive_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTelemetryArchiveLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_telemetry_archive_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_telemetry_archive_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_telemetry_archive_log")
public class PlatformTelemetryArchiveLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instance_id", nullable = false)
    @NotNull
    private Long instanceId;

    @Column(name = "records_archived", nullable = false)
    @NotNull
    private Integer recordsArchived;

    @Column(name = "archive_key", nullable = false)
    @NotNull
    @Size(max = 250)
    private String archiveKey;

    @Column(name = "archived_at", nullable = false)
    @NotNull
    private LocalDateTime archivedAt = LocalDateTime.now();

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
     * Retrieves instance id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getInstanceId() { return instanceId; }
    /**
     * Performs the setInstanceId operation in this module.
     *
     * @param instanceId the instanceId input value
     */
    public void setInstanceId(Long instanceId) { this.instanceId = instanceId; }
    /**
     * Retrieves records archived data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRecordsArchived() { return recordsArchived; }
    /**
     * Performs the setRecordsArchived operation in this module.
     *
     * @param recordsArchived the recordsArchived input value
     */
    public void setRecordsArchived(Integer recordsArchived) { this.recordsArchived = recordsArchived; }
    /**
     * Retrieves archive key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getArchiveKey() { return archiveKey; }
    /**
     * Performs the setArchiveKey operation in this module.
     *
     * @param archiveKey the archiveKey input value
     */
    public void setArchiveKey(String archiveKey) { this.archiveKey = archiveKey; }
    /**
     * Retrieves archived at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getArchivedAt() { return archivedAt; }
    /**
     * Performs the setArchivedAt operation in this module.
     *
     * @param archivedAt the archivedAt input value
     */
    public void setArchivedAt(LocalDateTime archivedAt) { this.archivedAt = archivedAt; }
}