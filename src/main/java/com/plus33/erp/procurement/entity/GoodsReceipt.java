/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.entity
 * File              : GoodsReceipt.java
 * Purpose           : JPA Entity representing a persistent database record in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GoodsReceiptController
 * Related Service   : GoodsReceiptService, GoodsReceiptServiceImpl
 * Related Repository: GoodsReceiptRepository
 * Related Entity    : GoodsReceipt
 * Related DTO       : N/A
 * Related Mapper    : GoodsReceiptMapper
 * Related DB Table  : goods_receipts
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : GoodsReceiptRepository, GoodsReceiptMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'goods_receipts'. Defines persistent domain object for Procurement Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.procurement.entity;

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
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code GoodsReceipt}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'goods_receipts'.</p>
 *
 * <p><b>Database Table   :</b> {@code goods_receipts}</p>
 * <p><b>Module Deps      :</b> Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "goods_receipts")
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receipt_number", nullable = false, unique = true, length = 50)
    private String receiptNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by", nullable = false)
    private User receivedBy;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GoodsReceiptStatus status = GoodsReceiptStatus.COMPLETED;

    @Column(length = 255)
    private String remarks;

    @Column(name = "supplier_delivery_note", length = 100)
    private String supplierDeliveryNote;

    @Column(name = "supplier_invoice_number", length = 100)
    private String supplierInvoiceNumber;

    @Column(name = "client_reference_id", nullable = false, unique = true)
    private UUID clientReferenceId;

    @Column(name = "total_quantity", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalQuantity = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @OneToMany(mappedBy = "goodsReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoodsReceiptItem> items = new ArrayList<>();

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        if (receivedAt == null) {
            receivedAt = LocalDateTime.now();
        }
    }
}