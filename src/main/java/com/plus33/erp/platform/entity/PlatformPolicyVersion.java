/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformPolicyVersion.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformPolicyVersionController
 * Related Service   : PlatformPolicyVersionService, PlatformPolicyVersionServiceImpl
 * Related Repository: PlatformPolicyVersionRepository
 * Related Entity    : PlatformPolicyVersion
 * Related DTO       : N/A
 * Related Mapper    : PlatformPolicyVersionMapper
 * Related DB Table  : platform_policy_version
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformPolicyVersionRepository, PlatformPolicyVersionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_policy_version'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformPolicyVersion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_policy_version'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_policy_version}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_policy_version")
public class PlatformPolicyVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "policy_id", nullable = false)
    @NotNull
    private Long policyId;

    @Column(name = "policy_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String policyVersion;

    @Column(name = "rego_content", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String regoContent;

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
     * Retrieves policy version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPolicyVersion() { return policyVersion; }
    /**
     * Performs the setPolicyVersion operation in this module.
     *
     * @param policyVersion the policyVersion input value
     */
    public void setPolicyVersion(String policyVersion) { this.policyVersion = policyVersion; }
    /**
     * Retrieves rego content data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRegoContent() { return regoContent; }
    /**
     * Performs the setRegoContent operation in this module.
     *
     * @param regoContent the regoContent input value
     */
    public void setRegoContent(String regoContent) { this.regoContent = regoContent; }
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