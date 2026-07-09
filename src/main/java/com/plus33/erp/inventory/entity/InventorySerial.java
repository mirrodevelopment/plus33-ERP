/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : InventorySerial.java
 * Purpose           : JPA Entity representing a persistent database record in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventorySerialController
 * Related Service   : InventorySerialService, InventorySerialServiceImpl
 * Related Repository: InventorySerialRepository
 * Related Entity    : InventorySerial
 * Related DTO       : N/A
 * Related Mapper    : InventorySerialMapper
 * Related DB Table  : inventory_serials
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : InventorySerialRepository, InventorySerialMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'inventory_serials'. Defines persistent domain object for Inventory Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventorySerial}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'inventory_serials'.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_serials}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "inventory_serials")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventorySerial {

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

    @Column(name = "serial_number", nullable = false, length = 100)
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private InventorySerialStatus status = InventorySerialStatus.IN_STOCK;

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