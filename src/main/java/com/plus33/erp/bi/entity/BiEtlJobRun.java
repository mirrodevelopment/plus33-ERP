package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_etl_job_run")
public class BiEtlJobRun {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "job_id", nullable = false) private Long jobId;
    @Column(name = "run_number", nullable = false) private Integer runNumber = 1;
    @Column(nullable = false, length = 30) private String status = "CREATED";
    @Column(name = "started_at") private LocalDateTime startedAt;
    @Column(name = "completed_at") private LocalDateTime completedAt;
    @Column(name = "duration_ms") private Long durationMs;
    @Column(name = "records_extracted") private Integer recordsExtracted = 0;
    @Column(name = "records_processed") private Integer recordsProcessed = 0;
    @Column(name = "records_rejected") private Integer recordsRejected = 0;
    @Column(name = "watermark_from") private Long watermarkFrom;
    @Column(name = "watermark_to") private Long watermarkTo;
    @Column(name = "batch_id", length = 100) private String batchId;
    @Column(name = "error_message", columnDefinition = "TEXT") private String errorMessage;
    @Column(name = "retry_count", nullable = false) private Integer retryCount = 0;
    @Column(name = "triggered_by", length = 100) private String triggeredBy;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getJobId() { return jobId; } public void setJobId(Long v) { this.jobId = v; }
    public Integer getRunNumber() { return runNumber; } public void setRunNumber(Integer v) { this.runNumber = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public LocalDateTime getStartedAt() { return startedAt; } public void setStartedAt(LocalDateTime v) { this.startedAt = v; }
    public LocalDateTime getCompletedAt() { return completedAt; } public void setCompletedAt(LocalDateTime v) { this.completedAt = v; }
    public Long getDurationMs() { return durationMs; } public void setDurationMs(Long v) { this.durationMs = v; }
    public Integer getRecordsExtracted() { return recordsExtracted; } public void setRecordsExtracted(Integer v) { this.recordsExtracted = v; }
    public Integer getRecordsProcessed() { return recordsProcessed; } public void setRecordsProcessed(Integer v) { this.recordsProcessed = v; }
    public Integer getRecordsRejected() { return recordsRejected; } public void setRecordsRejected(Integer v) { this.recordsRejected = v; }
    public Long getWatermarkFrom() { return watermarkFrom; } public void setWatermarkFrom(Long v) { this.watermarkFrom = v; }
    public Long getWatermarkTo() { return watermarkTo; } public void setWatermarkTo(Long v) { this.watermarkTo = v; }
    public String getBatchId() { return batchId; } public void setBatchId(String v) { this.batchId = v; }
    public String getErrorMessage() { return errorMessage; } public void setErrorMessage(String v) { this.errorMessage = v; }
    public Integer getRetryCount() { return retryCount; } public void setRetryCount(Integer v) { this.retryCount = v; }
    public String getTriggeredBy() { return triggeredBy; } public void setTriggeredBy(String v) { this.triggeredBy = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
