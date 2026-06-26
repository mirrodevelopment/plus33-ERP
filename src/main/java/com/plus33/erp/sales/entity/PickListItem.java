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
