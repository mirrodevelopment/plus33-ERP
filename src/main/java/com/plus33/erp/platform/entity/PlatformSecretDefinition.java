package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_secret_definition")
public class PlatformSecretDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "alias_path", nullable = false, unique = true)
    @NotNull
    @Size(max = 250)
    private String aliasPath;

    @Column(name = "secret_key", nullable = false)
    @NotNull
    @Size(max = 250)
    private String secretKey;

    @Column(name = "rotation_policy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String rotationPolicy;

    @Column(name = "next_rotation", nullable = false)
    @NotNull
    private LocalDateTime nextRotation;

    @Column(name = "last_rotation")
    private LocalDateTime lastRotation;

    @Column(name = "provider_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String providerVersion = "1";

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getAliasPath() { return aliasPath; }
    public void setAliasPath(String aliasPath) { this.aliasPath = aliasPath; }
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getRotationPolicy() { return rotationPolicy; }
    public void setRotationPolicy(String rotationPolicy) { this.rotationPolicy = rotationPolicy; }
    public LocalDateTime getNextRotation() { return nextRotation; }
    public void setNextRotation(LocalDateTime nextRotation) { this.nextRotation = nextRotation; }
    public LocalDateTime getLastRotation() { return lastRotation; }
    public void setLastRotation(LocalDateTime lastRotation) { this.lastRotation = lastRotation; }
    public String getProviderVersion() { return providerVersion; }
    public void setProviderVersion(String providerVersion) { this.providerVersion = providerVersion; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}