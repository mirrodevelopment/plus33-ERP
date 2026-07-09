/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : CustomerInvoiceItem.java
 * Purpose           : JPA Entity representing a persistent database record in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceItemController
 * Related Service   : CustomerInvoiceItemService, CustomerInvoiceItemServiceImpl
 * Related Repository: CustomerInvoiceItemRepository
 * Related Entity    : CustomerInvoiceItem
 * Related DTO       : N/A
 * Related Mapper    : CustomerInvoiceItemMapper
 * Related DB Table  : customer_invoice_items
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : CustomerInvoiceItemRepository, CustomerInvoiceItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'customer_invoice_items'. Defines persistent domain object for Sales Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "customer_invoice_items", uniqueConstraints = {
    @UniqueConstraint(name = "uk_customer_invoice_item_so_item", columnNames = {"customer_invoice_id", "sales_order_item_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerInvoiceItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'customer_invoice_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code customer_invoice_items}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_invoice_id", nullable = false)
    private CustomerInvoice customerInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_item_id")
    private SalesOrderItem salesOrderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pick_list_item_id")
    private PickListItem pickListItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @Column(name = "tax_percentage", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal taxPercentage = BigDecimal.ZERO;

    @Column(name = "net_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "tax_amount", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "returned_quantity", nullable = false, precision = 14, scale = 4)
    @Builder.Default
    private BigDecimal returnedQuantity = BigDecimal.ZERO;

    @Version
    private Long version;
}