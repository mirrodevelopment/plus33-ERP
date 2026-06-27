package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.finance.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "bank_fee_rules")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankFeeRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "charge_type", nullable = false, length = 30)
    private String chargeType; // MONTHLY_MAINTENANCE, TRANSACTION_PERCENT, TRANSACTION_FIXED, OUTWARD_TRANSFER

    @Column(name = "rate_percent", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal ratePercent = BigDecimal.ZERO;

    @Column(name = "fixed_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal fixedAmount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gl_expense_account_id", nullable = false)
    private Account glExpenseAccount;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
