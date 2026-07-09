/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTenantRouting.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTenantRoutingController
 * Related Service   : PlatformTenantRoutingService, PlatformTenantRoutingServiceImpl
 * Related Repository: PlatformTenantRoutingRepository
 * Related Entity    : PlatformTenantRouting
 * Related DTO       : N/A
 * Related Mapper    : PlatformTenantRoutingMapper
 * Related DB Table  : platform_tenant_routing
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTenantRoutingRepository, PlatformTenantRoutingMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_tenant_routing'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTenantRouting}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_tenant_routing'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_tenant_routing}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_tenant_routing")
public class PlatformTenantRouting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "tenant_id", nullable = false, unique = true)
    @NotNull
    @Size(max = 50)
    private String tenantId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String region;

    @Column(name = "routing_policy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String routingPolicy = "ACTIVE_ACTIVE";

    @Column(name = "replica_url")
    @Size(max = 250)
    private String replicaUrl;

    @Column(nullable = false)
    @NotNull
    private Boolean healthy = true;

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
     * Retrieves region data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRegion() { return region; }
    /**
     * Performs the setRegion operation in this module.
     *
     * @param region the region input value
     */
    public void setRegion(String region) { this.region = region; }
    /**
     * Retrieves routing policy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRoutingPolicy() { return routingPolicy; }
    /**
     * Performs the setRoutingPolicy operation in this module.
     *
     * @param routingPolicy the routingPolicy input value
     */
    public void setRoutingPolicy(String routingPolicy) { this.routingPolicy = routingPolicy; }
    /**
     * Retrieves replica url data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReplicaUrl() { return replicaUrl; }
    /**
     * Performs the setReplicaUrl operation in this module.
     *
     * @param replicaUrl the replicaUrl input value
     */
    public void setReplicaUrl(String replicaUrl) { this.replicaUrl = replicaUrl; }
    /**
     * Retrieves healthy data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getHealthy() { return healthy; }
    /**
     * Performs the setHealthy operation in this module.
     *
     * @param healthy the healthy input value
     */
    public void setHealthy(Boolean healthy) { this.healthy = healthy; }
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