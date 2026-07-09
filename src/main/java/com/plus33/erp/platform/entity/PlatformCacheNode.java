/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformCacheNode.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCacheNodeController
 * Related Service   : PlatformCacheNodeService, PlatformCacheNodeServiceImpl
 * Related Repository: PlatformCacheNodeRepository
 * Related Entity    : PlatformCacheNode
 * Related DTO       : N/A
 * Related Mapper    : PlatformCacheNodeMapper
 * Related DB Table  : platform_cache_node
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCacheNodeRepository, PlatformCacheNodeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_cache_node'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformCacheNode}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_cache_node'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_cache_node}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_cache_node")
public class PlatformCacheNode {
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
    private String status = "UP";

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