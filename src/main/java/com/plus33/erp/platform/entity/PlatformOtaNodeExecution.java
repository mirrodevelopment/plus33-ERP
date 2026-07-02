package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_ota_node_execution")
public class PlatformOtaNodeExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "campaign_id", nullable = false)
    @NotNull
    private Long campaignId;

    @Column(name = "node_id", nullable = false)
    @NotNull
    private Long nodeId;

    @Column(name = "download_started")
    private LocalDateTime downloadStarted;

    @Column(name = "download_completed")
    private LocalDateTime downloadCompleted;

    @Column(name = "install_started")
    private LocalDateTime installStarted;

    @Column(name = "install_completed")
    private LocalDateTime installCompleted;

    @Column(name = "reboot_required", nullable = false)
    @NotNull
    private Boolean rebootRequired = false;

    @Column(name = "reboot_completed", nullable = false)
    @NotNull
    private Boolean rebootCompleted = false;

    @Column(name = "execution_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String executionStatus; // QUEUED, DOWNLOADING, INSTALLING, SUCCESS, FAILED, ROLLED_BACK

    @Column(name = "failure_reason")
    @Size(max = 500)
    private String failureReason;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    public Long getNodeId() { return nodeId; }
    public void setNodeId(Long nodeId) { this.nodeId = nodeId; }
    public LocalDateTime getDownloadStarted() { return downloadStarted; }
    public void setDownloadStarted(LocalDateTime downloadStarted) { this.downloadStarted = downloadStarted; }
    public LocalDateTime getDownloadCompleted() { return downloadCompleted; }
    public void setDownloadCompleted(LocalDateTime downloadCompleted) { this.downloadCompleted = downloadCompleted; }
    public LocalDateTime getInstallStarted() { return installStarted; }
    public void setInstallStarted(LocalDateTime installStarted) { this.installStarted = installStarted; }
    public LocalDateTime getInstallCompleted() { return installCompleted; }
    public void setInstallCompleted(LocalDateTime installCompleted) { this.installCompleted = installCompleted; }
    public Boolean getRebootRequired() { return rebootRequired; }
    public void setRebootRequired(Boolean rebootRequired) { this.rebootRequired = rebootRequired; }
    public Boolean getRebootCompleted() { return rebootCompleted; }
    public void setRebootCompleted(Boolean rebootCompleted) { this.rebootCompleted = rebootCompleted; }
    public String getExecutionStatus() { return executionStatus; }
    public void setExecutionStatus(String executionStatus) { this.executionStatus = executionStatus; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}