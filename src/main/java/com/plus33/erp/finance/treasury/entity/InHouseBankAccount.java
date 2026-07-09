/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : InHouseBankAccount.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InHouseBankAccountController
 * Related Service   : InHouseBankAccountService, InHouseBankAccountServiceImpl
 * Related Repository: InHouseBankAccountRepository
 * Related Entity    : InHouseBankAccount
 * Related DTO       : N/A
 * Related Mapper    : InHouseBankAccountMapper
 * Related DB Table  : in_house_bank_accounts
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : InHouseBankAccountRepository, InHouseBankAccountMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'in_house_bank_accounts'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code InHouseBankAccount}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'in_house_bank_accounts'.</p>
 *
 * <p><b>Database Table   :</b> {@code in_house_bank_accounts}</p>
 * <p><b>Module Deps      :</b> Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "in_house_bank_accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InHouseBankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subsidiary_company_id", nullable = false)
    private Company subsidiaryCompany;

    @Column(name = "account_number", nullable = false, unique = true, length = 50)
    private String accountNumber;

    @Column(name = "currency_code", nullable = false, length = 3)
    @Builder.Default
    private String currencyCode = "AED";

    @Column(name = "current_balance", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gl_account_id", nullable = false)
    private Account glAccount;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}