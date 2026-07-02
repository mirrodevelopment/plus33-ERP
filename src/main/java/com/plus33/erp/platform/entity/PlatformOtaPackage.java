package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_ota_package")
public class PlatformOtaPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "package_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String packageName;

    @Column(name = "package_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String packageVersion;

    @Column(name = "semantic_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String semanticVersion;

    @Column(name = "checksum_sha256", nullable = false)
    @NotNull
    @Size(max = 64)
    private String checksumSha256;

    @Column(nullable = false)
    @NotNull
    @Size(max = 256)
    private String signature;

    @Column(name = "package_size_bytes", nullable = false)
    @NotNull
    private Long packageSizeBytes;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String compression = "GZIP";

    @Column(name = "supported_architecture", nullable = false)
    @NotNull
    @Size(max = 50)
    private String supportedArchitecture; // x86_64, arm64

    @Column(name = "supported_os", nullable = false)
    @NotNull
    @Size(max = 50)
    private String supportedOs; // Linux, Windows

    @Column(name = "minimum_agent_version")
    @Size(max = 50)
    private String minimumAgentVersion;

    @Column(name = "rollback_version")
    @Size(max = 50)
    private String rollbackVersion;

    @Column(name = "release_notes", columnDefinition = "TEXT")
    private String releaseNotes;

    @Column(name = "package_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String packageType; // FULL, DELTA, PATCH, HOTFIX

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public String getPackageVersion() { return packageVersion; }
    public void setPackageVersion(String packageVersion) { this.packageVersion = packageVersion; }
    public String getSemanticVersion() { return semanticVersion; }
    public void setSemanticVersion(String semanticVersion) { this.semanticVersion = semanticVersion; }
    public String getChecksumSha256() { return checksumSha256; }
    public void setChecksumSha256(String checksumSha256) { this.checksumSha256 = checksumSha256; }
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
    public Long getPackageSizeBytes() { return packageSizeBytes; }
    public void setPackageSizeBytes(Long packageSizeBytes) { this.packageSizeBytes = packageSizeBytes; }
    public String getCompression() { return compression; }
    public void setCompression(String compression) { this.compression = compression; }
    public String getSupportedArchitecture() { return supportedArchitecture; }
    public void setSupportedArchitecture(String supportedArchitecture) { this.supportedArchitecture = supportedArchitecture; }
    public String getSupportedOs() { return supportedOs; }
    public void setSupportedOs(String supportedOs) { this.supportedOs = supportedOs; }
    public String getMinimumAgentVersion() { return minimumAgentVersion; }
    public void setMinimumAgentVersion(String minimumAgentVersion) { this.minimumAgentVersion = minimumAgentVersion; }
    public String getRollbackVersion() { return rollbackVersion; }
    public void setRollbackVersion(String rollbackVersion) { this.rollbackVersion = rollbackVersion; }
    public String getReleaseNotes() { return releaseNotes; }
    public void setReleaseNotes(String releaseNotes) { this.releaseNotes = releaseNotes; }
    public String getPackageType() { return packageType; }
    public void setPackageType(String packageType) { this.packageType = packageType; }
}