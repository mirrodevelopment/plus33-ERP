package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_quality_rule")
public class BiDataQualityRule {
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
    public String getRuleName() { return ruleName; } public void setRuleName(String v) { this.ruleName = v; }
    public String getRuleType() { return ruleType; } public void setRuleType(String v) { this.ruleType = v; }
    public String getSourceTable() { return sourceTable; } public void setSourceTable(String v) { this.sourceTable = v; }
    public String getColumnName() { return columnName; } public void setColumnName(String v) { this.columnName = v; }
    public String getRuleExpression() { return ruleExpression; } public void setRuleExpression(String v) { this.ruleExpression = v; }
    public String getSeverity() { return severity; } public void setSeverity(String v) { this.severity = v; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean v) { this.isActive = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
