/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : BankStatementLine.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankStatementLineController
 * Related Service   : BankStatementLineService, BankStatementLineServiceImpl
 * Related Repository: BankStatementLineRepository
 * Related Entity    : BankStatementLine
 * Related DTO       : N/A
 * Related Mapper    : BankStatementLineMapper
 * Related DB Table  : bank_statement_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BankStatementLineRepository, BankStatementLineMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bank_statement_lines'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.finance.entity.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankStatementLine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bank_statement_lines'.</p>
 *
 * <p><b>Database Table   :</b> {@code bank_statement_lines}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "bank_statement_lines")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankStatementLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statement_id", nullable = false)
    private BankStatement statement;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "value_date")
    private LocalDate valueDate;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(length = 100)
    private String reference;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    @Builder.Default
    private Boolean reconciled = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "reconciled_at")
    private LocalDateTime reconciledAt;
}