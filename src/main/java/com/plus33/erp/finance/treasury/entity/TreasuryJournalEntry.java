/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : TreasuryJournalEntry.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryJournalEntryController
 * Related Service   : TreasuryJournalEntryService, TreasuryJournalEntryServiceImpl
 * Related Repository: TreasuryJournalEntryRepository
 * Related Entity    : TreasuryJournalEntry
 * Related DTO       : N/A
 * Related Mapper    : TreasuryJournalEntryMapper
 * Related DB Table  : treasury_journal_entries
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TreasuryJournalEntryRepository, TreasuryJournalEntryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'treasury_journal_entries'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Immutable double-entry journal line for treasury GL postings.
 * Each treasury event (FX deal, investment, transfer, interest accrual, etc.)
 * produces two or more complementary lines that must balance to zero.
 */
@Getter
@Entity
@Table(name = "treasury_journal_entries", indexes = {
    @Index(name = "idx_tje_reference", columnList = "reference_number"),
    @Index(name = "idx_tje_event", columnList = "event_type, event_id"),
    @Index(name = "idx_tje_account", columnList = "account_id, posting_date"),
    @Index(name = "idx_tje_company_date", columnList = "company_id, posting_date")
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryJournalEntry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'treasury_journal_entries'.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_journal_entries}</p>
 * <p><b>Module Deps      :</b> Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TreasuryJournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    /** Source event type — e.g. CASH_TRANSFER, FX_DEAL, INTEREST_ACCRUAL, INVESTMENT_PURCHASE */
    @Column(name = "event_type", nullable = false, length = 60)
    private String eventType;

    /** FK to the source event (cash_transfer_id, fx_deal_id, etc.) */
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    /** Idempotency key to prevent duplicate postings */
    @Column(name = "reference_number", nullable = false, unique = true, length = 120)
    private String referenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false, length = 6)
    private EntryType entryType; // DEBIT or CREDIT

    @Column(name = "amount", nullable = false, precision = 20, scale = 6)
    private BigDecimal amount;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    /** Amount converted to company base currency */
    @Column(name = "base_currency_amount", nullable = false, precision = 20, scale = 6)
    private BigDecimal baseCurrencyAmount;

    @Column(name = "base_currency_code", nullable = false, length = 3)
    private String baseCurrencyCode;

    @Column(name = "exchange_rate", nullable = false, precision = 18, scale = 6)
    @Builder.Default
    private BigDecimal exchangeRate = BigDecimal.ONE;

    @Column(name = "posting_date", nullable = false)
    private LocalDate postingDate;

    @Column(name = "value_date", nullable = false)
    private LocalDate valueDate;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "posted_by", nullable = false, length = 100)
    private String postedBy;

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

    public enum EntryType {
        DEBIT, CREDIT
    }
}