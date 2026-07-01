package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_k8s_resource", uniqueConstraints = @UniqueConstraint(columnNames = {"resource_name", "resource_type", "namespace"}))
public class PlatformK8sResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "resource_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String resourceName;

    @Column(name = "resource_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String resourceType;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String namespace;

    @Column(name = "manifest_yaml", columnDefinition = "TEXT")
    private String manifestYaml;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getNamespace() { return namespace; }
    public void setNamespace(String namespace) { this.namespace = namespace; }
    public String getManifestYaml() { return manifestYaml; }
    public void setManifestYaml(String manifestYaml) { this.manifestYaml = manifestYaml; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}