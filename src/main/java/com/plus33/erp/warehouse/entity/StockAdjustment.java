/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Warehouse Module
 * Package           : com.plus33.erp.warehouse.entity
 * File              : StockAdjustment.java
 * Purpose           : JPA Entity representing a persistent database record in Warehouse Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockAdjustmentController
 * Related Service   : StockAdjustmentService, StockAdjustmentServiceImpl
 * Related Repository: StockAdjustmentRepository
 * Related Entity    : StockAdjustment
 * Related DTO       : N/A
 * Related Mapper    : StockAdjustmentMapper
 * Related DB Table  : stock_adjustments
 * Related REST APIs : N/A
 * Depends On        : Inventory Module, Organization Module, Security Module
 * Used By           : StockAdjustmentRepository, StockAdjustmentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'stock_adjustments'. Defines persistent domain object for Warehouse Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.warehouse.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Warehouse Module</b>
 *
 * <p><b>Class  :</b> {@code StockAdjustment}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.warehouse.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'stock_adjustments'.</p>
 *
 * <p><b>Database Table   :</b> {@code stock_adjustments}</p>
 * <p><b>Module Deps      :</b> Inventory, Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "stock_adjustments")
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "adjustment_number", nullable = false, unique = true, length = 50)
    private String adjustmentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "previous_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal previousQuantity;

    @Column(name = "adjusted_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal adjustedQuantity;

    @Column(length = 255)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}