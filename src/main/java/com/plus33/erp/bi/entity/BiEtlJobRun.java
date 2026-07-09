/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiEtlJobRun.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiEtlJobRunController
 * Related Service   : BiEtlJobRunService, BiEtlJobRunServiceImpl
 * Related Repository: BiEtlJobRunRepository
 * Related Entity    : BiEtlJobRun
 * Related DTO       : N/A
 * Related Mapper    : BiEtlJobRunMapper
 * Related DB Table  : bi_etl_job_run
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiEtlJobRunRepository, BiEtlJobRunMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_etl_job_run'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiEtlJobRun}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_etl_job_run'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_etl_job_run}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_etl_job_run")
public class BiEtlJobRun {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
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
    /**
     * Retrieves job id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getJobId() { return jobId; } public void setJobId(Long v) { this.jobId = v; }
    /**
     * Retrieves run number data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRunNumber() { return runNumber; } public void setRunNumber(Integer v) { this.runNumber = v; }
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    /**
     * Retrieves started at data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartedAt() { return startedAt; } public void setStartedAt(LocalDateTime v) { this.startedAt = v; }
    /**
     * Retrieves completed at data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletedAt() { return completedAt; } public void setCompletedAt(LocalDateTime v) { this.completedAt = v; }
    /**
     * Retrieves duration ms data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDurationMs() { return durationMs; } public void setDurationMs(Long v) { this.durationMs = v; }
    /**
     * Retrieves records extracted data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRecordsExtracted() { return recordsExtracted; } public void setRecordsExtracted(Integer v) { this.recordsExtracted = v; }
    /**
     * Retrieves records processed data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRecordsProcessed() { return recordsProcessed; } public void setRecordsProcessed(Integer v) { this.recordsProcessed = v; }
    /**
     * Retrieves records rejected data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRecordsRejected() { return recordsRejected; } public void setRecordsRejected(Integer v) { this.recordsRejected = v; }
    /**
     * Retrieves watermark from data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWatermarkFrom() { return watermarkFrom; } public void setWatermarkFrom(Long v) { this.watermarkFrom = v; }
    /**
     * Retrieves watermark to data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWatermarkTo() { return watermarkTo; } public void setWatermarkTo(Long v) { this.watermarkTo = v; }
    /**
     * Retrieves batch id data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBatchId() { return batchId; } public void setBatchId(String v) { this.batchId = v; }
    /**
     * Retrieves error message data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getErrorMessage() { return errorMessage; } public void setErrorMessage(String v) { this.errorMessage = v; }
    /**
     * Retrieves retry count data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRetryCount() { return retryCount; } public void setRetryCount(Integer v) { this.retryCount = v; }
    /**
     * Retrieves triggered by data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTriggeredBy() { return triggeredBy; } public void setTriggeredBy(String v) { this.triggeredBy = v; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}