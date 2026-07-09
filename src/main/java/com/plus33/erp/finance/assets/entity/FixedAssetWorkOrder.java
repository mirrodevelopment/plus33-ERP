/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetWorkOrder.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetWorkOrderController
 * Related Service   : FixedAssetWorkOrderService, FixedAssetWorkOrderServiceImpl
 * Related Repository: FixedAssetWorkOrderRepository
 * Related Entity    : FixedAssetWorkOrder
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetWorkOrderMapper
 * Related DB Table  : fixed_asset_work_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetWorkOrderRepository, FixedAssetWorkOrderMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_work_orders'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetWorkOrder}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_work_orders'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_work_orders}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}