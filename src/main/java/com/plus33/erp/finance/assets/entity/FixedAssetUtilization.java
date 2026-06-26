package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fixed_asset_utilization")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetUtilization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Column(name = "reading_date", nullable = false)
    private LocalDate readingDate;

    @Builder.Default
    @Column(name = "usage_hours", precision = 10, scale = 2)
    private BigDecimal usageHours = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "production_count")
    private Integer productionCount = 0;

    @Builder.Default
    @Column(name = "mileage", precision = 12, scale = 2)
    private BigDecimal mileage = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "runtime_seconds")
    private Long runtimeSeconds = 0L;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
