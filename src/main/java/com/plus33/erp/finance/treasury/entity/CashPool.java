/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : CashPool.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CashPoolController
 * Related Service   : CashPoolService, CashPoolServiceImpl
 * Related Repository: CashPoolRepository
 * Related Entity    : CashPool
 * Related DTO       : N/A
 * Related Mapper    : CashPoolMapper
 * Related DB Table  : cash_pools
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : CashPoolRepository, CashPoolMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'cash_pools'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CashPool}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'cash_pools'.</p>
 *
 * <p><b>Database Table   :</b> {@code cash_pools}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "cash_pools")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "pool_name", nullable = false, length = 150)
    private String poolName;

    @Column(name = "pool_type", nullable = false, length = 30)
    @Builder.Default
    private String poolType = "ZERO_BALANCE"; // ZERO_BALANCE, NOTIONAL, TARGET_BALANCE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "header_account_id", nullable = false)
    private BankAccount headerAccount;

    @Column(name = "target_balance", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal targetBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CashPoolMember> members = new ArrayList<>();
}