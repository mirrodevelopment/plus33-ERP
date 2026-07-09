/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : CustomerInvoice.java
 * Purpose           : JPA Entity representing a persistent database record in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceController
 * Related Service   : CustomerInvoiceService, CustomerInvoiceServiceImpl
 * Related Repository: CustomerInvoiceRepository
 * Related Entity    : CustomerInvoice
 * Related DTO       : N/A
 * Related Mapper    : CustomerInvoiceMapper
 * Related DB Table  : customer_invoices
 * Related REST APIs : N/A
 * Depends On        : Finance Module, Organization Module, Security Module
 * Used By           : CustomerInvoiceRepository, CustomerInvoiceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'customer_invoices'. Defines persistent domain object for Sales Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "customer_invoices", uniqueConstraints = {
    @UniqueConstraint(name = "uk_customer_invoice_company_number", columnNames = {"company_id", "invoice_number"}),
    @UniqueConstraint(name = "uk_customer_invoice_client_reference", columnNames = {"company_id", "client_reference_id"}),
    @UniqueConstraint(name = "uk_customer_invoice_journal_entry", columnNames = {"journal_entry_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerInvoice}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'customer_invoices'.</p>
 *
 * <p><b>Database Table   :</b> {@code customer_invoices}</p>
 * <p><b>Module Deps      :</b> Finance, Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id")
    private SalesOrder salesOrder;

    @Column(name = "invoice_number", nullable = false, length = 50)
    private String invoiceNumber;

    @Column(name = "client_reference_id", nullable = false)
    private UUID clientReferenceId;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "subtotal_amount", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal subtotalAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "paid_amount", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "outstanding_balance", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal outstandingBalance = BigDecimal.ZERO;

    @Column(name = "credited_amount", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal creditedAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private CustomerInvoiceStatus status = CustomerInvoiceStatus.DRAFT;

    @Column(name = "currency_code", nullable = false, length = 3)
    @Builder.Default
    private String currencyCode = "AED";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id")
    private JournalEntry journalEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    private User submittedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Version
    private Long version;

    @OneToMany(mappedBy = "customerInvoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CustomerInvoiceItem> items = new ArrayList<>();

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}