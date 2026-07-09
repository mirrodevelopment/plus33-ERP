/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetSnapshotLine.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetSnapshotLineController
 * Related Service   : BudgetSnapshotLineService, BudgetSnapshotLineServiceImpl
 * Related Repository: BudgetSnapshotLineRepository
 * Related Entity    : BudgetSnapshotLine
 * Related DTO       : N/A
 * Related Mapper    : BudgetSnapshotLineMapper
 * Related DB Table  : budget_snapshot_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetSnapshotLineRepository, BudgetSnapshotLineMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budget_snapshot_lines'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import com.plus33.erp.finance.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetSnapshotLine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budget_snapshot_lines'.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_snapshot_lines}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "budget_snapshot_lines")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetSnapshotLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snapshot_id", nullable = false)
    private BudgetSnapshot snapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dimension_set_id", nullable = false)
    private BudgetDimensionSet dimensionSet;

    @Column(name = "period_start_date", nullable = false)
    private LocalDate periodStartDate;

    @Column(name = "period_end_date", nullable = false)
    private LocalDate periodEndDate;

    @Column(name = "allocated_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal allocatedAmount;

    @Column(name = "reserved_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal reservedAmount;

    @Column(name = "consumed_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal consumedAmount;
}