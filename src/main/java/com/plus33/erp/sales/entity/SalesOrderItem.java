/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : SalesOrderItem.java
 * Purpose           : JPA Entity representing a persistent database record in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderItemController
 * Related Service   : SalesOrderItemService, SalesOrderItemServiceImpl
 * Related Repository: SalesOrderItemRepository
 * Related Entity    : SalesOrderItem
 * Related DTO       : N/A
 * Related Mapper    : SalesOrderItemMapper
 * Related DB Table  : sales_order_items
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : SalesOrderItemRepository, SalesOrderItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'sales_order_items'. Defines persistent domain object for Sales Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "sales_order_items", uniqueConstraints = {
    @UniqueConstraint(name = "uq_sales_order_item_product", columnNames = {"sales_order_id", "product_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code SalesOrderItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'sales_order_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code sales_order_items}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id", nullable = false)
    private SalesOrder salesOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "ordered_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal orderedQuantity;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Builder.Default
    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "tax_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxPercentage = BigDecimal.ZERO;

    @Column(name = "line_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal;

    @Builder.Default
    @Column(name = "allocated_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal allocatedQuantity = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "fulfilled_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal fulfilledQuantity = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "invoiced_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal invoicedQuantity = BigDecimal.ZERO;

    @Version
    private Long version;
}