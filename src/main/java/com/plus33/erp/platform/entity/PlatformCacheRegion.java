package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_cache_region")
public class PlatformCacheRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "region_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String regionName;

    @Column(name = "replication_mode", nullable = false)
    @NotNull
    @Size(max = 50)
    private String replicationMode = "ASYNCHRONOUS";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    public String getReplicationMode() { return replicationMode; }
    public void setReplicationMode(String replicationMode) { this.replicationMode = replicationMode; }
}