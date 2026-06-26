package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fixed_asset_maintenance_plans")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetMaintenancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Column(name = "maintenance_type", nullable = false, length = 50)
    private String maintenanceType; // e.g. PREVENTIVE, CORRECTIVE

    @Column(name = "service_interval_days", nullable = false)
    private Integer serviceIntervalDays;

    @Column(name = "next_maintenance_date", nullable = false)
    private LocalDate nextMaintenanceDate;

    @Column(name = "maintenance_vendor", length = 100)
    private String maintenanceVendor;

    @Builder.Default
    @Column(name = "estimated_cost", precision = 15, scale = 2)
    private BigDecimal estimatedCost = BigDecimal.ZERO;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
