package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_multicloud_sync_profile")
public class PlatformMulticloudSyncProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "provider_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String providerName;

    @Column(name = "target_endpoint", nullable = false)
    @NotNull
    @Size(max = 250)
    private String targetEndpoint;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public String getTargetEndpoint() { return targetEndpoint; }
    public void setTargetEndpoint(String targetEndpoint) { this.targetEndpoint = targetEndpoint; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}