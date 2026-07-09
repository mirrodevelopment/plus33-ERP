/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetTransfer.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetTransferController
 * Related Service   : FixedAssetTransferService, FixedAssetTransferServiceImpl
 * Related Repository: FixedAssetTransferRepository
 * Related Entity    : FixedAssetTransfer
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetTransferMapper
 * Related DB Table  : fixed_asset_transfers
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : FixedAssetTransferRepository, FixedAssetTransferMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_transfers'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetTransfer}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_transfers'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_transfers}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "fixed_asset_transfers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_warehouse_id")
    private Warehouse fromWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_warehouse_id")
    private Warehouse toWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_store_id")
    private Store fromStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_store_id")
    private Store toStore;

    @Column(length = 255)
    private String reason;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransferStatus status = TransferStatus.REQUESTED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_company_id")
    private Company toCompany;

    @Column(name = "transferred_by", nullable = false, length = 100)
    private String transferredBy;

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