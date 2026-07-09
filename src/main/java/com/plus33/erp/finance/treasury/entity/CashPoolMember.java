/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : CashPoolMember.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CashPoolMemberController
 * Related Service   : CashPoolMemberService, CashPoolMemberServiceImpl
 * Related Repository: CashPoolMemberRepository
 * Related Entity    : CashPoolMember
 * Related DTO       : N/A
 * Related Mapper    : CashPoolMemberMapper
 * Related DB Table  : cash_pool_members
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CashPoolMemberRepository, CashPoolMemberMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'cash_pool_members'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CashPoolMember}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'cash_pool_members'.</p>
 *
 * <p><b>Database Table   :</b> {@code cash_pool_members}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "cash_pool_members")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashPoolMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pool_id", nullable = false)
    private CashPool pool;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false, unique = true)
    private BankAccount bankAccount;

    @Column(name = "sweep_priority", nullable = false)
    @Builder.Default
    private Integer sweepPriority = 1;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}