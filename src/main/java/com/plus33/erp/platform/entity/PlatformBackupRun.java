package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_backup_run")
public class PlatformBackupRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "backup_file", nullable = false)
    @NotNull
    @Size(max = 250)
    private String backupFile;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status;

    @Column(name = "size_bytes", nullable = false)
    @NotNull
    private Long sizeBytes = 0L;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String checksum;

    @Column(name = "sandbox_restored", nullable = false)
    @NotNull
    private Boolean sandboxRestored = false;

    @Column(name = "integrity_checked", nullable = false)
    @NotNull
    private Boolean integrityChecked = false;

    @Column(name = "integrity_message", columnDefinition = "TEXT")
    private String integrityMessage;

    @Column(name = "completed_at", nullable = false)
    @NotNull
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBackupFile() { return backupFile; }
    public void setBackupFile(String backupFile) { this.backupFile = backupFile; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; }
    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }
    public Boolean getSandboxRestored() { return sandboxRestored; }
    public void setSandboxRestored(Boolean sandboxRestored) { this.sandboxRestored = sandboxRestored; }
    public Boolean getIntegrityChecked() { return integrityChecked; }
    public void setIntegrityChecked(Boolean integrityChecked) { this.integrityChecked = integrityChecked; }
    public String getIntegrityMessage() { return integrityMessage; }
    public void setIntegrityMessage(String integrityMessage) { this.integrityMessage = integrityMessage; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}