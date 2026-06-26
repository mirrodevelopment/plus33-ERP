package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fixed_asset_downtimes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetDowntime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(nullable = false, length = 255)
    private String reason;

    @Column(name = "responsible_department", nullable = false, length = 100)
    private String responsibleDepartment;

    @Builder.Default
    @Column(name = "lost_production_hours", precision = 10, scale = 2)
    private BigDecimal lostProductionHours = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "lost_revenue", precision = 15, scale = 2)
    private BigDecimal lostRevenue = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
