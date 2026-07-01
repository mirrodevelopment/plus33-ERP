package com.plus33.erp.grc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grc_audit_findings")
public class AuditFinding {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "engagement_id", nullable = false) private Long engagementId;
    @Column(name = "finding_number", nullable = false, unique = true, length = 50) private String findingNumber;
    @Column(nullable = false, length = 200) private String title;
    @Column(nullable = false, length = 20) private String severity = "MEDIUM";
    @Column(nullable = false, length = 30) private String status = "OPEN";
    @Column(columnDefinition = "TEXT") private String description;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getEngagementId() { return engagementId; } public void setEngagementId(Long v) { this.engagementId = v; }
    public String getFindingNumber() { return findingNumber; } public void setFindingNumber(String v) { this.findingNumber = v; }
    public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
    public String getSeverity() { return severity; } public void setSeverity(String v) { this.severity = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
}
