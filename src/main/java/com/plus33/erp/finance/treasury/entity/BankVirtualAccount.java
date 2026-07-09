/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : BankVirtualAccount.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankVirtualAccountController
 * Related Service   : BankVirtualAccountService, BankVirtualAccountServiceImpl
 * Related Repository: BankVirtualAccountRepository
 * Related Entity    : BankVirtualAccount
 * Related DTO       : N/A
 * Related Mapper    : BankVirtualAccountMapper
 * Related DB Table  : bank_virtual_accounts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BankVirtualAccountRepository, BankVirtualAccountMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bank_virtual_accounts'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankVirtualAccount}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bank_virtual_accounts'.</p>
 *
 * <p><b>Database Table   :</b> {@code bank_virtual_accounts}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "bank_virtual_accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankVirtualAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id", nullable = false)
    private BankAccount parentAccount;

    @Column(name = "virtual_account_number", nullable = false, unique = true, length = 50)
    private String virtualAccountNumber;

    @Column(name = "owner_reference_type", nullable = false, length = 30)
    private String ownerReferenceType; // CUSTOMER, SUPPLIER

    @Column(name = "owner_reference_id", nullable = false)
    private Long ownerReferenceId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}