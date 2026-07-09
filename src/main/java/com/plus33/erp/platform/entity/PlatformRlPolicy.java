/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformRlPolicy.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRlPolicyController
 * Related Service   : PlatformRlPolicyService, PlatformRlPolicyServiceImpl
 * Related Repository: PlatformRlPolicyRepository
 * Related Entity    : PlatformRlPolicy
 * Related DTO       : N/A
 * Related Mapper    : PlatformRlPolicyMapper
 * Related DB Table  : platform_rl_policy
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRlPolicyRepository, PlatformRlPolicyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_rl_policy'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformRlPolicy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_rl_policy'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_rl_policy}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_rl_policy")
public class PlatformRlPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "policy_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String policyCode;

    @Column(name = "current_state_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String currentStateJson;

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
     * Retrieves policy code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPolicyCode() { return policyCode; }
    /**
     * Performs the setPolicyCode operation in this module.
     *
     * @param policyCode the policyCode input value
     */
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    /**
     * Retrieves current state json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCurrentStateJson() { return currentStateJson; }
    /**
     * Performs the setCurrentStateJson operation in this module.
     *
     * @param currentStateJson the currentStateJson input value
     */
    public void setCurrentStateJson(String currentStateJson) { this.currentStateJson = currentStateJson; }
}