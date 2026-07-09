/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * Package           : com.plus33.erp.store.entity
 * File              : WasteRecord.java
 * Purpose           : JPA Entity representing a persistent database record in Store Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WasteRecordController
 * Related Service   : WasteRecordService, WasteRecordServiceImpl
 * Related Repository: WasteRecordRepository
 * Related Entity    : WasteRecord
 * Related DTO       : N/A
 * Related Mapper    : WasteRecordMapper
 * Related DB Table  : waste_records
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Inventory Module, Security Module
 * Used By           : WasteRecordRepository, WasteRecordMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'waste_records'. Defines persistent domain object for Store Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.store.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.StockMovement;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Store Module</b>
 *
 * <p><b>Class  :</b> {@code WasteRecord}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.store.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'waste_records'.</p>
 *
 * <p><b>Database Table   :</b> {@code waste_records}</p>
 * <p><b>Module Deps      :</b> Organization, Inventory, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "waste_records")
@NoArgsConstructor
@AllArgsConstructor
public class WasteRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @Column(name = "waste_type", nullable = false, length = 30)
    private String wasteType;

    @Column(length = 255)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by", nullable = false)
    private User recordedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_movement_id")
    private StockMovement stockMovement;

    @Column(name = "recorded_at", nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        recordedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}