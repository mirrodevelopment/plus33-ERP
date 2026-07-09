/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTelemetryRetentionPolicy.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTelemetryRetentionPolicyController
 * Related Service   : PlatformTelemetryRetentionPolicyService, PlatformTelemetryRetentionPolicyServiceImpl
 * Related Repository: PlatformTelemetryRetentionPolicyRepository
 * Related Entity    : PlatformTelemetryRetentionPolicy
 * Related DTO       : N/A
 * Related Mapper    : PlatformTelemetryRetentionPolicyMapper
 * Related DB Table  : platform_telemetry_retention_policy
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTelemetryRetentionPolicyRepository, PlatformTelemetryRetentionPolicyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_telemetry_retention_policy'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTelemetryRetentionPolicy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_telemetry_retention_policy'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_telemetry_retention_policy}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_telemetry_retention_policy")
public class PlatformTelemetryRetentionPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "instance_id", nullable = false, unique = true)
    @NotNull
    private Long instanceId;

    @Column(name = "retention_days", nullable = false)
    @NotNull
    private Integer retentionDays = 30;

    @Column(name = "archival_target", nullable = false)
    @NotNull
    @Size(max = 150)
    private String archivalTarget = "S3_COLD";

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
     * Retrieves retention days data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRetentionDays() { return retentionDays; }
    /**
     * Performs the setRetentionDays operation in this module.
     *
     * @param retentionDays the retentionDays input value
     */
    public void setRetentionDays(Integer retentionDays) { this.retentionDays = retentionDays; }
    /**
     * Retrieves archival target data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getArchivalTarget() { return archivalTarget; }
    /**
     * Performs the setArchivalTarget operation in this module.
     *
     * @param archivalTarget the archivalTarget input value
     */
    public void setArchivalTarget(String archivalTarget) { this.archivalTarget = archivalTarget; }
}