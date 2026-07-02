package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_edge_node")
public class PlatformEdgeNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "node_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String nodeCode;

    @Column(name = "node_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String nodeName;

    @Column(name = "edge_cluster")
    @Size(max = 100)
    private String edgeCluster;

    @Column(name = "hardware_model")
    @Size(max = 100)
    private String hardwareModel;

    @Column(name = "firmware_version")
    @Size(max = 50)
    private String firmwareVersion;

    @Column(name = "os_version")
    @Size(max = 50)
    private String osVersion;

    @Column(name = "cpu_architecture")
    @Size(max = 50)
    private String cpuArchitecture;

    @Column(name = "serial_number")
    @Size(max = 100)
    private String serialNumber;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status; // ACTIVE, OFFLINE, PROVISIONING, DECOMMISSIONED

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @Column(name = "ip_address")
    @Size(max = 45)
    private String ipAddress;

    @Column(name = "mac_address")
    @Size(max = 17)
    private String macAddress;

    @Size(max = 200)
    private String location;

    @Column(name = "tenant_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String tenantId = "DEFAULT_TENANT";

    @Column(name = "certificate_thumbprint")
    @Size(max = 256)
    private String certificateThumbprint;

    @Column(name = "provisioned_at")
    private LocalDateTime provisionedAt;

    @Column(name = "heartbeat_interval_seconds", nullable = false)
    @NotNull
    private Integer heartbeatIntervalSeconds = 30;

    @Column(name = "sync_interval_seconds", nullable = false)
    @NotNull
    private Integer syncIntervalSeconds = 60;

    @Column(name = "offline_timeout_seconds", nullable = false)
    @NotNull
    private Integer offlineTimeoutSeconds = 120;

    @Column(name = "software_version")
    @Size(max = 50)
    private String softwareVersion;

    @Column(name = "deployment_group")
    @Size(max = 100)
    private String deploymentGroup;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getNodeCode() { return nodeCode; }
    public void setNodeCode(String nodeCode) { this.nodeCode = nodeCode; }
    public String getNodeName() { return nodeName; }
    public void setNodeName(String nodeName) { this.nodeName = nodeName; }
    public String getEdgeCluster() { return edgeCluster; }
    public void setEdgeCluster(String edgeCluster) { this.edgeCluster = edgeCluster; }
    public String getHardwareModel() { return hardwareModel; }
    public void setHardwareModel(String hardwareModel) { this.hardwareModel = hardwareModel; }
    public String getFirmwareVersion() { return firmwareVersion; }
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    public String getOsVersion() { return osVersion; }
    public void setOsVersion(String osVersion) { this.osVersion = osVersion; }
    public String getCpuArchitecture() { return cpuArchitecture; }
    public void setCpuArchitecture(String cpuArchitecture) { this.cpuArchitecture = cpuArchitecture; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getLastSeen() { return lastSeen; }
    public void setLastSeen(LocalDateTime lastSeen) { this.lastSeen = lastSeen; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getCertificateThumbprint() { return certificateThumbprint; }
    public void setCertificateThumbprint(String certificateThumbprint) { this.certificateThumbprint = certificateThumbprint; }
    public LocalDateTime getProvisionedAt() { return provisionedAt; }
    public void setProvisionedAt(LocalDateTime provisionedAt) { this.provisionedAt = provisionedAt; }
    public Integer getHeartbeatIntervalSeconds() { return heartbeatIntervalSeconds; }
    public void setHeartbeatIntervalSeconds(Integer heartbeatIntervalSeconds) { this.heartbeatIntervalSeconds = heartbeatIntervalSeconds; }
    public Integer getSyncIntervalSeconds() { return syncIntervalSeconds; }
    public void setSyncIntervalSeconds(Integer syncIntervalSeconds) { this.syncIntervalSeconds = syncIntervalSeconds; }
    public Integer getOfflineTimeoutSeconds() { return offlineTimeoutSeconds; }
    public void setOfflineTimeoutSeconds(Integer offlineTimeoutSeconds) { this.offlineTimeoutSeconds = offlineTimeoutSeconds; }
    public String getSoftwareVersion() { return softwareVersion; }
    public void setSoftwareVersion(String softwareVersion) { this.softwareVersion = softwareVersion; }
    public String getDeploymentGroup() { return deploymentGroup; }
    public void setDeploymentGroup(String deploymentGroup) { this.deploymentGroup = deploymentGroup; }
}