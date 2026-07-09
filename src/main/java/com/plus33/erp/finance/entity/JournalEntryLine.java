/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.entity
 * File              : JournalEntryLine.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JournalEntryLineController
 * Related Service   : JournalEntryLineService, JournalEntryLineServiceImpl
 * Related Repository: JournalEntryLineRepository
 * Related Entity    : JournalEntryLine
 * Related DTO       : N/A
 * Related Mapper    : JournalEntryLineMapper
 * Related DB Table  : journal_entry_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : JournalEntryLineRepository, JournalEntryLineMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'journal_entry_lines'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code JournalEntryLine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'journal_entry_lines'.</p>
 *
 * <p><b>Database Table   :</b> {@code journal_entry_lines}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "journal_entry_lines")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntryLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dimension_set_id")
    private BudgetDimensionSet dimensionSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Builder.Default
    @Column(name = "debit_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "credit_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal creditAmount = BigDecimal.ZERO;
}