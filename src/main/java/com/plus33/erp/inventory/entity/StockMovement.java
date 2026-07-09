/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : StockMovement.java
 * Purpose           : JPA Entity representing a persistent database record in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockMovementController
 * Related Service   : StockMovementService, StockMovementServiceImpl
 * Related Repository: StockMovementRepository
 * Related Entity    : StockMovement
 * Related DTO       : N/A
 * Related Mapper    : StockMovementMapper
 * Related DB Table  : stock_movements
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : StockMovementRepository, StockMovementMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'stock_movements'. Defines persistent domain object for Inventory Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockMovement}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'stock_movements'.</p>
 *
 * <p><b>Database Table   :</b> {@code stock_movements}</p>
 * <p><b>Module Deps      :</b> Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "stock_movements")
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "movement_type", nullable = false, length = 50)
    private String movementType;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @Column(name = "reference_no", length = 100)
    private String referenceNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", length = 50)
    private StockMovementReferenceType referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "reference_number", length = 50)
    private String referenceNumber;

    @Column(name = "movement_at", nullable = false, updatable = false)
    private LocalDateTime movementAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        movementAt = LocalDateTime.now();
    }
}