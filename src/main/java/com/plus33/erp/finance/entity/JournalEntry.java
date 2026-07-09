/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.entity
 * File              : JournalEntry.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JournalEntryController
 * Related Service   : JournalEntryService, JournalEntryServiceImpl
 * Related Repository: JournalEntryRepository
 * Related Entity    : JournalEntry
 * Related DTO       : N/A
 * Related Mapper    : JournalEntryMapper
 * Related DB Table  : journal_entries
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : JournalEntryRepository, JournalEntryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'journal_entries'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "journal_entries", uniqueConstraints = {
    @UniqueConstraint(name = "uk_journal_company_entry", columnNames = {"company_id", "entry_number"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code JournalEntry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'journal_entries'.</p>
 *
 * <p><b>Database Table   :</b> {@code journal_entries}</p>
 * <p><b>Module Deps      :</b> Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry_number", nullable = false, length = 50)
    private String entryNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "source_module", nullable = false, length = 50)
    private String sourceModule;

    @Column(name = "source_reference", length = 100)
    private String sourceReference;

    @Builder.Default
    @Column(nullable = false, length = 30)
    private String status = "DRAFT";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reversal_entry_id")
    private JournalEntry reversalEntry;

    @Builder.Default
    @Column(name = "closing_entry", nullable = false)
    private Boolean closingEntry = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "closing_type", length = 30)
    private ClosingEntryType closingType;

    @Builder.Default
    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode = "AED";

    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<JournalEntryLine> lines = new ArrayList<>();

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}