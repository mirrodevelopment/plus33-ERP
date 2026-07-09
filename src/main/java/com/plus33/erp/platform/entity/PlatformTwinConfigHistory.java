/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTwinConfigHistory.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTwinConfigHistoryController
 * Related Service   : PlatformTwinConfigHistoryService, PlatformTwinConfigHistoryServiceImpl
 * Related Repository: PlatformTwinConfigHistoryRepository
 * Related Entity    : PlatformTwinConfigHistory
 * Related DTO       : N/A
 * Related Mapper    : PlatformTwinConfigHistoryMapper
 * Related DB Table  : platform_twin_config_history
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTwinConfigHistoryRepository, PlatformTwinConfigHistoryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_twin_config_history'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTwinConfigHistory}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_twin_config_history'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_twin_config_history}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_twin_config_history")
public class PlatformTwinConfigHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instance_id", nullable = false)
    @NotNull
    private Long instanceId;

    @Column(name = "previous_version")
    @Size(max = 50)
    private String previousVersion;

    @Column(name = "new_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String newVersion;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "changed_at", nullable = false)
    @NotNull
    private LocalDateTime changedAt = LocalDateTime.now();

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
     * Retrieves previous version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPreviousVersion() { return previousVersion; }
    /**
     * Performs the setPreviousVersion operation in this module.
     *
     * @param previousVersion the previousVersion input value
     */
    public void setPreviousVersion(String previousVersion) { this.previousVersion = previousVersion; }
    /**
     * Retrieves new version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNewVersion() { return newVersion; }
    /**
     * Performs the setNewVersion operation in this module.
     *
     * @param newVersion the newVersion input value
     */
    public void setNewVersion(String newVersion) { this.newVersion = newVersion; }
    /**
     * Retrieves operator data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOperator() { return operator; }
    /**
     * Performs the setOperator operation in this module.
     *
     * @param operator the operator input value
     */
    public void setOperator(String operator) { this.operator = operator; }
    /**
     * Retrieves reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReason() { return reason; }
    /**
     * Performs the setReason operation in this module.
     *
     * @param reason the reason input value
     */
    public void setReason(String reason) { this.reason = reason; }
    /**
     * Retrieves changed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getChangedAt() { return changedAt; }
    /**
     * Performs the setChangedAt operation in this module.
     *
     * @param changedAt the changedAt input value
     */
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}