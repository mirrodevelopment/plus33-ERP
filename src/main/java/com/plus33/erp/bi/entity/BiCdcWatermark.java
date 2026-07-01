package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_cdc_watermark")
public class BiCdcWatermark {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "source_module", nullable = false, length = 100) private String sourceModule;
    @Column(name = "source_table", nullable = false, length = 100) private String sourceTable;
    @Column(name = "last_event_id") private Long lastEventId;
    @Column(name = "last_timestamp") private LocalDateTime lastTimestamp;
    @Column(name = "last_run") private LocalDateTime lastRun;
    @Column(nullable = false, length = 30) private String status = "ACTIVE";
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getSourceModule() { return sourceModule; } public void setSourceModule(String v) { this.sourceModule = v; }
    public String getSourceTable() { return sourceTable; } public void setSourceTable(String v) { this.sourceTable = v; }
    public Long getLastEventId() { return lastEventId; } public void setLastEventId(Long v) { this.lastEventId = v; }
    public LocalDateTime getLastTimestamp() { return lastTimestamp; } public void setLastTimestamp(LocalDateTime v) { this.lastTimestamp = v; }
    public LocalDateTime getLastRun() { return lastRun; } public void setLastRun(LocalDateTime v) { this.lastRun = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
