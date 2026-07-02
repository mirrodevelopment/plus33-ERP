package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getNodeId() { return nodeId; }
    public void setNodeId(Long nodeId) { this.nodeId = nodeId; }
    public BigDecimal getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(BigDecimal cpuUsage) { this.cpuUsage = cpuUsage; }
    public BigDecimal getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(BigDecimal memoryUsage) { this.memoryUsage = memoryUsage; }
    public BigDecimal getDiskUsage() { return diskUsage; }
    public void setDiskUsage(BigDecimal diskUsage) { this.diskUsage = diskUsage; }
    public BigDecimal getTemperature() { return temperature; }
    public void setTemperature(BigDecimal temperature) { this.temperature = temperature; }
    public String getRunningServices() { return runningServices; }
    public void setRunningServices(String runningServices) { this.runningServices = runningServices; }
    public String getFirmwareVersion() { return firmwareVersion; }
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    public Long getUptimeSeconds() { return uptimeSeconds; }
    public void setUptimeSeconds(Long uptimeSeconds) { this.uptimeSeconds = uptimeSeconds; }
    public String getNetworkQuality() { return networkQuality; }
    public void setNetworkQuality(String networkQuality) { this.networkQuality = networkQuality; }
    public String getLogs() { return logs; }
    public void setLogs(String logs) { this.logs = logs; }
    public String getStackTrace() { return stackTrace; }
    public void setStackTrace(String stackTrace) { this.stackTrace = stackTrace; }
    public String getExceptionMessage() { return exceptionMessage; }
    public void setExceptionMessage(String exceptionMessage) { this.exceptionMessage = exceptionMessage; }
    public String getThreadDump() { return threadDump; }
    public void setThreadDump(String threadDump) { this.threadDump = threadDump; }
    public String getCoreDumpLocation() { return coreDumpLocation; }
    public void setCoreDumpLocation(String coreDumpLocation) { this.coreDumpLocation = coreDumpLocation; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
}