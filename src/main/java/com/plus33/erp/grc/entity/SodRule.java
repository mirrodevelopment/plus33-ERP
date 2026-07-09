/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : SodRule.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SodRuleController
 * Related Service   : SodRuleService, SodRuleServiceImpl
 * Related Repository: SodRuleRepository
 * Related Entity    : SodRule
 * Related DTO       : N/A
 * Related Mapper    : SodRuleMapper
 * Related DB Table  : grc_sod_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SodRuleRepository, SodRuleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_sod_rules'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code SodRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_sod_rules'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_sod_rules}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_sod_rules")
public class SodRule {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "rule_name", nullable = false, length = 150) private String ruleName;
    @Column(name = "role_a", nullable = false, length = 100) private String roleA;
    @Column(name = "role_b", nullable = false, length = 100) private String roleB;
    @Column(name = "risk_level", nullable = false, length = 20) private String riskLevel = "HIGH";
    @Column(name = "sod_type", nullable = false, length = 20) private String sodType = "PREVENTIVE";
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves company id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    /**
     * Retrieves rule name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRuleName() { return ruleName; } public void setRuleName(String v) { this.ruleName = v; }
    /**
     * Retrieves role a data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRoleA() { return roleA; } public void setRoleA(String v) { this.roleA = v; }
    /**
     * Retrieves role b data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRoleB() { return roleB; } public void setRoleB(String v) { this.roleB = v; }
    /**
     * Retrieves risk level data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRiskLevel() { return riskLevel; } public void setRiskLevel(String v) { this.riskLevel = v; }
    /**
     * Retrieves sod type data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSodType() { return sodType; } public void setSodType(String v) { this.sodType = v; }
}