package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_analytics_role")
public class BiAnalyticsRole {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "role_code", nullable = false, unique = true, length = 100) private String roleCode;
    @Column(name = "role_name", nullable = false, length = 200) private String roleName;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(name = "is_active", nullable = false) private Boolean isActive = true;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getRoleCode() { return roleCode; } public void setRoleCode(String v) { this.roleCode = v; }
    public String getRoleName() { return roleName; } public void setRoleName(String v) { this.roleName = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean v) { this.isActive = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
