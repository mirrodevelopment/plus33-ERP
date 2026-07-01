package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_usage_log")
public class BiUsageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "dashboard_code", nullable = false)
    private String dashboardCode;
    @Column(name = "action_type", nullable = false)
    private String actionType;
    @Column(name = "duration_ms", nullable = false)
    private Long durationMs;
    @Column(name = "company_id", nullable = false)
    private Long companyId;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "accessed_at", nullable = false, updatable = false)
    private LocalDateTime accessedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getDashboardCode() { return dashboardCode; }
    public void setDashboardCode(String dashboardCode) { this.dashboardCode = dashboardCode; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public LocalDateTime getAccessedAt() { return accessedAt; }
    public void setAccessedAt(LocalDateTime accessedAt) { this.accessedAt = accessedAt; }
}