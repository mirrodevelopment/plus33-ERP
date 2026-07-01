package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_dashboard_share")
public class BiDashboardShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dashboard_code", nullable = false)
    private String dashboardCode;
    @Column(name = "shared_by", nullable = false)
    private String sharedBy;
    @Column(name = "recipient_user", nullable = false)
    private String recipientUser;
    @Column(name = "can_edit", nullable = false)
    private Boolean canEdit = false;
    @Column(name = "shared_at", nullable = false)
    private LocalDateTime sharedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDashboardCode() { return dashboardCode; }
    public void setDashboardCode(String dashboardCode) { this.dashboardCode = dashboardCode; }
    public String getSharedBy() { return sharedBy; }
    public void setSharedBy(String sharedBy) { this.sharedBy = sharedBy; }
    public String getRecipientUser() { return recipientUser; }
    public void setRecipientUser(String recipientUser) { this.recipientUser = recipientUser; }
    public Boolean getCanEdit() { return canEdit; }
    public void setCanEdit(Boolean canEdit) { this.canEdit = canEdit; }
    public LocalDateTime getSharedAt() { return sharedAt; }
    public void setSharedAt(LocalDateTime sharedAt) { this.sharedAt = sharedAt; }
}