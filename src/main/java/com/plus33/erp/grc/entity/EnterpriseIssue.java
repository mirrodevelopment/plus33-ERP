package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_enterprise_issues")
public class EnterpriseIssue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "issue_number", nullable = false, unique = true, length = 50) private String issueNumber;
    @Column(nullable = false, length = 200) private String title;
    @Column(nullable = false, length = 50) private String source;
    @Column(nullable = false, length = 30) private String status = "OPEN";
    @Column(nullable = false, length = 20) private String severity = "MEDIUM";
    @Column(name = "owner_id") private Long ownerId;
    @Column(name = "due_date") private LocalDate dueDate;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getIssueNumber() { return issueNumber; } public void setIssueNumber(String v) { this.issueNumber = v; }
    public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
    public String getSource() { return source; } public void setSource(String v) { this.source = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public String getSeverity() { return severity; } public void setSeverity(String v) { this.severity = v; }
    public Long getOwnerId() { return ownerId; } public void setOwnerId(Long v) { this.ownerId = v; }
    public LocalDate getDueDate() { return dueDate; } public void setDueDate(LocalDate v) { this.dueDate = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
