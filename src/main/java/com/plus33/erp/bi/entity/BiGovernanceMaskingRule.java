/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiGovernanceMaskingRule.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiGovernanceMaskingRuleController
 * Related Service   : BiGovernanceMaskingRuleService, BiGovernanceMaskingRuleServiceImpl
 * Related Repository: BiGovernanceMaskingRuleRepository
 * Related Entity    : BiGovernanceMaskingRule
 * Related DTO       : N/A
 * Related Mapper    : BiGovernanceMaskingRuleMapper
 * Related DB Table  : bi_governance_masking_rule
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiGovernanceMaskingRuleRepository, BiGovernanceMaskingRuleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_governance_masking_rule'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiGovernanceMaskingRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_governance_masking_rule'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_governance_masking_rule}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_governance_masking_rule")
public class BiGovernanceMaskingRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "rule_name", nullable = false, unique = true)
    private String ruleName;
    @Column(name = "classification_level", nullable = false)
    private String classificationLevel;
    @Column(name = "masking_type", nullable = false)
    private String maskingType;
    @Column(name = "masking_pattern")
    private String maskingPattern;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves rule name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRuleName() { return ruleName; }
    /**
     * Performs the setRuleName operation in this module.
     *
     * @param ruleName the ruleName input value
     */
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    /**
     * Retrieves classification level data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getClassificationLevel() { return classificationLevel; }
    /**
     * Performs the setClassificationLevel operation in this module.
     *
     * @param classificationLevel the classificationLevel input value
     */
    public void setClassificationLevel(String classificationLevel) { this.classificationLevel = classificationLevel; }
    /**
     * Retrieves masking type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMaskingType() { return maskingType; }
    /**
     * Performs the setMaskingType operation in this module.
     *
     * @param maskingType the maskingType input value
     */
    public void setMaskingType(String maskingType) { this.maskingType = maskingType; }
    /**
     * Retrieves masking pattern data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMaskingPattern() { return maskingPattern; }
    /**
     * Performs the setMaskingPattern operation in this module.
     *
     * @param maskingPattern the maskingPattern input value
     */
    public void setMaskingPattern(String maskingPattern) { this.maskingPattern = maskingPattern; }
    /**
     * Retrieves is active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIsActive() { return isActive; }
    /**
     * Performs the setIsActive operation in this module.
     *
     * @param isActive the isActive input value
     */
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
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
}