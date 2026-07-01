package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_policy_history")
public class PlatformPolicyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String policyCode;

    @Column(name = "previous_version")
    @Size(max = 50)
    private String previousVersion;

    @Column(name = "new_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String newVersion;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "changed_at", nullable = false)
    @NotNull
    private LocalDateTime changedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPolicyCode() { return policyCode; }
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    public String getPreviousVersion() { return previousVersion; }
    public void setPreviousVersion(String previousVersion) { this.previousVersion = previousVersion; }
    public String getNewVersion() { return newVersion; }
    public void setNewVersion(String newVersion) { this.newVersion = newVersion; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}