/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiFactLoadAudit.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiFactLoadAuditController
 * Related Service   : BiFactLoadAuditService, BiFactLoadAuditServiceImpl
 * Related Repository: BiFactLoadAuditRepository
 * Related Entity    : BiFactLoadAudit
 * Related DTO       : N/A
 * Related Mapper    : BiFactLoadAuditMapper
 * Related DB Table  : bi_fact_load_audit
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiFactLoadAuditRepository, BiFactLoadAuditMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_fact_load_audit'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiFactLoadAudit}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_fact_load_audit'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_fact_load_audit}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_fact_load_audit")
public class BiFactLoadAudit {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
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
    /**
     * Retrieves job run id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getJobRunId() { return jobRunId; } public void setJobRunId(Long v) { this.jobRunId = v; }
    /**
     * Retrieves fact table data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFactTable() { return factTable; } public void setFactTable(String v) { this.factTable = v; }
    /**
     * Retrieves batch id data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBatchId() { return batchId; } public void setBatchId(String v) { this.batchId = v; }
    /**
     * Retrieves rows inserted data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRowsInserted() { return rowsInserted; } public void setRowsInserted(Integer v) { this.rowsInserted = v; }
    /**
     * Retrieves rows updated data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRowsUpdated() { return rowsUpdated; } public void setRowsUpdated(Integer v) { this.rowsUpdated = v; }
    /**
     * Retrieves rows rejected data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRowsRejected() { return rowsRejected; } public void setRowsRejected(Integer v) { this.rowsRejected = v; }
    /**
     * Retrieves load duration ms data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getLoadDurationMs() { return loadDurationMs; } public void setLoadDurationMs(Long v) { this.loadDurationMs = v; }
    /**
     * Retrieves checksum data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChecksum() { return checksum; } public void setChecksum(String v) { this.checksum = v; }
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    /**
     * Retrieves loaded at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLoadedAt() { return loadedAt; }
}