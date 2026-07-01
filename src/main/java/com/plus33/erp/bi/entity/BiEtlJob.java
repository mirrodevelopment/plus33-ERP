package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_etl_job")
public class BiEtlJob {
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
    public String getJobName() { return jobName; } public void setJobName(String v) { this.jobName = v; }
    public String getJobType() { return jobType; } public void setJobType(String v) { this.jobType = v; }
    public String getSourceModule() { return sourceModule; } public void setSourceModule(String v) { this.sourceModule = v; }
    public String getSourceTable() { return sourceTable; } public void setSourceTable(String v) { this.sourceTable = v; }
    public String getTargetTable() { return targetTable; } public void setTargetTable(String v) { this.targetTable = v; }
    public String getScheduleCron() { return scheduleCron; } public void setScheduleCron(String v) { this.scheduleCron = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public Integer getPriority() { return priority; } public void setPriority(Integer v) { this.priority = v; }
    public Integer getMaxRetries() { return maxRetries; } public void setMaxRetries(Integer v) { this.maxRetries = v; }
    public Integer getTimeoutMinutes() { return timeoutMinutes; } public void setTimeoutMinutes(Integer v) { this.timeoutMinutes = v; }
    public Boolean getEnabled() { return enabled; } public void setEnabled(Boolean v) { this.enabled = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; } public void setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }
}
