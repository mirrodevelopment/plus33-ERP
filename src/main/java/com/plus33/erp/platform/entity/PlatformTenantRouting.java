package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_tenant_routing")
public class PlatformTenantRouting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "tenant_id", nullable = false, unique = true)
    @NotNull
    @Size(max = 50)
    private String tenantId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String region;

    @Column(name = "routing_policy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String routingPolicy = "ACTIVE_ACTIVE";

    @Column(name = "replica_url")
    @Size(max = 250)
    private String replicaUrl;

    @Column(nullable = false)
    @NotNull
    private Boolean healthy = true;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getRoutingPolicy() { return routingPolicy; }
    public void setRoutingPolicy(String routingPolicy) { this.routingPolicy = routingPolicy; }
    public String getReplicaUrl() { return replicaUrl; }
    public void setReplicaUrl(String replicaUrl) { this.replicaUrl = replicaUrl; }
    public Boolean getHealthy() { return healthy; }
    public void setHealthy(Boolean healthy) { this.healthy = healthy; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}