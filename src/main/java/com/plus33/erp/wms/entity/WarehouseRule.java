/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : WarehouseRule.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseRuleController
 * Related Service   : WarehouseRuleService, WarehouseRuleServiceImpl
 * Related Repository: WarehouseRuleRepository
 * Related Entity    : WarehouseRule
 * Related DTO       : N/A
 * Related Mapper    : WarehouseRuleMapper
 * Related DB Table  : warehouse_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseRuleRepository, WarehouseRuleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'warehouse_rules'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'warehouse_rules'.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_rules}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "warehouse_rules")
public class WarehouseRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "rule_code", nullable = false, unique = true, length = 50)
    private String ruleCode;

    @Column(name = "rule_name", nullable = false, length = 100)
    private String ruleName;

    @Column(name = "rule_type", nullable = false, length = 30)
    private String ruleType;

    @Column(name = "condition_expression", nullable = false, columnDefinition = "TEXT")
    private String conditionExpression;

    @Column(name = "action_expression", nullable = false, columnDefinition = "TEXT")
    private String actionExpression;

    @Column(nullable = false)
    private int priority = 5;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
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
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves rule code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRuleCode() { return ruleCode; }
    /**
     * Performs the setRuleCode operation in this module.
     *
     * @param ruleCode the ruleCode input value
     */
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
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
     * Retrieves rule type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRuleType() { return ruleType; }
    /**
     * Performs the setRuleType operation in this module.
     *
     * @param ruleType the ruleType input value
     */
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }
    /**
     * Retrieves condition expression data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConditionExpression() { return conditionExpression; }
    /**
     * Performs the setConditionExpression operation in this module.
     *
     * @param conditionExpression the conditionExpression input value
     */
    public void setConditionExpression(String conditionExpression) { this.conditionExpression = conditionExpression; }
    /**
     * Retrieves action expression data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getActionExpression() { return actionExpression; }
    /**
     * Performs the setActionExpression operation in this module.
     *
     * @param actionExpression the actionExpression input value
     */
    public void setActionExpression(String actionExpression) { this.actionExpression = actionExpression; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(int priority) { this.priority = priority; }
    /**
     * Performs the isActive operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(boolean active) { this.active = active; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}