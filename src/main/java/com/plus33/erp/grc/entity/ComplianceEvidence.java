package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_compliance_evidence")
public class ComplianceEvidence {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "reference_type", nullable = false, length = 50) private String referenceType;
    @Column(name = "reference_id", nullable = false) private Long referenceId;
    @Column(name = "file_name", nullable = false, length = 255) private String fileName;
    @Column(name = "content_hash", nullable = false, unique = true, length = 64) private String contentHash;
    @Column(name = "evidence_source", length = 100) private String evidenceSource;
    @Column(name = "uploaded_by_id", nullable = false) private Long uploadedById;
    @Column(name = "verified_by_id") private Long verifiedById;
    @Column(name = "verification_date") private LocalDate verificationDate;
    @Column(name = "retention_policy", nullable = false, length = 50) private String retentionPolicy = "7_YEARS";
    @Column(name = "uploaded_at", nullable = false, updatable = false) private LocalDateTime uploadedAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getReferenceType() { return referenceType; } public void setReferenceType(String v) { this.referenceType = v; }
    public Long getReferenceId() { return referenceId; } public void setReferenceId(Long v) { this.referenceId = v; }
    public String getFileName() { return fileName; } public void setFileName(String v) { this.fileName = v; }
    public String getContentHash() { return contentHash; } public void setContentHash(String v) { this.contentHash = v; }
    public String getEvidenceSource() { return evidenceSource; } public void setEvidenceSource(String v) { this.evidenceSource = v; }
    public Long getUploadedById() { return uploadedById; } public void setUploadedById(Long v) { this.uploadedById = v; }
    public Long getVerifiedById() { return verifiedById; } public void setVerifiedById(Long v) { this.verifiedById = v; }
    public LocalDate getVerificationDate() { return verificationDate; } public void setVerificationDate(LocalDate v) { this.verificationDate = v; }
    public String getRetentionPolicy() { return retentionPolicy; } public void setRetentionPolicy(String v) { this.retentionPolicy = v; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
}
