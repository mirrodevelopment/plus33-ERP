package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_policies")
public class EnterprisePolicy {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "policy_code", nullable = false, unique = true, length = 50) private String policyCode;
    @Column(nullable = false, length = 200) private String title;
    @Column(length = 100) private String category;
    @Column(nullable = false, length = 30) private String status = "DRAFT";
    @Column(name = "owner_id") private Long ownerId;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getPolicyCode() { return policyCode; } public void setPolicyCode(String v) { this.policyCode = v; }
    public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
    public String getCategory() { return category; } public void setCategory(String v) { this.category = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public Long getOwnerId() { return ownerId; } public void setOwnerId(Long v) { this.ownerId = v; }
}
