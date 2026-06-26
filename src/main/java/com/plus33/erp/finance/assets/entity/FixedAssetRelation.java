package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fixed_asset_relations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_asset_id", nullable = false)
    private FixedAsset sourceAsset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_asset_id", nullable = false)
    private FixedAsset targetAsset;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", nullable = false, length = 50)
    private AssetRelationType relationType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
