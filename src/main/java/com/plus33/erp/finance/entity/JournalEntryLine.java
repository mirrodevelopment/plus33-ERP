package com.plus33.erp.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;

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
