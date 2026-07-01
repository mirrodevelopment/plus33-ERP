package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public Integer getRecordsSynced() { return recordsSynced; }
    public void setRecordsSynced(Integer recordsSynced) { this.recordsSynced = recordsSynced; }
    public Long getLatencyMs() { return latencyMs; }
    public void setLatencyMs(Long latencyMs) { this.latencyMs = latencyMs; }
    public String getSyncStatus() { return syncStatus; }
    public void setSyncStatus(String syncStatus) { this.syncStatus = syncStatus; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}