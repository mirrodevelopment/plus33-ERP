/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetDepreciationLog.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetDepreciationLogController
 * Related Service   : FixedAssetDepreciationLogService, FixedAssetDepreciationLogServiceImpl
 * Related Repository: FixedAssetDepreciationLogRepository
 * Related Entity    : FixedAssetDepreciationLog
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetDepreciationLogMapper
 * Related DB Table  : fixed_asset_depreciation_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetDepreciationLogRepository, FixedAssetDepreciationLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_depreciation_logs'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import com.plus33.erp.finance.entity.JournalEntry;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetDepreciationLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_depreciation_logs'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_depreciation_logs}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "fixed_asset_depreciation_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetDepreciationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Column(name = "depreciation_date", nullable = false)
    private LocalDate depreciationDate;

    @Column(name = "depreciation_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal depreciationAmount;

    @Column(name = "book_value_before", nullable = false, precision = 15, scale = 2)
    private BigDecimal bookValueBefore;

    @Column(name = "book_value_after", nullable = false, precision = 15, scale = 2)
    private BigDecimal bookValueAfter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry;

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