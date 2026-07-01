package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "platform_region_profile")
public class PlatformRegionProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "region_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 50)
    private String regionCode;

    @Column(name = "health_score", nullable = false)
    @NotNull
    private Integer healthScore = 100;

    @Column(name = "cpu_utilization", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal cpuUtilization = BigDecimal.ZERO;

    @Column(name = "memory_utilization", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal memoryUtilization = BigDecimal.ZERO;

    @Column(name = "network_rtt_ms", nullable = false)
    @NotNull
    private Integer networkRttMs = 5;

    @Column(name = "failed_queries", nullable = false)
    @NotNull
    private Integer failedQueries = 0;

    @Column(name = "disk_utilization", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal diskUtilization = BigDecimal.ZERO;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
    public Integer getHealthScore() { return healthScore; }
    public void setHealthScore(Integer healthScore) { this.healthScore = healthScore; }
    public BigDecimal getCpuUtilization() { return cpuUtilization; }
    public void setCpuUtilization(BigDecimal cpuUtilization) { this.cpuUtilization = cpuUtilization; }
    public BigDecimal getMemoryUtilization() { return memoryUtilization; }
    public void setMemoryUtilization(BigDecimal memoryUtilization) { this.memoryUtilization = memoryUtilization; }
    public Integer getNetworkRttMs() { return networkRttMs; }
    public void setNetworkRttMs(Integer networkRttMs) { this.networkRttMs = networkRttMs; }
    public Integer getFailedQueries() { return failedQueries; }
    public void setFailedQueries(Integer failedQueries) { this.failedQueries = failedQueries; }
    public BigDecimal getDiskUtilization() { return diskUtilization; }
    public void setDiskUtilization(BigDecimal diskUtilization) { this.diskUtilization = diskUtilization; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}