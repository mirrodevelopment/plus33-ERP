package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_policy_versions",
    uniqueConstraints = @UniqueConstraint(columnNames = {"policy_id", "version_number"}))
public class PolicyVersion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "policy_id", nullable = false) private Long policyId;
    @Column(name = "version_number", nullable = false) private Integer versionNumber;
    @Column(name = "content_hash", nullable = false, length = 64) private String contentHash;
    @Column(name = "approved_at") private LocalDateTime approvedAt;
    @Column(name = "published_at") private LocalDateTime publishedAt;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getPolicyId() { return policyId; } public void setPolicyId(Long v) { this.policyId = v; }
    public Integer getVersionNumber() { return versionNumber; } public void setVersionNumber(Integer v) { this.versionNumber = v; }
    public String getContentHash() { return contentHash; } public void setContentHash(String v) { this.contentHash = v; }
    public LocalDateTime getApprovedAt() { return approvedAt; } public void setApprovedAt(LocalDateTime v) { this.approvedAt = v; }
    public LocalDateTime getPublishedAt() { return publishedAt; } public void setPublishedAt(LocalDateTime v) { this.publishedAt = v; }
}
