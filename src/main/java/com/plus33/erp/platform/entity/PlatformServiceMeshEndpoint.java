/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformServiceMeshEndpoint.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformServiceMeshEndpointController
 * Related Service   : PlatformServiceMeshEndpointService, PlatformServiceMeshEndpointServiceImpl
 * Related Repository: PlatformServiceMeshEndpointRepository
 * Related Entity    : PlatformServiceMeshEndpoint
 * Related DTO       : N/A
 * Related Mapper    : PlatformServiceMeshEndpointMapper
 * Related DB Table  : platform_service_mesh_endpoint
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformServiceMeshEndpointRepository, PlatformServiceMeshEndpointMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_service_mesh_endpoint'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformServiceMeshEndpoint}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_service_mesh_endpoint'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_service_mesh_endpoint}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves service name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getServiceName() { return serviceName; }
    /**
     * Performs the setServiceName operation in this module.
     *
     * @param serviceName the serviceName input value
     */
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    /**
     * Retrieves sidecar proxy ip data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSidecarProxyIp() { return sidecarProxyIp; }
    /**
     * Performs the setSidecarProxyIp operation in this module.
     *
     * @param sidecarProxyIp the sidecarProxyIp input value
     */
    public void setSidecarProxyIp(String sidecarProxyIp) { this.sidecarProxyIp = sidecarProxyIp; }
    /**
     * Retrieves mtls enabled data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getMtlsEnabled() { return mtlsEnabled; }
    /**
     * Performs the setMtlsEnabled operation in this module.
     *
     * @param mtlsEnabled the mtlsEnabled input value
     */
    public void setMtlsEnabled(Boolean mtlsEnabled) { this.mtlsEnabled = mtlsEnabled; }
    /**
     * Retrieves proxy status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProxyStatus() { return proxyStatus; }
    /**
     * Performs the setProxyStatus operation in this module.
     *
     * @param proxyStatus the proxyStatus input value
     */
    public void setProxyStatus(String proxyStatus) { this.proxyStatus = proxyStatus; }
}