package com.plus33.erp.procurement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "procurement_supplier_qualifications")
public class SupplierQualification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supplier_id", nullable = false, unique = true)
    private Long supplierId;

    @Column(nullable = false, length = 30)
    private String status = "ONBOARDING";

    @Column(name = "risk_score_financial")
    private BigDecimal riskScoreFinancial = BigDecimal.ZERO;

    @Column(name = "risk_score_compliance")
    private BigDecimal riskScoreCompliance = BigDecimal.ZERO;

    @Column(name = "risk_score_esg")
    private BigDecimal riskScoreEsg = BigDecimal.ZERO;

    @Column(name = "consolidated_risk_level", nullable = false, length = 20)
    private String consolidatedRiskLevel = "LOW";

    @Column(name = "approved_vendor_list", nullable = false)
    private Boolean approvedVendorList = false;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getRiskScoreFinancial() { return riskScoreFinancial; }
    public void setRiskScoreFinancial(BigDecimal riskScoreFinancial) { this.riskScoreFinancial = riskScoreFinancial; }
    public BigDecimal getRiskScoreCompliance() { return riskScoreCompliance; }
    public void setRiskScoreCompliance(BigDecimal riskScoreCompliance) { this.riskScoreCompliance = riskScoreCompliance; }
    public BigDecimal getRiskScoreEsg() { return riskScoreEsg; }
    public void setRiskScoreEsg(BigDecimal riskScoreEsg) { this.riskScoreEsg = riskScoreEsg; }
    public String getConsolidatedRiskLevel() { return consolidatedRiskLevel; }
    public void setConsolidatedRiskLevel(String consolidatedRiskLevel) { this.consolidatedRiskLevel = consolidatedRiskLevel; }
    public Boolean getApprovedVendorList() { return approvedVendorList; }
    public void setApprovedVendorList(Boolean approvedVendorList) { this.approvedVendorList = approvedVendorList; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
