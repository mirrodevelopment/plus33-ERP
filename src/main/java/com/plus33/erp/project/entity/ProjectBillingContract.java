package com.plus33.erp.project.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "project_billing_contracts")
public class ProjectBillingContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false, unique = true)
    private Long projectId;

    @Column(name = "contract_type", nullable = false, length = 30)
    private String contractType = "TIME_AND_MATERIAL";

    @Column(name = "billing_amount", nullable = false)
    private BigDecimal billingAmount = BigDecimal.ZERO;

    @Column(name = "recognized_revenue", nullable = false)
    private BigDecimal recognizedRevenue = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    public BigDecimal getBillingAmount() { return billingAmount; }
    public void setBillingAmount(BigDecimal billingAmount) { this.billingAmount = billingAmount; }
    public BigDecimal getRecognizedRevenue() { return recognizedRevenue; }
    public void setRecognizedRevenue(BigDecimal recognizedRevenue) { this.recognizedRevenue = recognizedRevenue; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
