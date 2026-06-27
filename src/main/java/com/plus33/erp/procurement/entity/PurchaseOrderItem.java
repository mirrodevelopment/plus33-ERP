package com.plus33.erp.procurement.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;

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
