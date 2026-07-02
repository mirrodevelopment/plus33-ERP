package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_device_drift_log")
public class PlatformDeviceDriftLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    @NotNull
    private Long deviceId;

    @Column(name = "baseline_hash", nullable = false)
    @NotNull
    @Size(max = 64)
    private String baselineHash;

    @Column(name = "current_hash", nullable = false)
    @NotNull
    @Size(max = 64)
    private String currentHash;

    @Column(name = "changed_files", columnDefinition = "TEXT")
    private String changedFiles;

    @Column(name = "registry_changes", columnDefinition = "TEXT")
    private String registryChanges;

    @Column(name = "package_changes", columnDefinition = "TEXT")
    private String packageChanges;

    @Column(name = "service_changes", columnDefinition = "TEXT")
    private String serviceChanges;

    @Column(name = "detected_at", nullable = false)
    @NotNull
    private LocalDateTime detectedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public String getBaselineHash() { return baselineHash; }
    public void setBaselineHash(String baselineHash) { this.baselineHash = baselineHash; }
    public String getCurrentHash() { return currentHash; }
    public void setCurrentHash(String currentHash) { this.currentHash = currentHash; }
    public String getChangedFiles() { return changedFiles; }
    public void setChangedFiles(String changedFiles) { this.changedFiles = changedFiles; }
    public String getRegistryChanges() { return registryChanges; }
    public void setRegistryChanges(String registryChanges) { this.registryChanges = registryChanges; }
    public String getPackageChanges() { return packageChanges; }
    public void setPackageChanges(String packageChanges) { this.packageChanges = packageChanges; }
    public String getServiceChanges() { return serviceChanges; }
    public void setServiceChanges(String serviceChanges) { this.serviceChanges = serviceChanges; }
    public LocalDateTime getDetectedAt() { return detectedAt; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
}