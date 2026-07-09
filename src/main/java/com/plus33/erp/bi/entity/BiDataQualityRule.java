/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiDataQualityRule.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiDataQualityRuleController
 * Related Service   : BiDataQualityRuleService, BiDataQualityRuleServiceImpl
 * Related Repository: BiDataQualityRuleRepository
 * Related Entity    : BiDataQualityRule
 * Related DTO       : N/A
 * Related Mapper    : BiDataQualityRuleMapper
 * Related DB Table  : bi_quality_rule
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiDataQualityRuleRepository, BiDataQualityRuleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_quality_rule'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiDataQualityRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_quality_rule'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_quality_rule}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_quality_rule")
public class BiDataQualityRule {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "rule_name", nullable = false, unique = true, length = 200) private String ruleName;
    @Column(name = "rule_type", nullable = false, length = 50) private String ruleType;
    @Column(name = "source_table", nullable = false, length = 100) private String sourceTable;
    @Column(name = "column_name", length = 100) private String columnName;
    @Column(name = "rule_expression", nullable = false, columnDefinition = "TEXT") private String ruleExpression;
    @Column(nullable = false, length = 20) private String severity = "ERROR";
    @Column(name = "is_active", nullable = false) private Boolean isActive = true;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves rule name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRuleName() { return ruleName; } public void setRuleName(String v) { this.ruleName = v; }
    /**
     * Retrieves rule type data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRuleType() { return ruleType; } public void setRuleType(String v) { this.ruleType = v; }
    /**
     * Retrieves source table data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceTable() { return sourceTable; } public void setSourceTable(String v) { this.sourceTable = v; }
    /**
     * Retrieves column name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getColumnName() { return columnName; } public void setColumnName(String v) { this.columnName = v; }
    /**
     * Retrieves rule expression data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRuleExpression() { return ruleExpression; } public void setRuleExpression(String v) { this.ruleExpression = v; }
    /**
     * Retrieves severity data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSeverity() { return severity; } public void setSeverity(String v) { this.severity = v; }
    /**
     * Retrieves is active data from the database.
     *
     * @param v the v input value
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean v) { this.isActive = v; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}