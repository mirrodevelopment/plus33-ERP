/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Warehouse Module
 * Package           : com.plus33.erp.warehouse.entity
 * File              : StockTransferItem.java
 * Purpose           : JPA Entity representing a persistent database record in Warehouse Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockTransferItemController
 * Related Service   : StockTransferItemService, StockTransferItemServiceImpl
 * Related Repository: StockTransferItemRepository
 * Related Entity    : StockTransferItem
 * Related DTO       : N/A
 * Related Mapper    : StockTransferItemMapper
 * Related DB Table  : stock_transfer_items
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : StockTransferItemRepository, StockTransferItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'stock_transfer_items'. Defines persistent domain object for Warehouse Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.warehouse.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Warehouse Module</b>
 *
 * <p><b>Class  :</b> {@code StockTransferItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.warehouse.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'stock_transfer_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code stock_transfer_items}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "stock_transfer_items")
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_transfer_id", nullable = false)
    private StockTransfer stockTransfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "requested_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal requestedQuantity;

    @Column(name = "transferred_quantity", precision = 12, scale = 2)
    private BigDecimal transferredQuantity = BigDecimal.ZERO;
}