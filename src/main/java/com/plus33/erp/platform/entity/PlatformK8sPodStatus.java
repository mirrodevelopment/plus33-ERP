/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformK8sPodStatus.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformK8sPodStatusController
 * Related Service   : PlatformK8sPodStatusService, PlatformK8sPodStatusServiceImpl
 * Related Repository: PlatformK8sPodStatusRepository
 * Related Entity    : PlatformK8sPodStatus
 * Related DTO       : N/A
 * Related Mapper    : PlatformK8sPodStatusMapper
 * Related DB Table  : platform_k8s_pod_status
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformK8sPodStatusRepository, PlatformK8sPodStatusMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_k8s_pod_status'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformK8sPodStatus}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_k8s_pod_status'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_k8s_pod_status}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_k8s_pod_status")
public class PlatformK8sPodStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "pod_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String podName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String namespace;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "CREATED";

    @Column(name = "node_ip")
    @Size(max = 100)
    private String nodeIp;

    @Column(nullable = false)
    @NotNull
    private Integer restarts = 0;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

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
     * Retrieves pod name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPodName() { return podName; }
    /**
     * Performs the setPodName operation in this module.
     *
     * @param podName the podName input value
     */
    public void setPodName(String podName) { this.podName = podName; }
    /**
     * Retrieves namespace data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNamespace() { return namespace; }
    /**
     * Performs the setNamespace operation in this module.
     *
     * @param namespace the namespace input value
     */
    public void setNamespace(String namespace) { this.namespace = namespace; }
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
     * Retrieves node ip data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNodeIp() { return nodeIp; }
    /**
     * Performs the setNodeIp operation in this module.
     *
     * @param nodeIp the nodeIp input value
     */
    public void setNodeIp(String nodeIp) { this.nodeIp = nodeIp; }
    /**
     * Retrieves restarts data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRestarts() { return restarts; }
    /**
     * Performs the setRestarts operation in this module.
     *
     * @param restarts the restarts input value
     */
    public void setRestarts(Integer restarts) { this.restarts = restarts; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}