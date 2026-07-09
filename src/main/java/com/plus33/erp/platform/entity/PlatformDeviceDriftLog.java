/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDeviceDriftLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDeviceDriftLogController
 * Related Service   : PlatformDeviceDriftLogService, PlatformDeviceDriftLogServiceImpl
 * Related Repository: PlatformDeviceDriftLogRepository
 * Related Entity    : PlatformDeviceDriftLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformDeviceDriftLogMapper
 * Related DB Table  : platform_device_drift_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDeviceDriftLogRepository, PlatformDeviceDriftLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_device_drift_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDeviceDriftLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_device_drift_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_device_drift_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_device_drift_log")
public class PlatformDeviceDriftLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    @NotNull
    private Long deviceId;

    @Column(name = "baseline_hash", nullable = false)
    @NotNull
    @Size(max = 64)
    private String baselineHash;

    @Column(name = "current_hash", nullable = false)
    @NotNull
    @Size(max = 64)
    private String currentHash;

    @Column(name = "changed_files", columnDefinition = "TEXT")
    private String changedFiles;

    @Column(name = "registry_changes", columnDefinition = "TEXT")
    private String registryChanges;

    @Column(name = "package_changes", columnDefinition = "TEXT")
    private String packageChanges;

    @Column(name = "service_changes", columnDefinition = "TEXT")
    private String serviceChanges;

    @Column(name = "detected_at", nullable = false)
    @NotNull
    private LocalDateTime detectedAt = LocalDateTime.now();

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
     * Retrieves device id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDeviceId() { return deviceId; }
    /**
     * Performs the setDeviceId operation in this module.
     *
     * @param deviceId the deviceId input value
     */
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    /**
     * Retrieves baseline hash data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBaselineHash() { return baselineHash; }
    /**
     * Performs the setBaselineHash operation in this module.
     *
     * @param baselineHash the baselineHash input value
     */
    public void setBaselineHash(String baselineHash) { this.baselineHash = baselineHash; }
    /**
     * Retrieves current hash data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCurrentHash() { return currentHash; }
    /**
     * Performs the setCurrentHash operation in this module.
     *
     * @param currentHash the currentHash input value
     */
    public void setCurrentHash(String currentHash) { this.currentHash = currentHash; }
    /**
     * Retrieves changed files data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChangedFiles() { return changedFiles; }
    /**
     * Performs the setChangedFiles operation in this module.
     *
     * @param changedFiles the changedFiles input value
     */
    public void setChangedFiles(String changedFiles) { this.changedFiles = changedFiles; }
    /**
     * Retrieves registry changes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRegistryChanges() { return registryChanges; }
    /**
     * Performs the setRegistryChanges operation in this module.
     *
     * @param registryChanges the registryChanges input value
     */
    public void setRegistryChanges(String registryChanges) { this.registryChanges = registryChanges; }
    /**
     * Retrieves package changes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPackageChanges() { return packageChanges; }
    /**
     * Performs the setPackageChanges operation in this module.
     *
     * @param packageChanges the packageChanges input value
     */
    public void setPackageChanges(String packageChanges) { this.packageChanges = packageChanges; }
    /**
     * Retrieves service changes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getServiceChanges() { return serviceChanges; }
    /**
     * Performs the setServiceChanges operation in this module.
     *
     * @param serviceChanges the serviceChanges input value
     */
    public void setServiceChanges(String serviceChanges) { this.serviceChanges = serviceChanges; }
    /**
     * Retrieves detected at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getDetectedAt() { return detectedAt; }
    /**
     * Performs the setDetectedAt operation in this module.
     *
     * @param detectedAt the detectedAt input value
     */
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
}