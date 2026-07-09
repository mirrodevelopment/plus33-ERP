/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : ReplenishmentRule.java
 * Purpose           : JPA Entity representing a persistent database record in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentRuleController
 * Related Service   : ReplenishmentRuleService, ReplenishmentRuleServiceImpl
 * Related Repository: ReplenishmentRuleRepository
 * Related Entity    : ReplenishmentRule
 * Related DTO       : N/A
 * Related Mapper    : ReplenishmentRuleMapper
 * Related DB Table  : replenishment_rules
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : ReplenishmentRuleRepository, ReplenishmentRuleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'replenishment_rules'. Defines persistent domain object for Inventory Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'replenishment_rules'.</p>
 *
 * <p><b>Database Table   :</b> {@code replenishment_rules}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "replenishment_rules")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplenishmentRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "min_quantity", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal minQuantity = BigDecimal.ZERO;

    @Column(name = "max_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal maxQuantity;

    @Column(name = "reorder_point", nullable = false, precision = 12, scale = 2)
    private BigDecimal reorderPoint;

    @Column(name = "reorder_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal reorderQuantity;

    @Column(name = "lead_time_days", nullable = false)
    @Builder.Default
    private int leadTimeDays = 0;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "client_reference_id", nullable = false, unique = true)
    private UUID clientReferenceId;

    @Version
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}