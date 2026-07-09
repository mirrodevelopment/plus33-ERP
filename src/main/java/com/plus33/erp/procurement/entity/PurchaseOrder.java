/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.entity
 * File              : PurchaseOrder.java
 * Purpose           : JPA Entity representing a persistent database record in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseOrderController
 * Related Service   : PurchaseOrderService, PurchaseOrderServiceImpl
 * Related Repository: PurchaseOrderRepository
 * Related Entity    : PurchaseOrder
 * Related DTO       : purchaseRequest
 * Related Mapper    : PurchaseOrderMapper
 * Related DB Table  : purchase_orders
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : PurchaseOrderRepository, PurchaseOrderMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'purchase_orders'. Defines persistent domain object for Procurement Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.procurement.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseOrder}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'purchase_orders'.</p>
 *
 * <p><b>Database Table   :</b> {@code purchase_orders}</p>
 * <p><b>Module Deps      :</b> Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "purchase_orders")
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_request_id", unique = true)
    private PurchaseRequest purchaseRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordered_by", nullable = false)
    private User orderedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by")
    private User issuedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PurchaseOrderStatus status = PurchaseOrderStatus.DRAFT;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Workflow Receipt & Audit Timestamps
    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "received_percentage", precision = 5, scale = 2)
    private BigDecimal receivedPercentage = BigDecimal.ZERO;

    // Financial Tracking Fields
    @Column(name = "subtotal_amount", precision = 12, scale = 2)
    private BigDecimal subtotalAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "currency_code", length = 10)
    private String currencyCode = "AED";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> items = new ArrayList<>();

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
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