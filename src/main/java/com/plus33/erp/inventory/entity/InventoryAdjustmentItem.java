/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : InventoryAdjustmentItem.java
 * Purpose           : JPA Entity representing a persistent database record in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAdjustmentItemController
 * Related Service   : InventoryAdjustmentItemService, InventoryAdjustmentItemServiceImpl
 * Related Repository: InventoryAdjustmentItemRepository
 * Related Entity    : InventoryAdjustmentItem
 * Related DTO       : N/A
 * Related Mapper    : InventoryAdjustmentItemMapper
 * Related DB Table  : inventory_adjustment_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryAdjustmentItemRepository, InventoryAdjustmentItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'inventory_adjustment_items'. Defines persistent domain object for Inventory Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryAdjustmentItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'inventory_adjustment_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_adjustment_items}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "inventory_adjustment_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryAdjustmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjustment_id", nullable = false)
    private InventoryAdjustment inventoryAdjustment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @Version
    private Long version;
}