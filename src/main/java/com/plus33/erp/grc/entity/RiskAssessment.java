package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "grc_risk_assessments")
public class RiskAssessment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "risk_id", nullable = false) private Long riskId;
    @Column(nullable = false) private BigDecimal probability;
    @Column(nullable = false) private BigDecimal impact;
    @Column(name = "residual_score", nullable = false) private BigDecimal residualScore;
    @Column(name = "assessment_date", nullable = false) private LocalDate assessmentDate;
    @Column(name = "assessor_id") private Long assessorId;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getRiskId() { return riskId; } public void setRiskId(Long v) { this.riskId = v; }
    public BigDecimal getProbability() { return probability; } public void setProbability(BigDecimal v) { this.probability = v; }
    public BigDecimal getImpact() { return impact; } public void setImpact(BigDecimal v) { this.impact = v; }
    public BigDecimal getResidualScore() { return residualScore; } public void setResidualScore(BigDecimal v) { this.residualScore = v; }
    public LocalDate getAssessmentDate() { return assessmentDate; } public void setAssessmentDate(LocalDate v) { this.assessmentDate = v; }
    public Long getAssessorId() { return assessorId; } public void setAssessorId(Long v) { this.assessorId = v; }
}
