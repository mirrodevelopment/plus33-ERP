/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEdgeNode.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEdgeNodeController
 * Related Service   : PlatformEdgeNodeService, PlatformEdgeNodeServiceImpl
 * Related Repository: PlatformEdgeNodeRepository
 * Related Entity    : PlatformEdgeNode
 * Related DTO       : N/A
 * Related Mapper    : PlatformEdgeNodeMapper
 * Related DB Table  : platform_edge_node
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEdgeNodeRepository, PlatformEdgeNodeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_edge_node'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEdgeNode}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_edge_node'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_edge_node}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
    /**
     * Retrieves node code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNodeCode() { return nodeCode; }
    /**
     * Performs the setNodeCode operation in this module.
     *
     * @param nodeCode the nodeCode input value
     */
    public void setNodeCode(String nodeCode) { this.nodeCode = nodeCode; }
    /**
     * Retrieves node name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNodeName() { return nodeName; }
    /**
     * Performs the setNodeName operation in this module.
     *
     * @param nodeName the nodeName input value
     */
    public void setNodeName(String nodeName) { this.nodeName = nodeName; }
    /**
     * Retrieves edge cluster data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEdgeCluster() { return edgeCluster; }
    /**
     * Performs the setEdgeCluster operation in this module.
     *
     * @param edgeCluster the edgeCluster input value
     */
    public void setEdgeCluster(String edgeCluster) { this.edgeCluster = edgeCluster; }
    /**
     * Retrieves hardware model data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getHardwareModel() { return hardwareModel; }
    /**
     * Performs the setHardwareModel operation in this module.
     *
     * @param hardwareModel the hardwareModel input value
     */
    public void setHardwareModel(String hardwareModel) { this.hardwareModel = hardwareModel; }
    /**
     * Retrieves firmware version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFirmwareVersion() { return firmwareVersion; }
    /**
     * Performs the setFirmwareVersion operation in this module.
     *
     * @param firmwareVersion the firmwareVersion input value
     */
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    /**
     * Retrieves os version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOsVersion() { return osVersion; }
    /**
     * Performs the setOsVersion operation in this module.
     *
     * @param osVersion the osVersion input value
     */
    public void setOsVersion(String osVersion) { this.osVersion = osVersion; }
    /**
     * Retrieves cpu architecture data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCpuArchitecture() { return cpuArchitecture; }
    /**
     * Performs the setCpuArchitecture operation in this module.
     *
     * @param cpuArchitecture the cpuArchitecture input value
     */
    public void setCpuArchitecture(String cpuArchitecture) { this.cpuArchitecture = cpuArchitecture; }
    /**
     * Retrieves serial number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSerialNumber() { return serialNumber; }
    /**
     * Performs the setSerialNumber operation in this module.
     *
     * @param serialNumber the serialNumber input value
     */
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Retrieves last seen data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLastSeen() { return lastSeen; }
    /**
     * Performs the setLastSeen operation in this module.
     *
     * @param lastSeen the lastSeen input value
     */
    public void setLastSeen(LocalDateTime lastSeen) { this.lastSeen = lastSeen; }
    /**
     * Retrieves ip address data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIpAddress() { return ipAddress; }
    /**
     * Performs the setIpAddress operation in this module.
     *
     * @param ipAddress the ipAddress input value
     */
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    /**
     * Retrieves mac address data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMacAddress() { return macAddress; }
    /**
     * Performs the setMacAddress operation in this module.
     *
     * @param macAddress the macAddress input value
     */
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    /**
     * Retrieves location data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLocation() { return location; }
    /**
     * Performs the setLocation operation in this module.
     *
     * @param location the location input value
     */
    public void setLocation(String location) { this.location = location; }
    /**
     * Retrieves tenant id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTenantId() { return tenantId; }
    /**
     * Performs the setTenantId operation in this module.
     *
     * @param tenantId the tenantId input value
     */
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    /**
     * Retrieves certificate thumbprint data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCertificateThumbprint() { return certificateThumbprint; }
    /**
     * Performs the setCertificateThumbprint operation in this module.
     *
     * @param certificateThumbprint the certificateThumbprint input value
     */
    public void setCertificateThumbprint(String certificateThumbprint) { this.certificateThumbprint = certificateThumbprint; }
    /**
     * Retrieves provisioned at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getProvisionedAt() { return provisionedAt; }
    /**
     * Performs the setProvisionedAt operation in this module.
     *
     * @param provisionedAt the provisionedAt input value
     */
    public void setProvisionedAt(LocalDateTime provisionedAt) { this.provisionedAt = provisionedAt; }
    /**
     * Retrieves heartbeat interval seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getHeartbeatIntervalSeconds() { return heartbeatIntervalSeconds; }
    /**
     * Performs the setHeartbeatIntervalSeconds operation in this module.
     *
     * @param heartbeatIntervalSeconds the heartbeatIntervalSeconds input value
     */
    public void setHeartbeatIntervalSeconds(Integer heartbeatIntervalSeconds) { this.heartbeatIntervalSeconds = heartbeatIntervalSeconds; }
    /**
     * Retrieves sync interval seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getSyncIntervalSeconds() { return syncIntervalSeconds; }
    /**
     * Performs the setSyncIntervalSeconds operation in this module.
     *
     * @param syncIntervalSeconds the syncIntervalSeconds input value
     */
    public void setSyncIntervalSeconds(Integer syncIntervalSeconds) { this.syncIntervalSeconds = syncIntervalSeconds; }
    /**
     * Retrieves offline timeout seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getOfflineTimeoutSeconds() { return offlineTimeoutSeconds; }
    /**
     * Performs the setOfflineTimeoutSeconds operation in this module.
     *
     * @param offlineTimeoutSeconds the offlineTimeoutSeconds input value
     */
    public void setOfflineTimeoutSeconds(Integer offlineTimeoutSeconds) { this.offlineTimeoutSeconds = offlineTimeoutSeconds; }
    /**
     * Retrieves software version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSoftwareVersion() { return softwareVersion; }
    /**
     * Performs the setSoftwareVersion operation in this module.
     *
     * @param softwareVersion the softwareVersion input value
     */
    public void setSoftwareVersion(String softwareVersion) { this.softwareVersion = softwareVersion; }
    /**
     * Retrieves deployment group data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDeploymentGroup() { return deploymentGroup; }
    /**
     * Performs the setDeploymentGroup operation in this module.
     *
     * @param deploymentGroup the deploymentGroup input value
     */
    public void setDeploymentGroup(String deploymentGroup) { this.deploymentGroup = deploymentGroup; }
}