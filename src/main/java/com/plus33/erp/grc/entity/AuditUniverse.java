package com.plus33.erp.grc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grc_audit_universe")
public class AuditUniverse {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "entity_name", nullable = false, length = 150) private String entityName;
    @Column(name = "entity_type", nullable = false, length = 50) private String entityType;
    @Column(name = "risk_score") private java.math.BigDecimal riskScore = java.math.BigDecimal.ZERO;
    @Column(name = "last_audited") private java.time.LocalDate lastAudited;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getEntityName() { return entityName; } public void setEntityName(String v) { this.entityName = v; }
    public String getEntityType() { return entityType; } public void setEntityType(String v) { this.entityType = v; }
    public java.math.BigDecimal getRiskScore() { return riskScore; } public void setRiskScore(java.math.BigDecimal v) { this.riskScore = v; }
    public java.time.LocalDate getLastAudited() { return lastAudited; } public void setLastAudited(java.time.LocalDate v) { this.lastAudited = v; }
}
