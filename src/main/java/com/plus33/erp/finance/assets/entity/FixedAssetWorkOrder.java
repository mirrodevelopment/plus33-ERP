package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fixed_asset_work_orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetWorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Column(name = "work_order_number", nullable = false, length = 50)
    private String workOrderNumber;

    @Column(name = "technician_name", length = 100)
    private String technicianName;

    @Column(name = "vendor_name", length = 100)
    private String vendorName;

    @Builder.Default
    @Column(name = "labor_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal laborCost = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "parts_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal partsCost = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "downtime_hours", precision = 10, scale = 2)
    private BigDecimal downtimeHours = BigDecimal.ZERO;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WorkOrderStatus status = WorkOrderStatus.DRAFT;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "completion_remarks", columnDefinition = "TEXT")
    private String completionRemarks;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
