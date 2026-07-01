package com.plus33.erp.procurement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "procurement_policies")
public class ProcurementPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "policy_type", nullable = false, length = 50)
    private String policyType;

    @Column(name = "threshold_amount")
    private BigDecimal thresholdAmount;

    @Column(name = "preferred_supplier_id")
    private Long preferredSupplierId;

    @Column(nullable = false)
    private Boolean active = true;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getPolicyType() { return policyType; }
    public void setPolicyType(String policyType) { this.policyType = policyType; }
    public BigDecimal getThresholdAmount() { return thresholdAmount; }
    public void setThresholdAmount(BigDecimal thresholdAmount) { this.thresholdAmount = thresholdAmount; }
    public Long getPreferredSupplierId() { return preferredSupplierId; }
    public void setPreferredSupplierId(Long preferredSupplierId) { this.preferredSupplierId = preferredSupplierId; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
