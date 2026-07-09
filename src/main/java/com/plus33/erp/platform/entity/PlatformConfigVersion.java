/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformConfigVersion.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformConfigVersionController
 * Related Service   : PlatformConfigVersionService, PlatformConfigVersionServiceImpl
 * Related Repository: PlatformConfigVersionRepository
 * Related Entity    : PlatformConfigVersion
 * Related DTO       : N/A
 * Related Mapper    : PlatformConfigVersionMapper
 * Related DB Table  : platform_config_version
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformConfigVersionRepository, PlatformConfigVersionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_config_version'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformConfigVersion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_config_version'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_config_version}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_config_version", uniqueConstraints = @UniqueConstraint(columnNames = {"config_id", "version"}))
public class PlatformConfigVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_id", nullable = false)
    @NotNull
    private Long configId;

    @Column(nullable = false)
    @NotNull
    private Integer version;

    @Column(name = "previous_version")
    private Integer previousVersion;

    @Column(name = "config_value", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String configValue;

    @Column(name = "effective_from", nullable = false)
    @NotNull
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String checksum;

    @Column(name = "modified_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String modifiedBy;

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
     * Retrieves config id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getConfigId() { return configId; }
    /**
     * Performs the setConfigId operation in this module.
     *
     * @param configId the configId input value
     */
    public void setConfigId(Long configId) { this.configId = configId; }
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
     * Retrieves previous version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPreviousVersion() { return previousVersion; }
    /**
     * Performs the setPreviousVersion operation in this module.
     *
     * @param previousVersion the previousVersion input value
     */
    public void setPreviousVersion(Integer previousVersion) { this.previousVersion = previousVersion; }

    /**
     * Retrieves config value data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConfigValue() { return configValue; }
    /**
     * Performs the setConfigValue operation in this module.
     *
     * @param configValue the configValue input value
     */
    public void setConfigValue(String configValue) { this.configValue = configValue; }
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
    /**
     * Retrieves checksum data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChecksum() { return checksum; }
    /**
     * Performs the setChecksum operation in this module.
     *
     * @param checksum the checksum input value
     */
    public void setChecksum(String checksum) { this.checksum = checksum; }
    /**
     * Retrieves modified by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getModifiedBy() { return modifiedBy; }
    /**
     * Performs the setModifiedBy operation in this module.
     *
     * @param modifiedBy the modifiedBy input value
     */
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }
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