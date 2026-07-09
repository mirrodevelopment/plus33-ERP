/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDiscoveryNode.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDiscoveryNodeController
 * Related Service   : PlatformDiscoveryNodeService, PlatformDiscoveryNodeServiceImpl
 * Related Repository: PlatformDiscoveryNodeRepository
 * Related Entity    : PlatformDiscoveryNode
 * Related DTO       : N/A
 * Related Mapper    : PlatformDiscoveryNodeMapper
 * Related DB Table  : platform_discovery_node
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDiscoveryNodeRepository, PlatformDiscoveryNodeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_discovery_node'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDiscoveryNode}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_discovery_node'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_discovery_node}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_discovery_node")
public class PlatformDiscoveryNode {
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

    @Column(name = "ip_address", nullable = false)
    @NotNull
    @Size(max = 100)
    private String ipAddress;

    @Column(nullable = false)
    @NotNull
    private Integer port;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "STARTING";

    @Column(name = "cluster_role", nullable = false)
    @NotNull
    @Size(max = 50)
    private String clusterRole = "FOLLOWER";

    @Column(name = "last_heartbeat", nullable = false)
    @NotNull
    private LocalDateTime lastHeartbeat = LocalDateTime.now();

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves port data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPort() { return port; }
    /**
     * Performs the setPort operation in this module.
     *
     * @param port the port input value
     */
    public void setPort(Integer port) { this.port = port; }
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
     * Retrieves cluster role data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getClusterRole() { return clusterRole; }
    /**
     * Performs the setClusterRole operation in this module.
     *
     * @param clusterRole the clusterRole input value
     */
    public void setClusterRole(String clusterRole) { this.clusterRole = clusterRole; }
    /**
     * Retrieves last heartbeat data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLastHeartbeat() { return lastHeartbeat; }
    /**
     * Performs the setLastHeartbeat operation in this module.
     *
     * @param lastHeartbeat the lastHeartbeat input value
     */
    public void setLastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}