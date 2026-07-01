package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_rl_policy_update")
public class PlatformRlPolicyUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_id", nullable = false)
    @NotNull
    private Long policyId;

    @Column(name = "action_taken", nullable = false)
    @NotNull
    @Size(max = 150)
    private String actionTaken;

    @Column(nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal reward;

    @Column(name = "state_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String stateJson;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public String getActionTaken() { return actionTaken; }
    public void setActionTaken(String actionTaken) { this.actionTaken = actionTaken; }
    public BigDecimal getReward() { return reward; }
    public void setReward(BigDecimal reward) { this.reward = reward; }
    public String getStateJson() { return stateJson; }
    public void setStateJson(String stateJson) { this.stateJson = stateJson; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}