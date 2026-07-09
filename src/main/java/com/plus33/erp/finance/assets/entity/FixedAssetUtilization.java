/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetUtilization.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetUtilizationController
 * Related Service   : FixedAssetUtilizationService, FixedAssetUtilizationServiceImpl
 * Related Repository: FixedAssetUtilizationRepository
 * Related Entity    : FixedAssetUtilization
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetUtilizationMapper
 * Related DB Table  : fixed_asset_utilization
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetUtilizationRepository, FixedAssetUtilizationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_utilization'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetUtilization}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_utilization'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_utilization}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}