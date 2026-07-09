/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiAlertRule.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiAlertRuleController
 * Related Service   : BiAlertRuleService, BiAlertRuleServiceImpl
 * Related Repository: BiAlertRuleRepository
 * Related Entity    : BiAlertRule
 * Related DTO       : N/A
 * Related Mapper    : BiAlertRuleMapper
 * Related DB Table  : bi_alert_rule
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiAlertRuleRepository, BiAlertRuleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_alert_rule'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiAlertRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_alert_rule'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_alert_rule}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_alert_rule")
public class BiAlertRule {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "rule_name", nullable = false, unique = true, length = 200) private String ruleName;
    @Column(name = "kpi_id") private Long kpiId;
    @Column(name = "dataset_name", length = 200) private String datasetName;
    @Column(name = "condition_type", nullable = false, length = 30) private String conditionType;
    @Column(name = "threshold_value", nullable = false, precision = 19, scale = 4) private BigDecimal thresholdValue;
    @Column(nullable = false, length = 20) private String severity = "WARNING";
    @Column(name = "notification_type", nullable = false, length = 30) private String notificationType = "EMAIL";
    @Column(columnDefinition = "TEXT") private String recipients;
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
     * Retrieves kpi id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getKpiId() { return kpiId; } public void setKpiId(Long v) { this.kpiId = v; }
    /**
     * Retrieves dataset name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDatasetName() { return datasetName; } public void setDatasetName(String v) { this.datasetName = v; }
    /**
     * Retrieves condition type data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConditionType() { return conditionType; } public void setConditionType(String v) { this.conditionType = v; }
    /**
     * Retrieves threshold value data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getThresholdValue() { return thresholdValue; } public void setThresholdValue(BigDecimal v) { this.thresholdValue = v; }
    /**
     * Retrieves severity data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSeverity() { return severity; } public void setSeverity(String v) { this.severity = v; }
    /**
     * Retrieves notification type data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNotificationType() { return notificationType; } public void setNotificationType(String v) { this.notificationType = v; }
    /**
     * Retrieves recipients data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRecipients() { return recipients; } public void setRecipients(String v) { this.recipients = v; }
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