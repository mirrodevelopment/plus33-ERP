/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : CashPositionSnapshotLine.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CashPositionSnapshotLineController
 * Related Service   : CashPositionSnapshotLineService, CashPositionSnapshotLineServiceImpl
 * Related Repository: CashPositionSnapshotLineRepository
 * Related Entity    : CashPositionSnapshotLine
 * Related DTO       : N/A
 * Related Mapper    : CashPositionSnapshotLineMapper
 * Related DB Table  : cash_position_snapshot_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CashPositionSnapshotLineRepository, CashPositionSnapshotLineMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'cash_position_snapshot_lines'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CashPositionSnapshotLine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'cash_position_snapshot_lines'.</p>
 *
 * <p><b>Database Table   :</b> {@code cash_position_snapshot_lines}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "cash_position_snapshot_lines")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashPositionSnapshotLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snapshot_id", nullable = false)
    private CashPositionSnapshot snapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "current_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "reconciled_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal reconciledBalance;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;
}