package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public Integer getNetworkLatencyMs() { return networkLatencyMs; }
    public void setNetworkLatencyMs(Integer networkLatencyMs) { this.networkLatencyMs = networkLatencyMs; }
    public BigDecimal getPacketLossRate() { return packetLossRate; }
    public void setPacketLossRate(BigDecimal packetLossRate) { this.packetLossRate = packetLossRate; }
    public BigDecimal getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(BigDecimal batteryLevel) { this.batteryLevel = batteryLevel; }
    public Long getUptimeSeconds() { return uptimeSeconds; }
    public void setUptimeSeconds(Long uptimeSeconds) { this.uptimeSeconds = uptimeSeconds; }
    public Integer getActiveThreads() { return activeThreads; }
    public void setActiveThreads(Integer activeThreads) { this.activeThreads = activeThreads; }
    public Integer getTelemetryBacklog() { return telemetryBacklog; }
    public void setTelemetryBacklog(Integer telemetryBacklog) { this.telemetryBacklog = telemetryBacklog; }
    public Integer getQueueDepth() { return queueDepth; }
    public void setQueueDepth(Integer queueDepth) { this.queueDepth = queueDepth; }
    public Integer getSyncLagSeconds() { return syncLagSeconds; }
    public void setSyncLagSeconds(Integer syncLagSeconds) { this.syncLagSeconds = syncLagSeconds; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}