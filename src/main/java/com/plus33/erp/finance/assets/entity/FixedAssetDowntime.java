/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetDowntime.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetDowntimeController
 * Related Service   : FixedAssetDowntimeService, FixedAssetDowntimeServiceImpl
 * Related Repository: FixedAssetDowntimeRepository
 * Related Entity    : FixedAssetDowntime
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetDowntimeMapper
 * Related DB Table  : fixed_asset_downtimes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetDowntimeRepository, FixedAssetDowntimeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_downtimes'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetDowntime}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_downtimes'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_downtimes}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}