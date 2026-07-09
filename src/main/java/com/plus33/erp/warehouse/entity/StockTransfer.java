/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Warehouse Module
 * Package           : com.plus33.erp.warehouse.entity
 * File              : StockTransfer.java
 * Purpose           : JPA Entity representing a persistent database record in Warehouse Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockTransferController
 * Related Service   : StockTransferService, StockTransferServiceImpl
 * Related Repository: StockTransferRepository
 * Related Entity    : StockTransfer
 * Related DTO       : N/A
 * Related Mapper    : StockTransferMapper
 * Related DB Table  : stock_transfers
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : StockTransferRepository, StockTransferMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'stock_transfers'. Defines persistent domain object for Warehouse Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.warehouse.entity;

import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Warehouse Module</b>
 *
 * <p><b>Class  :</b> {@code StockTransfer}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.warehouse.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'stock_transfers'.</p>
 *
 * <p><b>Database Table   :</b> {@code stock_transfers}</p>
 * <p><b>Module Deps      :</b> Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "stock_transfers")
@NoArgsConstructor
@AllArgsConstructor
public class StockTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transfer_number", nullable = false, unique = true, length = 50)
    private String transferNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_warehouse_id")
    private Warehouse sourceWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_store_id")
    private Store sourceStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_warehouse_id")
    private Warehouse destinationWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_store_id")
    private Store destinationStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";

    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        requestedAt = LocalDateTime.now();
    }
}