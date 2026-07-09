/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : CashTransfer.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CashTransferController
 * Related Service   : CashTransferService, CashTransferServiceImpl
 * Related Repository: CashTransferRepository
 * Related Entity    : CashTransfer
 * Related DTO       : N/A
 * Related Mapper    : CashTransferMapper
 * Related DB Table  : cash_transfers
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : CashTransferRepository, CashTransferMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'cash_transfers'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CashTransfer}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'cash_transfers'.</p>
 *
 * <p><b>Database Table   :</b> {@code cash_transfers}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "cash_transfers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_bank_account_id", nullable = false)
    private BankAccount sourceBankAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_bank_account_id", nullable = false)
    private BankAccount destinationBankAccount;

    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "exchange_rate", nullable = false, precision = 18, scale = 6)
    @Builder.Default
    private BigDecimal exchangeRate = BigDecimal.ONE;

    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal fees = BigDecimal.ZERO;

    @Column(name = "reference_number", nullable = false, length = 100)
    private String referenceNumber;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "DRAFT"; // DRAFT, PENDING_APPROVAL, APPROVED, COMPLETED, CANCELLED

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

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
}