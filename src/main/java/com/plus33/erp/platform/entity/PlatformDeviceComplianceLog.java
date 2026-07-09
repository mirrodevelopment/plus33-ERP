/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDeviceComplianceLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDeviceComplianceLogController
 * Related Service   : PlatformDeviceComplianceLogService, PlatformDeviceComplianceLogServiceImpl
 * Related Repository: PlatformDeviceComplianceLogRepository
 * Related Entity    : PlatformDeviceComplianceLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformDeviceComplianceLogMapper
 * Related DB Table  : platform_device_compliance_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDeviceComplianceLogRepository, PlatformDeviceComplianceLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_device_compliance_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDeviceComplianceLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_device_compliance_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_device_compliance_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_device_compliance_log")
public class PlatformDeviceComplianceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    @NotNull
    private Long deviceId;

    @Column(name = "policy_id", nullable = false)
    @NotNull
    private Long policyId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String result; // PASS, FAIL, WARNING, UNKNOWN

    @Column(name = "execution_time", nullable = false)
    @NotNull
    private LocalDateTime executionTime = LocalDateTime.now();

    @Column(name = "duration_ms", nullable = false)
    @NotNull
    private Long durationMs;

    @Column(columnDefinition = "TEXT")
    private String details;

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
     * Retrieves policy id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPolicyId() { return policyId; }
    /**
     * Performs the setPolicyId operation in this module.
     *
     * @param policyId the policyId input value
     */
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    /**
     * Retrieves result data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getResult() { return result; }
    /**
     * Performs the setResult operation in this module.
     *
     * @param result the result input value
     */
    public void setResult(String result) { this.result = result; }
    /**
     * Retrieves execution time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getExecutionTime() { return executionTime; }
    /**
     * Performs the setExecutionTime operation in this module.
     *
     * @param executionTime the executionTime input value
     */
    public void setExecutionTime(LocalDateTime executionTime) { this.executionTime = executionTime; }
    /**
     * Retrieves duration ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDurationMs() { return durationMs; }
    /**
     * Performs the setDurationMs operation in this module.
     *
     * @param durationMs the durationMs input value
     */
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    /**
     * Retrieves details data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDetails() { return details; }
    /**
     * Performs the setDetails operation in this module.
     *
     * @param details the details input value
     */
    public void setDetails(String details) { this.details = details; }
}