/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformFeatureFlagHistory.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformFeatureFlagHistoryController
 * Related Service   : PlatformFeatureFlagHistoryService, PlatformFeatureFlagHistoryServiceImpl
 * Related Repository: PlatformFeatureFlagHistoryRepository
 * Related Entity    : PlatformFeatureFlagHistory
 * Related DTO       : N/A
 * Related Mapper    : PlatformFeatureFlagHistoryMapper
 * Related DB Table  : platform_feature_flag_history
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformFeatureFlagHistoryRepository, PlatformFeatureFlagHistoryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_feature_flag_history'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformFeatureFlagHistory}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_feature_flag_history'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_feature_flag_history}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_feature_flag_history")
public class PlatformFeatureFlagHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flag_key", nullable = false)
    @NotNull
    @Size(max = 100)
    private String flagKey;

    @Column(name = "previous_value")
    @Size(max = 50)
    private String previousValue;

    @Column(name = "new_value", nullable = false)
    @NotNull
    @Size(max = 50)
    private String newValue;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(length = 500)
    @Size(max = 500)
    private String reason;

    @Column(name = "rollout_percentage", nullable = false)
    @NotNull
    private Integer rolloutPercentage = 0;

    @Column(name = "changed_at", nullable = false, updatable = false)
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
     * Retrieves flag key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFlagKey() { return flagKey; }
    /**
     * Performs the setFlagKey operation in this module.
     *
     * @param flagKey the flagKey input value
     */
    public void setFlagKey(String flagKey) { this.flagKey = flagKey; }
    /**
     * Retrieves previous value data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPreviousValue() { return previousValue; }
    /**
     * Performs the setPreviousValue operation in this module.
     *
     * @param previousValue the previousValue input value
     */
    public void setPreviousValue(String previousValue) { this.previousValue = previousValue; }
    /**
     * Retrieves new value data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNewValue() { return newValue; }
    /**
     * Performs the setNewValue operation in this module.
     *
     * @param newValue the newValue input value
     */
    public void setNewValue(String newValue) { this.newValue = newValue; }
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
     * Retrieves rollout percentage data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRolloutPercentage() { return rolloutPercentage; }
    /**
     * Performs the setRolloutPercentage operation in this module.
     *
     * @param rolloutPercentage the rolloutPercentage input value
     */
    public void setRolloutPercentage(Integer rolloutPercentage) { this.rolloutPercentage = rolloutPercentage; }
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