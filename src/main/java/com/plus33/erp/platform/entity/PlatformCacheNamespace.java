package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_cache_namespace")
public class PlatformCacheNamespace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "namespace_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String namespaceName;

    @Column(name = "ttl_seconds", nullable = false)
    @NotNull
    private Integer ttlSeconds = 3600;

    @Column(name = "eviction_policy", nullable = false)
    @NotNull
    @Size(max = 50)
    private String evictionPolicy = "LRU";

    @Column(name = "compression_enabled", nullable = false)
    @NotNull
    private Boolean compressionEnabled = false;

    @Column(name = "serialization_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String serializationType = "JSON";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getNamespaceName() { return namespaceName; }
    public void setNamespaceName(String namespaceName) { this.namespaceName = namespaceName; }
    public Integer getTtlSeconds() { return ttlSeconds; }
    public void setTtlSeconds(Integer ttlSeconds) { this.ttlSeconds = ttlSeconds; }
    public String getEvictionPolicy() { return evictionPolicy; }
    public void setEvictionPolicy(String evictionPolicy) { this.evictionPolicy = evictionPolicy; }
    public Boolean getCompressionEnabled() { return compressionEnabled; }
    public void setCompressionEnabled(Boolean compressionEnabled) { this.compressionEnabled = compressionEnabled; }
    public String getSerializationType() { return serializationType; }
    public void setSerializationType(String serializationType) { this.serializationType = serializationType; }
}