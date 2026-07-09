/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : InventoryLot.java
 * Purpose           : JPA Entity representing a persistent database record in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryLotController
 * Related Service   : InventoryLotService, InventoryLotServiceImpl
 * Related Repository: InventoryLotRepository
 * Related Entity    : InventoryLot
 * Related DTO       : N/A
 * Related Mapper    : InventoryLotMapper
 * Related DB Table  : inventory_lots
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : InventoryLotRepository, InventoryLotMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'inventory_lots'. Defines persistent domain object for Inventory Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryLot}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'inventory_lots'.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_lots}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "inventory_lots")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "lot_number", nullable = false, length = 100)
    private String lotNumber;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "manufactured_date")
    private LocalDate manufacturedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private InventoryLotStatus status = InventoryLotStatus.ACTIVE;

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