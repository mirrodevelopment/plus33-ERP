package com.plus33.erp.analytics.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "dashboard_configs", uniqueConstraints = {
    @UniqueConstraint(name = "uk_dashboard_company_role", columnNames = {"company_id", "dashboard_code", "role_code"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "dashboard_code", nullable = false, length = 100)
    private String dashboardCode;

    @Column(name = "dashboard_name", nullable = false, length = 150)
    private String dashboardName;

    @Column(name = "role_code", nullable = false, length = 100)
    private String roleCode;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "refresh_interval_minutes", nullable = false)
    private Integer refreshIntervalMinutes = 15;

    @Column(name = "last_refreshed_at")
    private LocalDateTime lastRefreshedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
