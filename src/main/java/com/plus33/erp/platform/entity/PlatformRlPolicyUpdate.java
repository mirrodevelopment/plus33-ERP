/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformRlPolicyUpdate.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRlPolicyUpdateController
 * Related Service   : PlatformRlPolicyUpdateService, PlatformRlPolicyUpdateServiceImpl
 * Related Repository: PlatformRlPolicyUpdateRepository
 * Related Entity    : PlatformRlPolicyUpdate
 * Related DTO       : N/A
 * Related Mapper    : PlatformRlPolicyUpdateMapper
 * Related DB Table  : platform_rl_policy_update
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRlPolicyUpdateRepository, PlatformRlPolicyUpdateMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_rl_policy_update'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformRlPolicyUpdate}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_rl_policy_update'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_rl_policy_update}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_rl_policy_update")
public class PlatformRlPolicyUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_id", nullable = false)
    @NotNull
    private Long policyId;

    @Column(name = "action_taken", nullable = false)
    @NotNull
    @Size(max = 150)
    private String actionTaken;

    @Column(nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal reward;

    @Column(name = "state_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String stateJson;

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
     * Retrieves policy id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPolicyId() { return policyId; }
    /**
     * Performs the setPolicyId operation in this module.
     *
     * @param policyId the policyId input value
     */
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    /**
     * Retrieves action taken data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getActionTaken() { return actionTaken; }
    /**
     * Performs the setActionTaken operation in this module.
     *
     * @param actionTaken the actionTaken input value
     */
    public void setActionTaken(String actionTaken) { this.actionTaken = actionTaken; }
    /**
     * Retrieves reward data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getReward() { return reward; }
    /**
     * Performs the setReward operation in this module.
     *
     * @param reward the reward input value
     */
    public void setReward(BigDecimal reward) { this.reward = reward; }
    /**
     * Retrieves state json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStateJson() { return stateJson; }
    /**
     * Performs the setStateJson operation in this module.
     *
     * @param stateJson the stateJson input value
     */
    public void setStateJson(String stateJson) { this.stateJson = stateJson; }
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