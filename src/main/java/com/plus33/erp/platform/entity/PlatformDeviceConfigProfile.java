package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_device_config_profile")
public class PlatformDeviceConfigProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "profile_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String profileCode;

    @Column(name = "profile_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String profileName;

    @Column(name = "profile_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String profileVersion;

    @Column(nullable = false)
    @NotNull
    @Size(max = 64)
    private String checksum;

    @Column(name = "configuration_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String configurationJson;

    @Column(name = "rollback_profile_id")
    private Long rollbackProfileId;

    @Column(name = "effective_from")
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

    @Column(name = "assignment_scope", nullable = false)
    @NotNull
    @Size(max = 200)
    private String assignmentScope; // Base Profile, Warehouse Profile, POS Profile, Edge Gateway Profile

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getProfileCode() { return profileCode; }
    public void setProfileCode(String profileCode) { this.profileCode = profileCode; }
    public String getProfileName() { return profileName; }
    public void setProfileName(String profileName) { this.profileName = profileName; }
    public String getProfileVersion() { return profileVersion; }
    public void setProfileVersion(String profileVersion) { this.profileVersion = profileVersion; }
    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }
    public String getConfigurationJson() { return configurationJson; }
    public void setConfigurationJson(String configurationJson) { this.configurationJson = configurationJson; }
    public Long getRollbackProfileId() { return rollbackProfileId; }
    public void setRollbackProfileId(Long rollbackProfileId) { this.rollbackProfileId = rollbackProfileId; }
    public LocalDateTime getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDateTime effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDateTime getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDateTime effectiveTo) { this.effectiveTo = effectiveTo; }
    public String getAssignmentScope() { return assignmentScope; }
    public void setAssignmentScope(String assignmentScope) { this.assignmentScope = assignmentScope; }
}