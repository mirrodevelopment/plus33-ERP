/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformBackupRun.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformBackupRunController
 * Related Service   : PlatformBackupRunService, PlatformBackupRunServiceImpl
 * Related Repository: PlatformBackupRunRepository
 * Related Entity    : PlatformBackupRun
 * Related DTO       : N/A
 * Related Mapper    : PlatformBackupRunMapper
 * Related DB Table  : platform_backup_run
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformBackupRunRepository, PlatformBackupRunMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_backup_run'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformBackupRun}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_backup_run'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_backup_run}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_backup_run")
public class PlatformBackupRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "backup_file", nullable = false)
    @NotNull
    @Size(max = 250)
    private String backupFile;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status;

    @Column(name = "size_bytes", nullable = false)
    @NotNull
    private Long sizeBytes = 0L;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String checksum;

    @Column(name = "sandbox_restored", nullable = false)
    @NotNull
    private Boolean sandboxRestored = false;

    @Column(name = "integrity_checked", nullable = false)
    @NotNull
    private Boolean integrityChecked = false;

    @Column(name = "integrity_message", columnDefinition = "TEXT")
    private String integrityMessage;

    @Column(name = "completed_at", nullable = false)
    @NotNull
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves backup file data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBackupFile() { return backupFile; }
    /**
     * Performs the setBackupFile operation in this module.
     *
     * @param backupFile the backupFile input value
     */
    public void setBackupFile(String backupFile) { this.backupFile = backupFile; }
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
     * Retrieves size bytes data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSizeBytes() { return sizeBytes; }
    /**
     * Performs the setSizeBytes operation in this module.
     *
     * @param sizeBytes the sizeBytes input value
     */
    public void setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; }
    /**
     * Retrieves checksum data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChecksum() { return checksum; }
    /**
     * Performs the setChecksum operation in this module.
     *
     * @param checksum the checksum input value
     */
    public void setChecksum(String checksum) { this.checksum = checksum; }
    /**
     * Retrieves sandbox restored data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getSandboxRestored() { return sandboxRestored; }
    /**
     * Performs the setSandboxRestored operation in this module.
     *
     * @param sandboxRestored the sandboxRestored input value
     */
    public void setSandboxRestored(Boolean sandboxRestored) { this.sandboxRestored = sandboxRestored; }
    /**
     * Retrieves integrity checked data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIntegrityChecked() { return integrityChecked; }
    /**
     * Performs the setIntegrityChecked operation in this module.
     *
     * @param integrityChecked the integrityChecked input value
     */
    public void setIntegrityChecked(Boolean integrityChecked) { this.integrityChecked = integrityChecked; }
    /**
     * Retrieves integrity message data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIntegrityMessage() { return integrityMessage; }
    /**
     * Performs the setIntegrityMessage operation in this module.
     *
     * @param integrityMessage the integrityMessage input value
     */
    public void setIntegrityMessage(String integrityMessage) { this.integrityMessage = integrityMessage; }
    /**
     * Retrieves completed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletedAt() { return completedAt; }
    /**
     * Performs the setCompletedAt operation in this module.
     *
     * @param completedAt the completedAt input value
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
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
}