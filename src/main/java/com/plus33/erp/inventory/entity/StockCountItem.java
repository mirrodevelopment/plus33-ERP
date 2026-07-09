/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : StockCountItem.java
 * Purpose           : JPA Entity representing a persistent database record in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountItemController
 * Related Service   : StockCountItemService, StockCountItemServiceImpl
 * Related Repository: StockCountItemRepository
 * Related Entity    : StockCountItem
 * Related DTO       : N/A
 * Related Mapper    : StockCountItemMapper
 * Related DB Table  : stock_count_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StockCountItemRepository, StockCountItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'stock_count_items'. Defines persistent domain object for Inventory Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockCountItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'stock_count_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code stock_count_items}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "stock_count_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockCountItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_count_id", nullable = false)
    private StockCount stockCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "system_quantity", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal systemQuantity = BigDecimal.ZERO;

    @Column(name = "reserved_quantity", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal reservedQuantity = BigDecimal.ZERO;

    @Column(name = "available_quantity", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal availableQuantity = BigDecimal.ZERO;

    @Column(name = "counted_quantity", precision = 12, scale = 2)
    private BigDecimal countedQuantity;

    @Column(name = "variance", precision = 12, scale = 2)
    private BigDecimal variance;

    @Version
    private Long version;
}