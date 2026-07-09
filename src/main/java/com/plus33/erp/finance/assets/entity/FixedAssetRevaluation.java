/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetRevaluation.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetRevaluationController
 * Related Service   : FixedAssetRevaluationService, FixedAssetRevaluationServiceImpl
 * Related Repository: FixedAssetRevaluationRepository
 * Related Entity    : FixedAssetRevaluation
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetRevaluationMapper
 * Related DB Table  : fixed_asset_revaluations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetRevaluationRepository, FixedAssetRevaluationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_revaluations'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.JournalEntry;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetRevaluation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_revaluations'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_revaluations}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "fixed_asset_revaluations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetRevaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Column(name = "revaluation_date", nullable = false)
    private LocalDate revaluationDate;

    @Column(name = "previous_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal previousValue;

    @Column(name = "new_fair_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal newFairValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revaluation_reserve_account_id", nullable = false)
    private Account revaluationReserveAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id")
    private JournalEntry journalEntry;

    @Column(length = 255)
    private String reason;

    @Column(name = "performed_by", nullable = false, length = 100)
    private String performedBy;

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