/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetInsuranceClaim.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetInsuranceClaimController
 * Related Service   : FixedAssetInsuranceClaimService, FixedAssetInsuranceClaimServiceImpl
 * Related Repository: FixedAssetInsuranceClaimRepository
 * Related Entity    : FixedAssetInsuranceClaim
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetInsuranceClaimMapper
 * Related DB Table  : fixed_asset_insurance_claims
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetInsuranceClaimRepository, FixedAssetInsuranceClaimMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_insurance_claims'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetInsuranceClaim}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_insurance_claims'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_insurance_claims}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "fixed_asset_insurance_claims")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetInsuranceClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Column(name = "claim_number", nullable = false, length = 50)
    private String claimNumber;

    @Column(name = "claim_date", nullable = false)
    private LocalDate claimDate;

    @Column(name = "claim_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal claimAmount;

    @Column(nullable = false, length = 30)
    private String status; // PENDING, APPROVED, REJECTED, PAID

    @Column(length = 255)
    private String description;

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