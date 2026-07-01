package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_alert_rule")
public class BiAlertRule {
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
    public String getRuleName() { return ruleName; } public void setRuleName(String v) { this.ruleName = v; }
    public Long getKpiId() { return kpiId; } public void setKpiId(Long v) { this.kpiId = v; }
    public String getDatasetName() { return datasetName; } public void setDatasetName(String v) { this.datasetName = v; }
    public String getConditionType() { return conditionType; } public void setConditionType(String v) { this.conditionType = v; }
    public BigDecimal getThresholdValue() { return thresholdValue; } public void setThresholdValue(BigDecimal v) { this.thresholdValue = v; }
    public String getSeverity() { return severity; } public void setSeverity(String v) { this.severity = v; }
    public String getNotificationType() { return notificationType; } public void setNotificationType(String v) { this.notificationType = v; }
    public String getRecipients() { return recipients; } public void setRecipients(String v) { this.recipients = v; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean v) { this.isActive = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
