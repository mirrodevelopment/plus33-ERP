package com.plus33.erp.procurement.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;

@Getter
@Setter
@Entity
@Table(name = "purchase_request_items")
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dimension_set_id")
    private BudgetDimensionSet dimensionSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_request_id", nullable = false)
    private PurchaseRequest purchaseRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "requested_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal requestedQuantity;

    @Column(name = "approved_quantity", precision = 12, scale = 2)
    private BigDecimal approvedQuantity;

    @Column(name = "unit_of_measure", length = 50)
    private String unitOfMeasure;

    @Column(length = 255)
    private String remarks;
}
