/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformPolicyAudit.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformPolicyAuditController
 * Related Service   : PlatformPolicyAuditService, PlatformPolicyAuditServiceImpl
 * Related Repository: PlatformPolicyAuditRepository
 * Related Entity    : PlatformPolicyAudit
 * Related DTO       : N/A
 * Related Mapper    : PlatformPolicyAuditMapper
 * Related DB Table  : platform_policy_audit
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformPolicyAuditRepository, PlatformPolicyAuditMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_policy_audit'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformPolicyAudit}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_policy_audit'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_policy_audit}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_policy_audit")
public class PlatformPolicyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String policyCode;

    @Column(name = "user_identity", nullable = false)
    @NotNull
    @Size(max = 100)
    private String userIdentity;

    @Column(nullable = false)
    @NotNull
    @Size(max = 150)
    private String action;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String decision = "DENY";

    @Column(name = "evaluated_at", nullable = false)
    @NotNull
    private LocalDateTime evaluatedAt = LocalDateTime.now();

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
     * Retrieves user identity data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getUserIdentity() { return userIdentity; }
    /**
     * Performs the setUserIdentity operation in this module.
     *
     * @param userIdentity the userIdentity input value
     */
    public void setUserIdentity(String userIdentity) { this.userIdentity = userIdentity; }
    /**
     * Retrieves action data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAction() { return action; }
    /**
     * Performs the setAction operation in this module.
     *
     * @param action the action input value
     */
    public void setAction(String action) { this.action = action; }
    /**
     * Retrieves decision data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDecision() { return decision; }
    /**
     * Performs the setDecision operation in this module.
     *
     * @param decision the decision input value
     */
    public void setDecision(String decision) { this.decision = decision; }
    /**
     * Retrieves evaluated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getEvaluatedAt() { return evaluatedAt; }
    /**
     * Performs the setEvaluatedAt operation in this module.
     *
     * @param evaluatedAt the evaluatedAt input value
     */
    public void setEvaluatedAt(LocalDateTime evaluatedAt) { this.evaluatedAt = evaluatedAt; }
}