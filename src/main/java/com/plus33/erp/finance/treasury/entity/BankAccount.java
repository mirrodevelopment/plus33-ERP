/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : BankAccount.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankAccountController
 * Related Service   : BankAccountService, BankAccountServiceImpl
 * Related Repository: BankAccountRepository
 * Related Entity    : BankAccount
 * Related DTO       : N/A
 * Related Mapper    : BankAccountMapper
 * Related DB Table  : bank_accounts
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : BankAccountRepository, BankAccountMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bank_accounts'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "bank_accounts", uniqueConstraints = {
    @UniqueConstraint(name = "uk_bank_accounts_number", columnNames = {"company_id", "account_number"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankAccount}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bank_accounts'.</p>
 *
 * <p><b>Database Table   :</b> {@code bank_accounts}</p>
 * <p><b>Module Deps      :</b> Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private BankBranch branch;

    @Column(name = "account_name", nullable = false, length = 150)
    private String accountName;

    @Column(name = "account_number", nullable = false, length = 50)
    private String accountNumber;

    @Column(length = 50)
    private String iban;

    @Column(name = "currency_code", nullable = false, length = 3)
    @Builder.Default
    private String currencyCode = "AED";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gl_account_id", nullable = false)
    private Account glAccount;

    @Column(name = "account_type", nullable = false, length = 30)
    @Builder.Default
    private String accountType = "CURRENT";

    @Column(name = "opening_balance", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal openingBalance = BigDecimal.ZERO;

    @Column(name = "current_balance", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(name = "reconciled_balance", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal reconciledBalance = BigDecimal.ZERO;

    @Column(name = "projected_closing_balance", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal projectedClosingBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}