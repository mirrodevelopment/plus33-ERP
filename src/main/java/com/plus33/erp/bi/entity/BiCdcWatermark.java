/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiCdcWatermark.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiCdcWatermarkController
 * Related Service   : BiCdcWatermarkService, BiCdcWatermarkServiceImpl
 * Related Repository: BiCdcWatermarkRepository
 * Related Entity    : BiCdcWatermark
 * Related DTO       : N/A
 * Related Mapper    : BiCdcWatermarkMapper
 * Related DB Table  : bi_cdc_watermark
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiCdcWatermarkRepository, BiCdcWatermarkMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_cdc_watermark'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiCdcWatermark}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_cdc_watermark'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_cdc_watermark}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_cdc_watermark")
public class BiCdcWatermark {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "source_module", nullable = false, length = 100) private String sourceModule;
    @Column(name = "source_table", nullable = false, length = 100) private String sourceTable;
    @Column(name = "last_event_id") private Long lastEventId;
    @Column(name = "last_timestamp") private LocalDateTime lastTimestamp;
    @Column(name = "last_run") private LocalDateTime lastRun;
    @Column(nullable = false, length = 30) private String status = "ACTIVE";
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves source module data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceModule() { return sourceModule; } public void setSourceModule(String v) { this.sourceModule = v; }
    /**
     * Retrieves source table data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceTable() { return sourceTable; } public void setSourceTable(String v) { this.sourceTable = v; }
    /**
     * Retrieves last event id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getLastEventId() { return lastEventId; } public void setLastEventId(Long v) { this.lastEventId = v; }
    /**
     * Retrieves last timestamp data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLastTimestamp() { return lastTimestamp; } public void setLastTimestamp(LocalDateTime v) { this.lastTimestamp = v; }
    /**
     * Retrieves last run data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLastRun() { return lastRun; } public void setLastRun(LocalDateTime v) { this.lastRun = v; }
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}