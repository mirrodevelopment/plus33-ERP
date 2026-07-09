/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetLease.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetLeaseController
 * Related Service   : FixedAssetLeaseService, FixedAssetLeaseServiceImpl
 * Related Repository: FixedAssetLeaseRepository
 * Related Entity    : FixedAssetLease
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetLeaseMapper
 * Related DB Table  : fixed_asset_leases
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetLeaseRepository, FixedAssetLeaseMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_leases'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import com.plus33.erp.finance.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetLease}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_leases'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_leases}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "fixed_asset_leases")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetLease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Enumerated(EnumType.STRING)
    @Column(name = "lease_type", nullable = false, length = 30)
    private LeaseType leaseType;

    @Column(name = "lease_start_date", nullable = false)
    private LocalDate leaseStartDate;

    @Column(name = "lease_end_date", nullable = false)
    private LocalDate leaseEndDate;

    @Column(name = "monthly_lease_payment", nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyLeasePayment;

    @Column(name = "lessor_name", nullable = false, length = 100)
    private String lessorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lease_liability_account_id", nullable = false)
    private Account leaseLiabilityAccount;

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