/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.entity
 * File              : PurchaseOrderItem.java
 * Purpose           : JPA Entity representing a persistent database record in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseOrderItemController
 * Related Service   : PurchaseOrderItemService, PurchaseOrderItemServiceImpl
 * Related Repository: PurchaseOrderItemRepository
 * Related Entity    : PurchaseOrderItem
 * Related DTO       : N/A
 * Related Mapper    : PurchaseOrderItemMapper
 * Related DB Table  : purchase_order_items
 * Related REST APIs : N/A
 * Depends On        : Inventory Module, Finance Module
 * Used By           : PurchaseOrderItemRepository, PurchaseOrderItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'purchase_order_items'. Defines persistent domain object for Procurement Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.procurement.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseOrderItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'purchase_order_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code purchase_order_items}</p>
 * <p><b>Module Deps      :</b> Inventory, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "purchase_order_items")
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dimension_set_id")
    private BudgetDimensionSet dimensionSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "ordered_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal orderedQuantity;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "received_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal receivedQuantity = BigDecimal.ZERO;

    @Column(name = "invoiced_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal invoicedQuantity = BigDecimal.ZERO;

    @Column(name = "remaining_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal remainingQuantity;

    @Column(length = 255)
    private String remarks;

    @Version
    private Long version;
}