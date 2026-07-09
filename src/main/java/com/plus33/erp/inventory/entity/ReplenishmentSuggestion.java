/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : ReplenishmentSuggestion.java
 * Purpose           : JPA Entity representing a persistent database record in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentSuggestionController
 * Related Service   : ReplenishmentSuggestionService, ReplenishmentSuggestionServiceImpl
 * Related Repository: ReplenishmentSuggestionRepository
 * Related Entity    : ReplenishmentSuggestion
 * Related DTO       : purchaseRequest
 * Related Mapper    : ReplenishmentSuggestionMapper
 * Related DB Table  : replenishment_suggestions
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Procurement Module
 * Used By           : ReplenishmentSuggestionRepository, ReplenishmentSuggestionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'replenishment_suggestions'. Defines persistent domain object for Inventory Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.procurement.entity.PurchaseRequest;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentSuggestion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'replenishment_suggestions'.</p>
 *
 * <p><b>Database Table   :</b> {@code replenishment_suggestions}</p>
 * <p><b>Module Deps      :</b> Organization, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "replenishment_suggestions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplenishmentSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id", nullable = false)
    private ReplenishmentRule rule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "current_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentQuantity;

    @Column(name = "reserved_quantity", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal reservedQuantity = BigDecimal.ZERO;

    @Column(name = "available_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal availableQuantity;

    @Column(name = "suggested_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal suggestedQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private ReplenishmentSuggestionStatus status = ReplenishmentSuggestionStatus.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_source", nullable = false, length = 20)
    @Builder.Default
    private ReplenishmentEvaluationSource evaluationSource = ReplenishmentEvaluationSource.MANUAL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_request_id")
    private PurchaseRequest purchaseRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id")
    private InventoryTransfer transfer;

    @Column(name = "client_reference_id", nullable = false, unique = true)
    private UUID clientReferenceId;

    @Version
    private Long version;

    @Column(name = "evaluated_at", nullable = false)
    @Builder.Default
    private LocalDateTime evaluatedAt = LocalDateTime.now();

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;

    @Column(name = "fulfilled_at")
    private LocalDateTime fulfilledAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (evaluatedAt == null) {
            evaluatedAt = LocalDateTime.now();
        }
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