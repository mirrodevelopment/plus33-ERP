/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.entity
 * File              : DashboardConfig.java
 * Purpose           : JPA Entity representing a persistent database record in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DashboardConfigController
 * Related Service   : DashboardConfigService, DashboardConfigServiceImpl
 * Related Repository: DashboardConfigRepository
 * Related Entity    : DashboardConfig
 * Related DTO       : N/A
 * Related Mapper    : DashboardConfigMapper
 * Related DB Table  : dashboard_configs
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : DashboardConfigRepository, DashboardConfigMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'dashboard_configs'. Defines persistent domain object for Analytics Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
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
/**
 * <b>PLUS33 Coffee ERP -- Analytics Module</b>
 *
 * <p><b>Class  :</b> {@code DashboardConfig}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.analytics.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'dashboard_configs'.</p>
 *
 * <p><b>Database Table   :</b> {@code dashboard_configs}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Builder.Default
    @Column(name = "refresh_interval_minutes", nullable = false)
    private Integer refreshIntervalMinutes = 15;

    @Column(name = "last_refreshed_at")
    private LocalDateTime lastRefreshedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}