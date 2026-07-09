/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiExportJob.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiExportJobController
 * Related Service   : BiExportJobService, BiExportJobServiceImpl
 * Related Repository: BiExportJobRepository
 * Related Entity    : BiExportJob
 * Related DTO       : N/A
 * Related Mapper    : BiExportJobMapper
 * Related DB Table  : bi_export_job
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiExportJobRepository, BiExportJobMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_export_job'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiExportJob}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_export_job'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_export_job}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_export_job")
public class BiExportJob {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "job_reference", nullable = false, unique = true, length = 100) private String jobReference;
    @Column(name = "report_name", nullable = false, length = 200) private String reportName;
    @Column(name = "export_format", nullable = false, length = 20) private String exportFormat;
    @Column(name = "parameters_json", columnDefinition = "TEXT") private String parametersJson;
    @Column(name = "requested_by", nullable = false, length = 100) private String requestedBy;
    @Column(nullable = false, length = 30) private String status = "REQUESTED";
    @Column(name = "file_path", length = 500) private String filePath;
    @Column(name = "file_size_bytes") private Long fileSizeBytes;
    @Column(name = "error_message", columnDefinition = "TEXT") private String errorMessage;
    @Column(name = "requested_at", nullable = false, updatable = false) private LocalDateTime requestedAt = LocalDateTime.now();
    @Column(name = "started_at") private LocalDateTime startedAt;
    @Column(name = "completed_at") private LocalDateTime completedAt;
    @Column(name = "expires_at") private LocalDateTime expiresAt;
    @Column(name = "downloaded_at") private LocalDateTime downloadedAt;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves company id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    /**
     * Retrieves job reference data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getJobReference() { return jobReference; } public void setJobReference(String v) { this.jobReference = v; }
    /**
     * Retrieves report name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReportName() { return reportName; } public void setReportName(String v) { this.reportName = v; }
    /**
     * Retrieves export format data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExportFormat() { return exportFormat; } public void setExportFormat(String v) { this.exportFormat = v; }
    /**
     * Retrieves parameters json data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getParametersJson() { return parametersJson; } public void setParametersJson(String v) { this.parametersJson = v; }
    /**
     * Retrieves requested by data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequestedBy() { return requestedBy; } public void setRequestedBy(String v) { this.requestedBy = v; }
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    /**
     * Retrieves file path data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFilePath() { return filePath; } public void setFilePath(String v) { this.filePath = v; }
    /**
     * Retrieves file size bytes data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getFileSizeBytes() { return fileSizeBytes; } public void setFileSizeBytes(Long v) { this.fileSizeBytes = v; }
    /**
     * Retrieves error message data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getErrorMessage() { return errorMessage; } public void setErrorMessage(String v) { this.errorMessage = v; }
    /**
     * Retrieves requested at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRequestedAt() { return requestedAt; }
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
     * Retrieves expires at data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getExpiresAt() { return expiresAt; } public void setExpiresAt(LocalDateTime v) { this.expiresAt = v; }
    /**
     * Retrieves downloaded at data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getDownloadedAt() { return downloadedAt; } public void setDownloadedAt(LocalDateTime v) { this.downloadedAt = v; }
}