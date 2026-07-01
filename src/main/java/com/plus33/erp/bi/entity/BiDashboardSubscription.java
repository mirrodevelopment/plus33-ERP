package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_dashboard_subscription")
public class BiDashboardSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dashboard_code", nullable = false)
    private String dashboardCode;
    @Column(name = "subscriber_user", nullable = false)
    private String subscriberUser;
    @Column(name = "schedule_cron", nullable = false)
    private String scheduleCron;
    @Column(name = "export_format", nullable = false)
    private String exportFormat = "PDF";
    @Column(nullable = false)
    private String status = "ACTIVE";
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "last_triggered_at")
    private LocalDateTime lastTriggeredAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDashboardCode() { return dashboardCode; }
    public void setDashboardCode(String dashboardCode) { this.dashboardCode = dashboardCode; }
    public String getSubscriberUser() { return subscriberUser; }
    public void setSubscriberUser(String subscriberUser) { this.subscriberUser = subscriberUser; }
    public String getScheduleCron() { return scheduleCron; }
    public void setScheduleCron(String scheduleCron) { this.scheduleCron = scheduleCron; }
    public String getExportFormat() { return exportFormat; }
    public void setExportFormat(String exportFormat) { this.exportFormat = exportFormat; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getLastTriggeredAt() { return lastTriggeredAt; }
    public void setLastTriggeredAt(LocalDateTime lastTriggeredAt) { this.lastTriggeredAt = lastTriggeredAt; }
}