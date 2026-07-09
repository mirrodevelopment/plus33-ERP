/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : CustomerReturnItem.java
 * Purpose           : JPA Entity representing a persistent database record in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerReturnItemController
 * Related Service   : CustomerReturnItemService, CustomerReturnItemServiceImpl
 * Related Repository: CustomerReturnItemRepository
 * Related Entity    : CustomerReturnItem
 * Related DTO       : N/A
 * Related Mapper    : CustomerReturnItemMapper
 * Related DB Table  : customer_return_items
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : CustomerReturnItemRepository, CustomerReturnItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'customer_return_items'. Defines persistent domain object for Sales Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

import com.plus33.erp.inventory.entity.InventoryLot;
import com.plus33.erp.inventory.entity.InventorySerial;
import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "customer_return_items", uniqueConstraints = {
    @UniqueConstraint(name = "uq_customer_return_item_product", columnNames = {"customer_return_id", "product_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerReturnItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'customer_return_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code customer_return_items}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_return_id", nullable = false)
    private CustomerReturn customerReturn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_item_id")
    private SalesOrderItem salesOrderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_invoice_item_id")
    private CustomerInvoiceItem customerInvoiceItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "inspection_result", length = 30)
    private InspectionResult inspectionResult;

    @Column(name = "inspection_notes", columnDefinition = "TEXT")
    private String inspectionNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private InventoryLot lot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serial_id")
    private InventorySerial serial;

    @Version
    private Long version;
}