package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_service_mesh_endpoint")
public class PlatformServiceMeshEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "service_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String serviceName;

    @Column(name = "sidecar_proxy_ip", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sidecarProxyIp;

    @Column(name = "mtls_enabled", nullable = false)
    @NotNull
    private Boolean mtlsEnabled = true;

    @Column(name = "proxy_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String proxyStatus = "CONNECTED";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getSidecarProxyIp() { return sidecarProxyIp; }
    public void setSidecarProxyIp(String sidecarProxyIp) { this.sidecarProxyIp = sidecarProxyIp; }
    public Boolean getMtlsEnabled() { return mtlsEnabled; }
    public void setMtlsEnabled(Boolean mtlsEnabled) { this.mtlsEnabled = mtlsEnabled; }
    public String getProxyStatus() { return proxyStatus; }
    public void setProxyStatus(String proxyStatus) { this.proxyStatus = proxyStatus; }
}