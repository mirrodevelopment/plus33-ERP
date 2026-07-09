/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformSecretDefinition.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformSecretDefinitionController
 * Related Service   : PlatformSecretDefinitionService, PlatformSecretDefinitionServiceImpl
 * Related Repository: PlatformSecretDefinitionRepository
 * Related Entity    : PlatformSecretDefinition
 * Related DTO       : N/A
 * Related Mapper    : PlatformSecretDefinitionMapper
 * Related DB Table  : platform_secret_definition
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformSecretDefinitionRepository, PlatformSecretDefinitionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_secret_definition'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformSecretDefinition}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_secret_definition'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_secret_definition}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_secret_definition")
public class PlatformSecretDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "alias_path", nullable = false, unique = true)
    @NotNull
    @Size(max = 250)
    private String aliasPath;

    @Column(name = "secret_key", nullable = false)
    @NotNull
    @Size(max = 250)
    private String secretKey;

    @Column(name = "rotation_policy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String rotationPolicy;

    @Column(name = "next_rotation", nullable = false)
    @NotNull
    private LocalDateTime nextRotation;

    @Column(name = "last_rotation")
    private LocalDateTime lastRotation;

    @Column(name = "provider_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String providerVersion = "1";

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves alias path data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAliasPath() { return aliasPath; }
    /**
     * Performs the setAliasPath operation in this module.
     *
     * @param aliasPath the aliasPath input value
     */
    public void setAliasPath(String aliasPath) { this.aliasPath = aliasPath; }
    /**
     * Retrieves secret key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSecretKey() { return secretKey; }
    /**
     * Performs the setSecretKey operation in this module.
     *
     * @param secretKey the secretKey input value
     */
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    /**
     * Retrieves rotation policy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRotationPolicy() { return rotationPolicy; }
    /**
     * Performs the setRotationPolicy operation in this module.
     *
     * @param rotationPolicy the rotationPolicy input value
     */
    public void setRotationPolicy(String rotationPolicy) { this.rotationPolicy = rotationPolicy; }
    /**
     * Retrieves next rotation data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getNextRotation() { return nextRotation; }
    /**
     * Performs the setNextRotation operation in this module.
     *
     * @param nextRotation the nextRotation input value
     */
    public void setNextRotation(LocalDateTime nextRotation) { this.nextRotation = nextRotation; }
    /**
     * Retrieves last rotation data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLastRotation() { return lastRotation; }
    /**
     * Performs the setLastRotation operation in this module.
     *
     * @param lastRotation the lastRotation input value
     */
    public void setLastRotation(LocalDateTime lastRotation) { this.lastRotation = lastRotation; }
    /**
     * Retrieves provider version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProviderVersion() { return providerVersion; }
    /**
     * Performs the setProviderVersion operation in this module.
     *
     * @param providerVersion the providerVersion input value
     */
    public void setProviderVersion(String providerVersion) { this.providerVersion = providerVersion; }
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