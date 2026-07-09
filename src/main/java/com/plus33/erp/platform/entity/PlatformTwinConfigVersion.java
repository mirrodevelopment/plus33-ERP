/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTwinConfigVersion.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTwinConfigVersionController
 * Related Service   : PlatformTwinConfigVersionService, PlatformTwinConfigVersionServiceImpl
 * Related Repository: PlatformTwinConfigVersionRepository
 * Related Entity    : PlatformTwinConfigVersion
 * Related DTO       : N/A
 * Related Mapper    : PlatformTwinConfigVersionMapper
 * Related DB Table  : platform_twin_config_version
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTwinConfigVersionRepository, PlatformTwinConfigVersionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_twin_config_version'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTwinConfigVersion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_twin_config_version'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_twin_config_version}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_twin_config_version")
public class PlatformTwinConfigVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "instance_id", nullable = false)
    @NotNull
    private Long instanceId;

    @Column(name = "config_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String configVersion;

    @Column(name = "configuration_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String configurationJson;

    @Column(name = "effective_from", nullable = false)
    @NotNull
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

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
     * Retrieves instance id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getInstanceId() { return instanceId; }
    /**
     * Performs the setInstanceId operation in this module.
     *
     * @param instanceId the instanceId input value
     */
    public void setInstanceId(Long instanceId) { this.instanceId = instanceId; }
    /**
     * Retrieves config version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConfigVersion() { return configVersion; }
    /**
     * Performs the setConfigVersion operation in this module.
     *
     * @param configVersion the configVersion input value
     */
    public void setConfigVersion(String configVersion) { this.configVersion = configVersion; }
    /**
     * Retrieves configuration json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConfigurationJson() { return configurationJson; }
    /**
     * Performs the setConfigurationJson operation in this module.
     *
     * @param configurationJson the configurationJson input value
     */
    public void setConfigurationJson(String configurationJson) { this.configurationJson = configurationJson; }
    /**
     * Retrieves effective from data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getEffectiveFrom() { return effectiveFrom; }
    /**
     * Performs the setEffectiveFrom operation in this module.
     *
     * @param effectiveFrom the effectiveFrom input value
     */
    public void setEffectiveFrom(LocalDateTime effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    /**
     * Retrieves effective to data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getEffectiveTo() { return effectiveTo; }
    /**
     * Performs the setEffectiveTo operation in this module.
     *
     * @param effectiveTo the effectiveTo input value
     */
    public void setEffectiveTo(LocalDateTime effectiveTo) { this.effectiveTo = effectiveTo; }
}