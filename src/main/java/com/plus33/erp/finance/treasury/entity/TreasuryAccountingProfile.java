package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
