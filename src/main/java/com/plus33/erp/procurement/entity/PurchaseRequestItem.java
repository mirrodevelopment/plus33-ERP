/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.entity
 * File              : PurchaseRequestItem.java
 * Purpose           : JPA Entity representing a persistent database record in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseRequestItemController
 * Related Service   : PurchaseRequestItemService, PurchaseRequestItemServiceImpl
 * Related Repository: PurchaseRequestItemRepository
 * Related Entity    : PurchaseRequestItem
 * Related DTO       : purchaseRequest
 * Related Mapper    : PurchaseRequestItemMapper
 * Related DB Table  : purchase_request_items
 * Related REST APIs : N/A
 * Depends On        : Inventory Module, Finance Module
 * Used By           : PurchaseRequestItemRepository, PurchaseRequestItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'purchase_request_items'. Defines persistent domain object for Procurement Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PurchaseRequestItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'purchase_request_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code purchase_request_items}</p>
 * <p><b>Module Deps      :</b> Inventory, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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