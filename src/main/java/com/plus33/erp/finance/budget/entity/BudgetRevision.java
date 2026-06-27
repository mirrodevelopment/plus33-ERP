package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "budget_revisions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_line_id", nullable = false)
    private BudgetLine budgetLine;

    @Column(name = "revision_date", nullable = false)
    private LocalDate revisionDate;

    @Column(name = "previous_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal previousAmount;

    @Column(name = "new_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal newAmount;

    @Column(name = "change_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal changeAmount;

    @Column(length = 255)
    private String reason;

    @Column(name = "performed_by", nullable = false, length = 100)
    private String performedBy;

    @Builder.Default
    @Column(nullable = false, length = 30)
    private String status = "APPROVED"; // REQUESTED, APPROVED, REJECTED
}
