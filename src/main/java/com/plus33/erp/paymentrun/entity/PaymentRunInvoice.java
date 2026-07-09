/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.entity
 * File              : PaymentRunInvoice.java
 * Purpose           : JPA Entity representing a persistent database record in Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunInvoiceController
 * Related Service   : PaymentRunInvoiceService, PaymentRunInvoiceServiceImpl
 * Related Repository: PaymentRunInvoiceRepository
 * Related Entity    : PaymentRunInvoice
 * Related DTO       : N/A
 * Related Mapper    : PaymentRunInvoiceMapper
 * Related DB Table  : payment_run_invoices
 * Related REST APIs : N/A
 * Depends On        : Finance Module
 * Used By           : PaymentRunInvoiceRepository, PaymentRunInvoiceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payment_run_invoices'. Defines persistent domain object for Paymentrun Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.paymentrun.entity;

import com.plus33.erp.finance.entity.SupplierInvoice;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "payment_run_invoices", uniqueConstraints = {
    @UniqueConstraint(name = "uq_payment_run_invoice", columnNames = {"payment_run_id", "supplier_invoice_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRunInvoice}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payment_run_invoices'.</p>
 *
 * <p><b>Database Table   :</b> {@code payment_run_invoices}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRunInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_run_id", nullable = false)
    private PaymentRun paymentRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_invoice_id", nullable = false)
    private SupplierInvoice supplierInvoice;

    @Column(name = "invoice_outstanding_balance", nullable = false, precision = 14, scale = 2)
    private BigDecimal invoiceOutstandingBalance;

    @Column(name = "payment_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "payment_reference", nullable = false, length = 100)
    private String paymentReference;
}