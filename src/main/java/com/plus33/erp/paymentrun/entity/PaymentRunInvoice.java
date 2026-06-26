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
