/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.entity
 * File              : ARWriteOff.java
 * Purpose           : JPA Entity representing a persistent database record in Ar Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ARWriteOffController
 * Related Service   : ARWriteOffService, ARWriteOffServiceImpl
 * Related Repository: ARWriteOffRepository
 * Related Entity    : ARWriteOff
 * Related DTO       : N/A
 * Related Mapper    : ARWriteOffMapper
 * Related DB Table  : ar_write_offs
 * Related REST APIs : N/A
 * Depends On        : Finance Module, Organization Module, Sales Module, Security Module
 * Used By           : ARWriteOffRepository, ARWriteOffMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'ar_write_offs'. Defines persistent domain object for Ar Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.ar.entity;

import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerInvoice;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Immutable accounting record of a bad-debt write-off.
 * <p>
 * The associated {@link CustomerInvoice} retains its current status;
 * only {@code outstandingBalance} is reduced. The GL entry posts:
 * DR Bad Debt Expense (5300) / CR Accounts Receivable (1400).
 */
@Getter
@Setter
@Entity
@Table(name = "ar_write_offs", uniqueConstraints = {
    @UniqueConstraint(name = "uk_arwo_company_number", columnNames = {"company_id", "write_off_number"})
})
/**
 * <b>PLUS33 Coffee ERP -- Ar Module</b>
 *
 * <p><b>Class  :</b> {@code ARWriteOff}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.ar.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'ar_write_offs'.</p>
 *
 * <p><b>Database Table   :</b> {@code ar_write_offs}</p>
 * <p><b>Module Deps      :</b> Finance, Organization, Sales, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ARWriteOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "write_off_number", nullable = false, length = 50)
    private String writeOffNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_invoice_id", nullable = false)
    private CustomerInvoice customerInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "write_off_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal writeOffAmount;

    @Column(name = "write_off_date", nullable = false)
    private LocalDate writeOffDate;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id")
    private JournalEntry journalEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "written_off_by", nullable = false)
    private User writtenOffBy;

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