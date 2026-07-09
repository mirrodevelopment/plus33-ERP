/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : TreasuryAccountingProfile.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryAccountingProfileController
 * Related Service   : TreasuryAccountingProfileService, TreasuryAccountingProfileServiceImpl
 * Related Repository: TreasuryAccountingProfileRepository
 * Related Entity    : TreasuryAccountingProfile
 * Related DTO       : N/A
 * Related Mapper    : TreasuryAccountingProfileMapper
 * Related DB Table  : treasury_accounting_profiles
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TreasuryAccountingProfileRepository, TreasuryAccountingProfileMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'treasury_accounting_profiles'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryAccountingProfile}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'treasury_accounting_profiles'.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_accounting_profiles}</p>
 * <p><b>Module Deps      :</b> Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "treasury_accounting_profiles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreasuryAccountingProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "profile_code", nullable = false, length = 50)
    private String profileCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_account_id", nullable = false)
    private Account cashAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_fee_account_id", nullable = false)
    private Account bankFeeAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fx_gain_account_id", nullable = false)
    private Account fxGainAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fx_loss_account_id", nullable = false)
    private Account fxLossAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_income_account_id", nullable = false)
    private Account interestIncomeAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_expense_account_id", nullable = false)
    private Account interestExpenseAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hedge_reserve_account_id", nullable = false)
    private Account hedgeReserveAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investment_account_id", nullable = false)
    private Account investmentAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overdraft_account_id", nullable = false)
    private Account overdraftAccount;

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