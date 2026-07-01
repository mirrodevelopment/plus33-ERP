package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_telemetry_archive_log")
public class PlatformTelemetryArchiveLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instance_id", nullable = false)
    @NotNull
    private Long instanceId;

    @Column(name = "records_archived", nullable = false)
    @NotNull
    private Integer recordsArchived;

    @Column(name = "archive_key", nullable = false)
    @NotNull
    @Size(max = 250)
    private String archiveKey;

    @Column(name = "archived_at", nullable = false)
    @NotNull
    private LocalDateTime archivedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getInstanceId() { return instanceId; }
    public void setInstanceId(Long instanceId) { this.instanceId = instanceId; }
    public Integer getRecordsArchived() { return recordsArchived; }
    public void setRecordsArchived(Integer recordsArchived) { this.recordsArchived = recordsArchived; }
    public String getArchiveKey() { return archiveKey; }
    public void setArchiveKey(String archiveKey) { this.archiveKey = archiveKey; }
    public LocalDateTime getArchivedAt() { return archivedAt; }
    public void setArchivedAt(LocalDateTime archivedAt) { this.archivedAt = archivedAt; }
}