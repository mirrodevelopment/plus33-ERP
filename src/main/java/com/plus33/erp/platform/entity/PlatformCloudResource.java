package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_cloud_resource")
public class PlatformCloudResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "resource_id", nullable = false, unique = true)
    @NotNull
    @Size(max = 250)
    private String resourceId;

    @Column(name = "resource_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String resourceType;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String provider;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String region;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "RUNNING";

    @Column(name = "cost_daily", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal costDaily = BigDecimal.ZERO;

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
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getCostDaily() { return costDaily; }
    public void setCostDaily(BigDecimal costDaily) { this.costDaily = costDaily; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}