package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_sod_violations")
public class SodViolation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "sod_rule_id", nullable = false) private Long sodRuleId;
    @Column(name = "user_id", nullable = false) private Long userId;
    @Column(name = "detected_at", nullable = false) private LocalDateTime detectedAt = LocalDateTime.now();
    @Column(nullable = false, length = 20) private String status = "OPEN";

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getSodRuleId() { return sodRuleId; } public void setSodRuleId(Long v) { this.sodRuleId = v; }
    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    public LocalDateTime getDetectedAt() { return detectedAt; } public void setDetectedAt(LocalDateTime v) { this.detectedAt = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
}
