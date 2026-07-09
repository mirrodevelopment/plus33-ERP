/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : CreditNote.java
 * Purpose           : JPA Entity representing a persistent database record in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreditNoteController
 * Related Service   : CreditNoteService, CreditNoteServiceImpl
 * Related Repository: CreditNoteRepository
 * Related Entity    : CreditNote
 * Related DTO       : N/A
 * Related Mapper    : CreditNoteMapper
 * Related DB Table  : credit_notes
 * Related REST APIs : N/A
 * Depends On        : Finance Module, Organization Module, Security Module
 * Used By           : CreditNoteRepository, CreditNoteMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'credit_notes'. Defines persistent domain object for Sales Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "credit_notes", uniqueConstraints = {
    @UniqueConstraint(name = "uk_credit_notes_number", columnNames = {"company_id", "credit_note_number"}),
    @UniqueConstraint(name = "uk_credit_notes_client_ref", columnNames = {"company_id", "client_reference_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CreditNote}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'credit_notes'.</p>
 *
 * <p><b>Database Table   :</b> {@code credit_notes}</p>
 * <p><b>Module Deps      :</b> Finance, Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditNote {

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
    @JoinColumn(name = "customer_return_id")
    private CustomerReturn customerReturn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_invoice_id")
    private CustomerInvoice customerInvoice;

    @Column(name = "credit_note_number", nullable = false, length = 50)
    private String creditNoteNumber;

    @Column(name = "client_reference_id", nullable = false)
    private UUID clientReferenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CreditNoteStatus status;

    @Column(name = "subtotal_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotalAmount;

    @Column(name = "tax_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal taxAmount;

    @Builder.Default
    @Column(name = "discount_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id")
    private JournalEntry journalEntry;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    // Audit fields
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Version
    private Long version;

    @Builder.Default
    @OneToMany(mappedBy = "creditNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditNoteItem> items = new ArrayList<>();

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

    /**
     * Creates a new item and persists it to the database.
     *
     * @param item the item input value
     * @throws BusinessException if a business rule is violated
     */
    public void addItem(CreditNoteItem item) {
        items.add(item);
        item.setCreditNote(this);
    }

    /**
     * Permanently deletes the item from the database.
     *
     * @param item the item input value
     */
    public void removeItem(CreditNoteItem item) {
        items.remove(item);
        item.setCreditNote(null);
    }
}