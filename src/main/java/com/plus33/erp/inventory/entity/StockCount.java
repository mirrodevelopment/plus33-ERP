/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : StockCount.java
 * Purpose           : JPA Entity representing a persistent database record in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountController
 * Related Service   : StockCountService, StockCountServiceImpl
 * Related Repository: StockCountRepository
 * Related Entity    : StockCount
 * Related DTO       : N/A
 * Related Mapper    : StockCountMapper
 * Related DB Table  : stock_counts
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : StockCountRepository, StockCountMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'stock_counts'. Defines persistent domain object for Inventory Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockCount}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'stock_counts'.</p>
 *
 * <p><b>Database Table   :</b> {@code stock_counts}</p>
 * <p><b>Module Deps      :</b> Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "stock_counts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count_number", nullable = false, unique = true, length = 50)
    private String countNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StockCountStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "count_type", nullable = false, length = 30)
    private StockCountType countType;

    @Column(name = "blind_count", nullable = false)
    @Builder.Default
    private boolean blindCount = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjustment_id")
    private InventoryAdjustment adjustment;

    @Column(name = "approval_required", nullable = false)
    private boolean approvalRequired;

    @Column(name = "approval_threshold_percentage", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal approvalThresholdPercentage = BigDecimal.valueOf(5.00);

    @Column(name = "client_reference_id", nullable = false, unique = true)
    private UUID clientReferenceId;

    @Column(name = "remarks", length = 255)
    private String remarks;

    @Column(name = "rejection_reason", length = 255)
    private String rejectionReason;

    @Column(name = "recount_number", nullable = false)
    @Builder.Default
    private int recountNumber = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "started_by")
    private User startedBy;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    private User submittedBy;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by")
    private User postedBy;

    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closed_by")
    private User closedBy;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @OneToMany(mappedBy = "stockCount", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StockCountItem> items = new ArrayList<>();

    @Version
    private Long version;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}