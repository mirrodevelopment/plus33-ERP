/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : PickListItem.java
 * Purpose           : JPA Entity representing a persistent database record in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListItemController
 * Related Service   : PickListItemService, PickListItemServiceImpl
 * Related Repository: PickListItemRepository
 * Related Entity    : PickListItem
 * Related DTO       : N/A
 * Related Mapper    : PickListItemMapper
 * Related DB Table  : pick_list_items
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : PickListItemRepository, PickListItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'pick_list_items'. Defines persistent domain object for Sales Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "pick_list_items", uniqueConstraints = {
    @UniqueConstraint(name = "uq_pick_item_product", columnNames = {"pick_list_id", "product_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code PickListItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'pick_list_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code pick_list_items}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pick_list_id", nullable = false)
    private PickList pickList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_item_id", nullable = false)
    private SalesOrderItem salesOrderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "ordered_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal orderedQuantity;

    @Builder.Default
    @Column(name = "allocated_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal allocatedQuantity = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "picked_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal pickedQuantity = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "shipped_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal shippedQuantity = BigDecimal.ZERO;

    @Version
    private Long version;
}