package com.plus33.erp.project.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "project_billing_milestones")
public class BillingMilestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_id", nullable = false)
    private Long contractId;

    @Column(name = "milestone_name", nullable = false, length = 100)
    private String milestoneName;

    @Column(name = "milestone_percentage", nullable = false)
    private BigDecimal milestonePercentage;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Boolean billed = false;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getMilestoneName() { return milestoneName; }
    public void setMilestoneName(String milestoneName) { this.milestoneName = milestoneName; }
    public BigDecimal getMilestonePercentage() { return milestonePercentage; }
    public void setMilestonePercentage(BigDecimal milestonePercentage) { this.milestonePercentage = milestonePercentage; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Boolean getBilled() { return billed; }
    public void setBilled(Boolean billed) { this.billed = billed; }
}
