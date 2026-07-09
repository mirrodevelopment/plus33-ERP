/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDeviceConfigProfile.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDeviceConfigProfileController
 * Related Service   : PlatformDeviceConfigProfileService, PlatformDeviceConfigProfileServiceImpl
 * Related Repository: PlatformDeviceConfigProfileRepository
 * Related Entity    : PlatformDeviceConfigProfile
 * Related DTO       : N/A
 * Related Mapper    : PlatformDeviceConfigProfileMapper
 * Related DB Table  : platform_device_config_profile
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDeviceConfigProfileRepository, PlatformDeviceConfigProfileMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_device_config_profile'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDeviceConfigProfile}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_device_config_profile'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_device_config_profile}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_device_config_profile")
public class PlatformDeviceConfigProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "profile_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String profileCode;

    @Column(name = "profile_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String profileName;

    @Column(name = "profile_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String profileVersion;

    @Column(nullable = false)
    @NotNull
    @Size(max = 64)
    private String checksum;

    @Column(name = "configuration_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String configurationJson;

    @Column(name = "rollback_profile_id")
    private Long rollbackProfileId;

    @Column(name = "effective_from")
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

    @Column(name = "assignment_scope", nullable = false)
    @NotNull
    @Size(max = 200)
    private String assignmentScope; // Base Profile, Warehouse Profile, POS Profile, Edge Gateway Profile

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
     * Retrieves profile code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProfileCode() { return profileCode; }
    /**
     * Performs the setProfileCode operation in this module.
     *
     * @param profileCode the profileCode input value
     */
    public void setProfileCode(String profileCode) { this.profileCode = profileCode; }
    /**
     * Retrieves profile name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProfileName() { return profileName; }
    /**
     * Performs the setProfileName operation in this module.
     *
     * @param profileName the profileName input value
     */
    public void setProfileName(String profileName) { this.profileName = profileName; }
    /**
     * Retrieves profile version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProfileVersion() { return profileVersion; }
    /**
     * Performs the setProfileVersion operation in this module.
     *
     * @param profileVersion the profileVersion input value
     */
    public void setProfileVersion(String profileVersion) { this.profileVersion = profileVersion; }
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
     * Retrieves rollback profile id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRollbackProfileId() { return rollbackProfileId; }
    /**
     * Performs the setRollbackProfileId operation in this module.
     *
     * @param rollbackProfileId the rollbackProfileId input value
     */
    public void setRollbackProfileId(Long rollbackProfileId) { this.rollbackProfileId = rollbackProfileId; }
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
     * Retrieves assignment scope data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAssignmentScope() { return assignmentScope; }
    /**
     * Performs the setAssignmentScope operation in this module.
     *
     * @param assignmentScope the assignmentScope input value
     */
    public void setAssignmentScope(String assignmentScope) { this.assignmentScope = assignmentScope; }
}