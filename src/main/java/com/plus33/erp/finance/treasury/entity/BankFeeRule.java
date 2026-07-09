/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : BankFeeRule.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankFeeRuleController
 * Related Service   : BankFeeRuleService, BankFeeRuleServiceImpl
 * Related Repository: BankFeeRuleRepository
 * Related Entity    : BankFeeRule
 * Related DTO       : N/A
 * Related Mapper    : BankFeeRuleMapper
 * Related DB Table  : bank_fee_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BankFeeRuleRepository, BankFeeRuleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bank_fee_rules'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.finance.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankFeeRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bank_fee_rules'.</p>
 *
 * <p><b>Database Table   :</b> {@code bank_fee_rules}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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