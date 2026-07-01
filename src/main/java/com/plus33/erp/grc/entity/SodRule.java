package com.plus33.erp.grc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grc_sod_rules")
public class SodRule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "rule_name", nullable = false, length = 150) private String ruleName;
    @Column(name = "role_a", nullable = false, length = 100) private String roleA;
    @Column(name = "role_b", nullable = false, length = 100) private String roleB;
    @Column(name = "risk_level", nullable = false, length = 20) private String riskLevel = "HIGH";
    @Column(name = "sod_type", nullable = false, length = 20) private String sodType = "PREVENTIVE";

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getRuleName() { return ruleName; } public void setRuleName(String v) { this.ruleName = v; }
    public String getRoleA() { return roleA; } public void setRoleA(String v) { this.roleA = v; }
    public String getRoleB() { return roleB; } public void setRoleB(String v) { this.roleB = v; }
    public String getRiskLevel() { return riskLevel; } public void setRiskLevel(String v) { this.riskLevel = v; }
    public String getSodType() { return sodType; } public void setSodType(String v) { this.sodType = v; }
}
