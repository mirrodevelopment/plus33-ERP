package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_esg_compliance_log")
public class PlatformEsgComplianceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String framework; // GRI, SASB, CSRD

    @Column(name = "compliance_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal complianceScore;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status; // COMPLIANT, WARNING, NON_COMPLIANT

    @Column(name = "finding_summary", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String findingSummary;

    @Column(name = "corrective_action", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String correctiveAction;

    @Column(name = "owner_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String ownerName;

    @Column(name = "next_review_date", nullable = false)
    @NotNull
    private LocalDateTime nextReviewDate;

    @Column(name = "audited_at", nullable = false)
    @NotNull
    private LocalDateTime auditedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFramework() { return framework; }
    public void setFramework(String framework) { this.framework = framework; }
    public BigDecimal getComplianceScore() { return complianceScore; }
    public void setComplianceScore(BigDecimal complianceScore) { this.complianceScore = complianceScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFindingSummary() { return findingSummary; }
    public void setFindingSummary(String findingSummary) { this.findingSummary = findingSummary; }
    public String getCorrectiveAction() { return correctiveAction; }
    public void setCorrectiveAction(String correctiveAction) { this.correctiveAction = correctiveAction; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public LocalDateTime getNextReviewDate() { return nextReviewDate; }
    public void setNextReviewDate(LocalDateTime nextReviewDate) { this.nextReviewDate = nextReviewDate; }
    public LocalDateTime getAuditedAt() { return auditedAt; }
    public void setAuditedAt(LocalDateTime auditedAt) { this.auditedAt = auditedAt; }
}