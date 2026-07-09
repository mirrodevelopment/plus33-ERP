/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.entity
 * File              : PeriodLockOverride.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PeriodLockOverrideController
 * Related Service   : PeriodLockOverrideService, PeriodLockOverrideServiceImpl
 * Related Repository: PeriodLockOverrideRepository
 * Related Entity    : PeriodLockOverride
 * Related DTO       : N/A
 * Related Mapper    : PeriodLockOverrideMapper
 * Related DB Table  : period_lock_overrides
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : PeriodLockOverrideRepository, PeriodLockOverrideMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'period_lock_overrides'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.finance.entity.JournalEntry;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PeriodLockOverride}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'period_lock_overrides'.</p>
 *
 * <p><b>Database Table   :</b> {@code period_lock_overrides}</p>
 * <p><b>Module Deps      :</b> Organization, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "period_lock_overrides")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodLockOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "user_email", nullable = false, length = 100)
    private String userEmail;

    @Column(name = "override_at", nullable = false)
    @Builder.Default
    private LocalDateTime overrideAt = LocalDateTime.now();

    @Column(name = "original_lock_date", nullable = false)
    private LocalDate originalLockDate;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id")
    private JournalEntry journalEntry;

    @Column(length = 255)
    private String reason;
}