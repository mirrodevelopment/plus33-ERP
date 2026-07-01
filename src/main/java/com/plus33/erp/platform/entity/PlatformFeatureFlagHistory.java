package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_feature_flag_history")
public class PlatformFeatureFlagHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flag_key", nullable = false)
    @NotNull
    @Size(max = 100)
    private String flagKey;

    @Column(name = "previous_value")
    @Size(max = 50)
    private String previousValue;

    @Column(name = "new_value", nullable = false)
    @NotNull
    @Size(max = 50)
    private String newValue;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(length = 500)
    @Size(max = 500)
    private String reason;

    @Column(name = "rollout_percentage", nullable = false)
    @NotNull
    private Integer rolloutPercentage = 0;

    @Column(name = "changed_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime changedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFlagKey() { return flagKey; }
    public void setFlagKey(String flagKey) { this.flagKey = flagKey; }
    public String getPreviousValue() { return previousValue; }
    public void setPreviousValue(String previousValue) { this.previousValue = previousValue; }
    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Integer getRolloutPercentage() { return rolloutPercentage; }
    public void setRolloutPercentage(Integer rolloutPercentage) { this.rolloutPercentage = rolloutPercentage; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}