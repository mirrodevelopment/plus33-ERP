/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * Package           : com.plus33.erp.store.entity
 * File              : SalesTransactionItem.java
 * Purpose           : JPA Entity representing a persistent database record in Store Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesTransactionItemController
 * Related Service   : SalesTransactionItemService, SalesTransactionItemServiceImpl
 * Related Repository: SalesTransactionItemRepository
 * Related Entity    : SalesTransactionItem
 * Related DTO       : N/A
 * Related Mapper    : SalesTransactionItemMapper
 * Related DB Table  : sales_transaction_items
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : SalesTransactionItemRepository, SalesTransactionItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'sales_transaction_items'. Defines persistent domain object for Store Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.store.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.StockMovement;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Store Module</b>
 *
 * <p><b>Class  :</b> {@code SalesTransactionItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.store.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'sales_transaction_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code sales_transaction_items}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "sales_transaction_items")
@NoArgsConstructor
@AllArgsConstructor
public class SalesTransactionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_transaction_id", nullable = false)
    private SalesTransaction salesTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_movement_id")
    private StockMovement stockMovement;
}