package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_risks")
public class EnterpriseRisk {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "risk_number", nullable = false, unique = true, length = 50) private String riskNumber;
    @Column(nullable = false, length = 200) private String title;
    @Column(nullable = false, length = 50) private String category;
    @Column(length = 100) private String domain;
    @Column(name = "inherent_score", nullable = false) private BigDecimal inherentScore = BigDecimal.ZERO;
    @Column(name = "residual_score", nullable = false) private BigDecimal residualScore = BigDecimal.ZERO;
    @Column(nullable = false, length = 30) private String status = "IDENTIFIED";
    @Column(name = "owner_employee_id") private Long ownerEmployeeId;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getRiskNumber() { return riskNumber; } public void setRiskNumber(String v) { this.riskNumber = v; }
    public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
    public String getCategory() { return category; } public void setCategory(String v) { this.category = v; }
    public String getDomain() { return domain; } public void setDomain(String v) { this.domain = v; }
    public BigDecimal getInherentScore() { return inherentScore; } public void setInherentScore(BigDecimal v) { this.inherentScore = v; }
    public BigDecimal getResidualScore() { return residualScore; } public void setResidualScore(BigDecimal v) { this.residualScore = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public Long getOwnerEmployeeId() { return ownerEmployeeId; } public void setOwnerEmployeeId(Long v) { this.ownerEmployeeId = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
