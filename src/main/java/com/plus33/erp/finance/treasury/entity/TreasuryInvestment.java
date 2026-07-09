/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : TreasuryInvestment.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryInvestmentController
 * Related Service   : TreasuryInvestmentService, TreasuryInvestmentServiceImpl
 * Related Repository: TreasuryInvestmentRepository
 * Related Entity    : TreasuryInvestment
 * Related DTO       : N/A
 * Related Mapper    : TreasuryInvestmentMapper
 * Related DB Table  : treasury_investments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryInvestmentRepository, TreasuryInvestmentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'treasury_investments'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryInvestment}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'treasury_investments'.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_investments}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "treasury_investments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreasuryInvestment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "reference_number", nullable = false, unique = true, length = 100)
    private String referenceNumber;

    @Column(name = "investment_type", nullable = false, length = 50)
    private String investmentType; // FIXED_DEPOSIT, MONEY_MARKET_FUND, TREASURY_BILL, COMMERCIAL_PAPER, BOND

    @Column(name = "principal_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal principalAmount;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "expected_yield", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal expectedYield = BigDecimal.ZERO;

    @Column(name = "effective_yield", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal effectiveYield = BigDecimal.ZERO;

    @Column(name = "compounded_interest", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal compoundedInterest = BigDecimal.ZERO;

    @Column(name = "tax_withholding", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal taxWithholding = BigDecimal.ZERO;

    @Column(name = "early_liquidation_penalty", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal earlyLiquidationPenalty = BigDecimal.ZERO;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "maturity_date", nullable = false)
    private LocalDate maturityDate;

    @Column(name = "interest_earned", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal interestEarned = BigDecimal.ZERO;

    @Column(name = "accrued_interest", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal accruedInterest = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, MATURED, LIQUIDATED

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