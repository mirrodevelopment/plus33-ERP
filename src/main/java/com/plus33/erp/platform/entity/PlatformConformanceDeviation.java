package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_conformance_deviation")
public class PlatformConformanceDeviation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_id", nullable = false)
    @NotNull
    private Long caseId;

    @Column(name = "rule_id", nullable = false)
    @NotNull
    private Long ruleId;

    @Column(name = "deviation_details", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String deviationDetails;

    @Column(name = "sla_breach_risk", nullable = false)
    @NotNull
    private Boolean slaBreachRisk = false;

    @Column(name = "detected_at", nullable = false)
    @NotNull
    private LocalDateTime detectedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCaseId() { return caseId; }
    public void setCaseId(Long caseId) { this.caseId = caseId; }
    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    public String getDeviationDetails() { return deviationDetails; }
    public void setDeviationDetails(String deviationDetails) { this.deviationDetails = deviationDetails; }
    public Boolean getSlaBreachRisk() { return slaBreachRisk; }
    public void setSlaBreachRisk(Boolean slaBreachRisk) { this.slaBreachRisk = slaBreachRisk; }
    public LocalDateTime getDetectedAt() { return detectedAt; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
}