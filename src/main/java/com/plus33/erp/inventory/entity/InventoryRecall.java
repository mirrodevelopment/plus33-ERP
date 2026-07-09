/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : InventoryRecall.java
 * Purpose           : JPA Entity representing a persistent database record in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryRecallController
 * Related Service   : InventoryRecallService, InventoryRecallServiceImpl
 * Related Repository: InventoryRecallRepository
 * Related Entity    : InventoryRecall
 * Related DTO       : N/A
 * Related Mapper    : InventoryRecallMapper
 * Related DB Table  : inventory_recalls
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : InventoryRecallRepository, InventoryRecallMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'inventory_recalls'. Defines persistent domain object for Inventory Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryRecall}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'inventory_recalls'.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_recalls}</p>
 * <p><b>Module Deps      :</b> Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "inventory_recalls")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRecall {

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
    @JoinColumn(name = "lot_id")
    private InventoryLot lot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serial_id")
    private InventorySerial serial;

    @Column(name = "recall_number", nullable = false, unique = true, length = 50)
    private String recallNumber;

    @Column(name = "recall_reason", nullable = false, length = 255)
    private String recallReason;

    @Column(name = "recall_reference_number", length = 100)
    private String recallReferenceNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private InventoryRecallStatus status = InventoryRecallStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recalled_by_id", nullable = false)
    private User recalledBy;

    @Column(name = "recalled_at", nullable = false)
    private LocalDateTime recalledAt;

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
        if (recalledAt == null) {
            recalledAt = LocalDateTime.now();
        }
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