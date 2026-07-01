package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_fact_load_audit")
public class BiFactLoadAudit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "job_run_id") private Long jobRunId;
    @Column(name = "fact_table", nullable = false, length = 100) private String factTable;
    @Column(name = "batch_id", length = 100) private String batchId;
    @Column(name = "rows_inserted") private Integer rowsInserted = 0;
    @Column(name = "rows_updated") private Integer rowsUpdated = 0;
    @Column(name = "rows_rejected") private Integer rowsRejected = 0;
    @Column(name = "load_duration_ms") private Long loadDurationMs;
    @Column(length = 200) private String checksum;
    @Column(nullable = false, length = 30) private String status = "COMPLETED";
    @Column(name = "loaded_at", nullable = false, updatable = false) private LocalDateTime loadedAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getJobRunId() { return jobRunId; } public void setJobRunId(Long v) { this.jobRunId = v; }
    public String getFactTable() { return factTable; } public void setFactTable(String v) { this.factTable = v; }
    public String getBatchId() { return batchId; } public void setBatchId(String v) { this.batchId = v; }
    public Integer getRowsInserted() { return rowsInserted; } public void setRowsInserted(Integer v) { this.rowsInserted = v; }
    public Integer getRowsUpdated() { return rowsUpdated; } public void setRowsUpdated(Integer v) { this.rowsUpdated = v; }
    public Integer getRowsRejected() { return rowsRejected; } public void setRowsRejected(Integer v) { this.rowsRejected = v; }
    public Long getLoadDurationMs() { return loadDurationMs; } public void setLoadDurationMs(Long v) { this.loadDurationMs = v; }
    public String getChecksum() { return checksum; } public void setChecksum(String v) { this.checksum = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public LocalDateTime getLoadedAt() { return loadedAt; }
}
