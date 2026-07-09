/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.entity
 * File              : GoodsReceiptItem.java
 * Purpose           : JPA Entity representing a persistent database record in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GoodsReceiptItemController
 * Related Service   : GoodsReceiptItemService, GoodsReceiptItemServiceImpl
 * Related Repository: GoodsReceiptItemRepository
 * Related Entity    : GoodsReceiptItem
 * Related DTO       : N/A
 * Related Mapper    : GoodsReceiptItemMapper
 * Related DB Table  : goods_receipt_items
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : GoodsReceiptItemRepository, GoodsReceiptItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'goods_receipt_items'. Defines persistent domain object for Procurement Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.procurement.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code GoodsReceiptItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'goods_receipt_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code goods_receipt_items}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "goods_receipt_items")
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_receipt_id", nullable = false)
    private GoodsReceipt goodsReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "received_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal receivedQuantity;

    @Column(length = 255)
    private String remarks;
}