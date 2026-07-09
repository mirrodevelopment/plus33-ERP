/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : BankStatement.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankStatementController
 * Related Service   : BankStatementService, BankStatementServiceImpl
 * Related Repository: BankStatementRepository
 * Related Entity    : BankStatement
 * Related DTO       : N/A
 * Related Mapper    : BankStatementMapper
 * Related DB Table  : bank_statements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BankStatementRepository, BankStatementMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bank_statements'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bank_statements", uniqueConstraints = {
    @UniqueConstraint(name = "uk_statements_num", columnNames = {"bank_account_id", "statement_number"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankStatement}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bank_statements'.</p>
 *
 * <p><b>Database Table   :</b> {@code bank_statements}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "statement_number", nullable = false, length = 100)
    private String statementNumber;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "opening_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal openingBalance;

    @Column(name = "closing_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal closingBalance;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "DRAFT"; // DRAFT, RECONCILING, RECONCILED

    @Column(name = "imported_at", nullable = false, updatable = false)
    private LocalDateTime importedAt;

    @OneToMany(mappedBy = "statement", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BankStatementLine> lines = new ArrayList<>();

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        importedAt = LocalDateTime.now();
    }
}