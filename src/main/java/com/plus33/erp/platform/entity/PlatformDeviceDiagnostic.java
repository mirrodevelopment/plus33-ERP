/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDeviceDiagnostic.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDeviceDiagnosticController
 * Related Service   : PlatformDeviceDiagnosticService, PlatformDeviceDiagnosticServiceImpl
 * Related Repository: PlatformDeviceDiagnosticRepository
 * Related Entity    : PlatformDeviceDiagnostic
 * Related DTO       : N/A
 * Related Mapper    : PlatformDeviceDiagnosticMapper
 * Related DB Table  : platform_device_diagnostic
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDeviceDiagnosticRepository, PlatformDeviceDiagnosticMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_device_diagnostic'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDeviceDiagnostic}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_device_diagnostic'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_device_diagnostic}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_device_diagnostic")
public class PlatformDeviceDiagnostic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_id", nullable = false)
    @NotNull
    private Long nodeId;

    @Column(name = "cpu_usage", precision = 5, scale = 2)
    private BigDecimal cpuUsage;

    @Column(name = "memory_usage", precision = 5, scale = 2)
    private BigDecimal memoryUsage;

    @Column(name = "disk_usage", precision = 5, scale = 2)
    private BigDecimal diskUsage;

    @Column(precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(name = "running_services", columnDefinition = "TEXT")
    private String runningServices;

    @Column(name = "firmware_version")
    @Size(max = 50)
    private String firmwareVersion;

    @Column(name = "uptime_seconds")
    private Long uptimeSeconds;

    @Column(name = "network_quality")
    @Size(max = 50)
    private String networkQuality;

    @Column(columnDefinition = "TEXT")
    private String logs;

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(name = "exception_message")
    @Size(max = 500)
    private String exceptionMessage;

    @Column(name = "thread_dump", columnDefinition = "TEXT")
    private String threadDump;

    @Column(name = "core_dump_location")
    @Size(max = 500)
    private String coreDumpLocation;

    @Column(name = "reported_at", nullable = false)
    @NotNull
    private LocalDateTime reportedAt = LocalDateTime.now();

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
     * Retrieves node id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getNodeId() { return nodeId; }
    /**
     * Performs the setNodeId operation in this module.
     *
     * @param nodeId the nodeId input value
     */
    public void setNodeId(Long nodeId) { this.nodeId = nodeId; }
    /**
     * Retrieves cpu usage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCpuUsage() { return cpuUsage; }
    /**
     * Performs the setCpuUsage operation in this module.
     *
     * @param cpuUsage the cpuUsage input value
     */
    public void setCpuUsage(BigDecimal cpuUsage) { this.cpuUsage = cpuUsage; }
    /**
     * Retrieves memory usage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMemoryUsage() { return memoryUsage; }
    /**
     * Performs the setMemoryUsage operation in this module.
     *
     * @param memoryUsage the memoryUsage input value
     */
    public void setMemoryUsage(BigDecimal memoryUsage) { this.memoryUsage = memoryUsage; }
    /**
     * Retrieves disk usage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDiskUsage() { return diskUsage; }
    /**
     * Performs the setDiskUsage operation in this module.
     *
     * @param diskUsage the diskUsage input value
     */
    public void setDiskUsage(BigDecimal diskUsage) { this.diskUsage = diskUsage; }
    /**
     * Retrieves temperature data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTemperature() { return temperature; }
    /**
     * Performs the setTemperature operation in this module.
     *
     * @param temperature the temperature input value
     */
    public void setTemperature(BigDecimal temperature) { this.temperature = temperature; }
    /**
     * Retrieves running services data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRunningServices() { return runningServices; }
    /**
     * Performs the setRunningServices operation in this module.
     *
     * @param runningServices the runningServices input value
     */
    public void setRunningServices(String runningServices) { this.runningServices = runningServices; }
    /**
     * Retrieves firmware version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFirmwareVersion() { return firmwareVersion; }
    /**
     * Performs the setFirmwareVersion operation in this module.
     *
     * @param firmwareVersion the firmwareVersion input value
     */
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    /**
     * Retrieves uptime seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getUptimeSeconds() { return uptimeSeconds; }
    /**
     * Performs the setUptimeSeconds operation in this module.
     *
     * @param uptimeSeconds the uptimeSeconds input value
     */
    public void setUptimeSeconds(Long uptimeSeconds) { this.uptimeSeconds = uptimeSeconds; }
    /**
     * Retrieves network quality data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNetworkQuality() { return networkQuality; }
    /**
     * Performs the setNetworkQuality operation in this module.
     *
     * @param networkQuality the networkQuality input value
     */
    public void setNetworkQuality(String networkQuality) { this.networkQuality = networkQuality; }
    /**
     * Retrieves logs data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLogs() { return logs; }
    /**
     * Performs the setLogs operation in this module.
     *
     * @param logs the logs input value
     */
    public void setLogs(String logs) { this.logs = logs; }
    /**
     * Retrieves stack trace data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStackTrace() { return stackTrace; }
    /**
     * Performs the setStackTrace operation in this module.
     *
     * @param stackTrace the stackTrace input value
     */
    public void setStackTrace(String stackTrace) { this.stackTrace = stackTrace; }
    /**
     * Retrieves exception message data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExceptionMessage() { return exceptionMessage; }
    /**
     * Performs the setExceptionMessage operation in this module.
     *
     * @param exceptionMessage the exceptionMessage input value
     */
    public void setExceptionMessage(String exceptionMessage) { this.exceptionMessage = exceptionMessage; }
    /**
     * Retrieves thread dump data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getThreadDump() { return threadDump; }
    /**
     * Performs the setThreadDump operation in this module.
     *
     * @param threadDump the threadDump input value
     */
    public void setThreadDump(String threadDump) { this.threadDump = threadDump; }
    /**
     * Retrieves core dump location data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCoreDumpLocation() { return coreDumpLocation; }
    /**
     * Performs the setCoreDumpLocation operation in this module.
     *
     * @param coreDumpLocation the coreDumpLocation input value
     */
    public void setCoreDumpLocation(String coreDumpLocation) { this.coreDumpLocation = coreDumpLocation; }
    /**
     * Retrieves reported at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getReportedAt() { return reportedAt; }
    /**
     * Performs the setReportedAt operation in this module.
     *
     * @param reportedAt the reportedAt input value
     */
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
}