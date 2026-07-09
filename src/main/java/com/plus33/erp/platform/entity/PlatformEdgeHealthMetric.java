/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEdgeHealthMetric.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEdgeHealthMetricController
 * Related Service   : PlatformEdgeHealthMetricService, PlatformEdgeHealthMetricServiceImpl
 * Related Repository: PlatformEdgeHealthMetricRepository
 * Related Entity    : PlatformEdgeHealthMetric
 * Related DTO       : N/A
 * Related Mapper    : PlatformEdgeHealthMetricMapper
 * Related DB Table  : platform_edge_health_metric
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEdgeHealthMetricRepository, PlatformEdgeHealthMetricMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_edge_health_metric'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEdgeHealthMetric}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_edge_health_metric'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_edge_health_metric}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_edge_health_metric")
public class PlatformEdgeHealthMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_id", nullable = false)
    @NotNull
    private Long nodeId;

    @Column(name = "cpu_usage", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal cpuUsage;

    @Column(name = "memory_usage", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal memoryUsage;

    @Column(name = "disk_usage", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal diskUsage;

    @Column(precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(name = "network_latency_ms")
    private Integer networkLatencyMs;

    @Column(name = "packet_loss_rate", precision = 5, scale = 2)
    private BigDecimal packetLossRate;

    @Column(name = "battery_level", precision = 5, scale = 2)
    private BigDecimal batteryLevel;

    @Column(name = "uptime_seconds", nullable = false)
    @NotNull
    private Long uptimeSeconds;

    @Column(name = "active_threads", nullable = false)
    @NotNull
    private Integer activeThreads;

    @Column(name = "telemetry_backlog", nullable = false)
    @NotNull
    private Integer telemetryBacklog = 0;

    @Column(name = "queue_depth", nullable = false)
    @NotNull
    private Integer queueDepth = 0;

    @Column(name = "sync_lag_seconds", nullable = false)
    @NotNull
    private Integer syncLagSeconds = 0;

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
     * Retrieves network latency ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getNetworkLatencyMs() { return networkLatencyMs; }
    /**
     * Performs the setNetworkLatencyMs operation in this module.
     *
     * @param networkLatencyMs the networkLatencyMs input value
     */
    public void setNetworkLatencyMs(Integer networkLatencyMs) { this.networkLatencyMs = networkLatencyMs; }
    /**
     * Retrieves packet loss rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPacketLossRate() { return packetLossRate; }
    /**
     * Performs the setPacketLossRate operation in this module.
     *
     * @param packetLossRate the packetLossRate input value
     */
    public void setPacketLossRate(BigDecimal packetLossRate) { this.packetLossRate = packetLossRate; }
    /**
     * Retrieves battery level data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getBatteryLevel() { return batteryLevel; }
    /**
     * Performs the setBatteryLevel operation in this module.
     *
     * @param batteryLevel the batteryLevel input value
     */
    public void setBatteryLevel(BigDecimal batteryLevel) { this.batteryLevel = batteryLevel; }
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
     * Retrieves active threads data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getActiveThreads() { return activeThreads; }
    /**
     * Performs the setActiveThreads operation in this module.
     *
     * @param activeThreads the activeThreads input value
     */
    public void setActiveThreads(Integer activeThreads) { this.activeThreads = activeThreads; }
    /**
     * Retrieves telemetry backlog data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getTelemetryBacklog() { return telemetryBacklog; }
    /**
     * Performs the setTelemetryBacklog operation in this module.
     *
     * @param telemetryBacklog the telemetryBacklog input value
     */
    public void setTelemetryBacklog(Integer telemetryBacklog) { this.telemetryBacklog = telemetryBacklog; }
    /**
     * Retrieves queue depth data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getQueueDepth() { return queueDepth; }
    /**
     * Performs the setQueueDepth operation in this module.
     *
     * @param queueDepth the queueDepth input value
     */
    public void setQueueDepth(Integer queueDepth) { this.queueDepth = queueDepth; }
    /**
     * Retrieves sync lag seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getSyncLagSeconds() { return syncLagSeconds; }
    /**
     * Performs the setSyncLagSeconds operation in this module.
     *
     * @param syncLagSeconds the syncLagSeconds input value
     */
    public void setSyncLagSeconds(Integer syncLagSeconds) { this.syncLagSeconds = syncLagSeconds; }
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