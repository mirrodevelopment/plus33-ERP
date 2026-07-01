package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getClassificationLevel() { return classificationLevel; }
    public void setClassificationLevel(String classificationLevel) { this.classificationLevel = classificationLevel; }
    public String getMaskingType() { return maskingType; }
    public void setMaskingType(String maskingType) { this.maskingType = maskingType; }
    public String getMaskingPattern() { return maskingPattern; }
    public void setMaskingPattern(String maskingPattern) { this.maskingPattern = maskingPattern; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}