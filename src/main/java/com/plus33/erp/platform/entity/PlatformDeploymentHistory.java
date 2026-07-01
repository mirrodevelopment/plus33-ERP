package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_deployment_history")
public class PlatformDeploymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "deployment_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String deploymentVersion;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String changelog;

    @Column(name = "deployed_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String deployedBy;

    @Column(name = "started_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getDeploymentVersion() { return deploymentVersion; }
    public void setDeploymentVersion(String deploymentVersion) { this.deploymentVersion = deploymentVersion; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getChangelog() { return changelog; }
    public void setChangelog(String changelog) { this.changelog = changelog; }
    public String getDeployedBy() { return deployedBy; }
    public void setDeployedBy(String deployedBy) { this.deployedBy = deployedBy; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}