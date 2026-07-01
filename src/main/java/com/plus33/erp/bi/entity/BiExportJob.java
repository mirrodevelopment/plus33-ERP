package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_export_job")
public class BiExportJob {
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
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getJobReference() { return jobReference; } public void setJobReference(String v) { this.jobReference = v; }
    public String getReportName() { return reportName; } public void setReportName(String v) { this.reportName = v; }
    public String getExportFormat() { return exportFormat; } public void setExportFormat(String v) { this.exportFormat = v; }
    public String getParametersJson() { return parametersJson; } public void setParametersJson(String v) { this.parametersJson = v; }
    public String getRequestedBy() { return requestedBy; } public void setRequestedBy(String v) { this.requestedBy = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public String getFilePath() { return filePath; } public void setFilePath(String v) { this.filePath = v; }
    public Long getFileSizeBytes() { return fileSizeBytes; } public void setFileSizeBytes(Long v) { this.fileSizeBytes = v; }
    public String getErrorMessage() { return errorMessage; } public void setErrorMessage(String v) { this.errorMessage = v; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public LocalDateTime getStartedAt() { return startedAt; } public void setStartedAt(LocalDateTime v) { this.startedAt = v; }
    public LocalDateTime getCompletedAt() { return completedAt; } public void setCompletedAt(LocalDateTime v) { this.completedAt = v; }
    public LocalDateTime getExpiresAt() { return expiresAt; } public void setExpiresAt(LocalDateTime v) { this.expiresAt = v; }
    public LocalDateTime getDownloadedAt() { return downloadedAt; } public void setDownloadedAt(LocalDateTime v) { this.downloadedAt = v; }
}
