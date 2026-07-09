/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.entity
 * File              : SupplierInvoiceItem.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceItemController
 * Related Service   : SupplierInvoiceItemService, SupplierInvoiceItemServiceImpl
 * Related Repository: SupplierInvoiceItemRepository
 * Related Entity    : SupplierInvoiceItem
 * Related DTO       : N/A
 * Related Mapper    : SupplierInvoiceItemMapper
 * Related DB Table  : supplier_invoice_items
 * Related REST APIs : N/A
 * Depends On        : Inventory Module, Procurement Module
 * Used By           : SupplierInvoiceItemRepository, SupplierInvoiceItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'supplier_invoice_items'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.procurement.entity.GoodsReceiptItem;
import com.plus33.erp.procurement.entity.PurchaseOrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;

@Getter
@Setter
@Entity
@Table(name = "supplier_invoice_items", uniqueConstraints = {
    @UniqueConstraint(name = "uk_supplier_invoice_item_po_item", columnNames = {"supplier_invoice_id", "purchase_order_item_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierInvoiceItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'supplier_invoice_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code supplier_invoice_items}</p>
 * <p><b>Module Deps      :</b> Inventory, Procurement, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierInvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dimension_set_id")
    private BudgetDimensionSet dimensionSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_invoice_id", nullable = false)
    private SupplierInvoice supplierInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_item_id", nullable = false)
    private PurchaseOrderItem purchaseOrderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_receipt_item_id")
    private GoodsReceiptItem goodsReceiptItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "net_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "tax_rate", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(name = "tax_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "discount_amount", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount;
}