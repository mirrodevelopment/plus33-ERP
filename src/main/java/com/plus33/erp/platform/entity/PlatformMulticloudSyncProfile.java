/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformMulticloudSyncProfile.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformMulticloudSyncProfileController
 * Related Service   : PlatformMulticloudSyncProfileService, PlatformMulticloudSyncProfileServiceImpl
 * Related Repository: PlatformMulticloudSyncProfileRepository
 * Related Entity    : PlatformMulticloudSyncProfile
 * Related DTO       : N/A
 * Related Mapper    : PlatformMulticloudSyncProfileMapper
 * Related DB Table  : platform_multicloud_sync_profile
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformMulticloudSyncProfileRepository, PlatformMulticloudSyncProfileMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_multicloud_sync_profile'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformMulticloudSyncProfile}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_multicloud_sync_profile'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_multicloud_sync_profile}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_multicloud_sync_profile")
public class PlatformMulticloudSyncProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "provider_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String providerName;

    @Column(name = "target_endpoint", nullable = false)
    @NotNull
    @Size(max = 250)
    private String targetEndpoint;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

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
     * Retrieves provider name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProviderName() { return providerName; }
    /**
     * Performs the setProviderName operation in this module.
     *
     * @param providerName the providerName input value
     */
    public void setProviderName(String providerName) { this.providerName = providerName; }
    /**
     * Retrieves target endpoint data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTargetEndpoint() { return targetEndpoint; }
    /**
     * Performs the setTargetEndpoint operation in this module.
     *
     * @param targetEndpoint the targetEndpoint input value
     */
    public void setTargetEndpoint(String targetEndpoint) { this.targetEndpoint = targetEndpoint; }
    /**
     * Retrieves active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(Boolean active) { this.active = active; }
}