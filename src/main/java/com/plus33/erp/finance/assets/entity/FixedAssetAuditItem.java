/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetAuditItem.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetAuditItemController
 * Related Service   : FixedAssetAuditItemService, FixedAssetAuditItemServiceImpl
 * Related Repository: FixedAssetAuditItemRepository
 * Related Entity    : FixedAssetAuditItem
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetAuditItemMapper
 * Related DB Table  : fixed_asset_audit_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetAuditItemRepository, FixedAssetAuditItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_audit_items'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetAuditItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_audit_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_audit_items}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "fixed_asset_audit_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetAuditItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_id", nullable = false)
    private FixedAssetAudit audit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AssetAuditResult result;

    @Builder.Default
    @Column(name = "location_mismatch", nullable = false)
    private Boolean locationMismatch = false;

    @Column(name = "barcode_scanned", length = 100)
    private String barcodeScanned;

    @Column(length = 255)
    private String remarks;

    @Column(name = "photo_evidence_url")
    private String photoEvidenceUrl;
}