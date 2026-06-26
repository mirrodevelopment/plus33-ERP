package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

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
