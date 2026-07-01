package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "grc_risk_appetite_statements")
public class RiskAppetiteStatement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "risk_category", nullable = false, length = 50) private String riskCategory;
    @Column(name = "max_residual_score", nullable = false) private BigDecimal maxResidualScore;
    @Column(name = "escalation_threshold", nullable = false) private BigDecimal escalationThreshold;
    @Column(name = "approval_authority", nullable = false, length = 100) private String approvalAuthority;
    @Column(name = "effective_from", nullable = false) private LocalDate effectiveFrom;
    @Column(name = "effective_to", nullable = false) private LocalDate effectiveTo;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getRiskCategory() { return riskCategory; } public void setRiskCategory(String v) { this.riskCategory = v; }
    public BigDecimal getMaxResidualScore() { return maxResidualScore; } public void setMaxResidualScore(BigDecimal v) { this.maxResidualScore = v; }
    public BigDecimal getEscalationThreshold() { return escalationThreshold; } public void setEscalationThreshold(BigDecimal v) { this.escalationThreshold = v; }
    public String getApprovalAuthority() { return approvalAuthority; } public void setApprovalAuthority(String v) { this.approvalAuthority = v; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; } public void setEffectiveFrom(LocalDate v) { this.effectiveFrom = v; }
    public LocalDate getEffectiveTo() { return effectiveTo; } public void setEffectiveTo(LocalDate v) { this.effectiveTo = v; }
}
