/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetRevision.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetRevisionController
 * Related Service   : BudgetRevisionService, BudgetRevisionServiceImpl
 * Related Repository: BudgetRevisionRepository
 * Related Entity    : BudgetRevision
 * Related DTO       : N/A
 * Related Mapper    : BudgetRevisionMapper
 * Related DB Table  : budget_revisions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetRevisionRepository, BudgetRevisionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budget_revisions'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetRevision}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budget_revisions'.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_revisions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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