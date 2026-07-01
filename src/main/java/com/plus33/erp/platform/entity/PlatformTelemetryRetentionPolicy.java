package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_telemetry_retention_policy")
public class PlatformTelemetryRetentionPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "instance_id", nullable = false, unique = true)
    @NotNull
    private Long instanceId;

    @Column(name = "retention_days", nullable = false)
    @NotNull
    private Integer retentionDays = 30;

    @Column(name = "archival_target", nullable = false)
    @NotNull
    @Size(max = 150)
    private String archivalTarget = "S3_COLD";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Long getInstanceId() { return instanceId; }
    public void setInstanceId(Long instanceId) { this.instanceId = instanceId; }
    public Integer getRetentionDays() { return retentionDays; }
    public void setRetentionDays(Integer retentionDays) { this.retentionDays = retentionDays; }
    public String getArchivalTarget() { return archivalTarget; }
    public void setArchivalTarget(String archivalTarget) { this.archivalTarget = archivalTarget; }
}