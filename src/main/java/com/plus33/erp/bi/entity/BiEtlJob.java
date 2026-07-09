/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiEtlJob.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiEtlJobController
 * Related Service   : BiEtlJobService, BiEtlJobServiceImpl
 * Related Repository: BiEtlJobRepository
 * Related Entity    : BiEtlJob
 * Related DTO       : N/A
 * Related Mapper    : BiEtlJobMapper
 * Related DB Table  : bi_etl_job
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiEtlJobRepository, BiEtlJobMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_etl_job'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiEtlJob}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_etl_job'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_etl_job}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_etl_job")
public class BiEtlJob {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "job_name", nullable = false, unique = true, length = 200) private String jobName;
    @Column(name = "job_type", nullable = false, length = 50) private String jobType = "ETL";
    @Column(name = "source_module", nullable = false, length = 100) private String sourceModule;
    @Column(name = "source_table", length = 100) private String sourceTable;
    @Column(name = "target_table", length = 100) private String targetTable;
    @Column(name = "schedule_cron", length = 100) private String scheduleCron;
    @Column(nullable = false, length = 30) private String status = "CREATED";
    @Column(nullable = false) private Integer priority = 50;
    @Column(name = "max_retries", nullable = false) private Integer maxRetries = 3;
    @Column(name = "timeout_minutes", nullable = false) private Integer timeoutMinutes = 60;
    @Column(nullable = false) private Boolean enabled = true;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt = LocalDateTime.now();
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves job name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getJobName() { return jobName; } public void setJobName(String v) { this.jobName = v; }
    /**
     * Retrieves job type data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getJobType() { return jobType; } public void setJobType(String v) { this.jobType = v; }
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
     * Retrieves target table data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTargetTable() { return targetTable; } public void setTargetTable(String v) { this.targetTable = v; }
    /**
     * Retrieves schedule cron data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getScheduleCron() { return scheduleCron; } public void setScheduleCron(String v) { this.scheduleCron = v; }
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    /**
     * Retrieves priority data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPriority() { return priority; } public void setPriority(Integer v) { this.priority = v; }
    /**
     * Retrieves max retries data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getMaxRetries() { return maxRetries; } public void setMaxRetries(Integer v) { this.maxRetries = v; }
    /**
     * Retrieves timeout minutes data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getTimeoutMinutes() { return timeoutMinutes; } public void setTimeoutMinutes(Integer v) { this.timeoutMinutes = v; }
    /**
     * Retrieves enabled data from the database.
     *
     * @param v the v input value
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getEnabled() { return enabled; } public void setEnabled(Boolean v) { this.enabled = v; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; } public void setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }
}