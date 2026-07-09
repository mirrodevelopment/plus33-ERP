/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetRelation.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetRelationController
 * Related Service   : FixedAssetRelationService, FixedAssetRelationServiceImpl
 * Related Repository: FixedAssetRelationRepository
 * Related Entity    : FixedAssetRelation
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetRelationMapper
 * Related DB Table  : fixed_asset_relations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetRelationRepository, FixedAssetRelationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_relations'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetRelation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_relations'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_relations}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}