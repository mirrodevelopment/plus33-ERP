package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_policy_audit")
public class PlatformPolicyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String policyCode;

    @Column(name = "user_identity", nullable = false)
    @NotNull
    @Size(max = 100)
    private String userIdentity;

    @Column(nullable = false)
    @NotNull
    @Size(max = 150)
    private String action;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String decision = "DENY";

    @Column(name = "evaluated_at", nullable = false)
    @NotNull
    private LocalDateTime evaluatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPolicyCode() { return policyCode; }
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    public String getUserIdentity() { return userIdentity; }
    public void setUserIdentity(String userIdentity) { this.userIdentity = userIdentity; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public LocalDateTime getEvaluatedAt() { return evaluatedAt; }
    public void setEvaluatedAt(LocalDateTime evaluatedAt) { this.evaluatedAt = evaluatedAt; }
}