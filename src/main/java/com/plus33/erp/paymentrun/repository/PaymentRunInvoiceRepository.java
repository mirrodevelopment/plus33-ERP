/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.repository
 * File              : PaymentRunInvoiceRepository.java
 * Purpose           : JPA Repository providing database CRUD for Paymentrun Module entities
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
 * Depends On        : None
 * Used By           : PaymentRunInvoiceService, PaymentRunInvoiceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Paymentrun Module against the 'payment_run_invoices' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.paymentrun.repository;

import com.plus33.erp.paymentrun.entity.PaymentRunInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRunInvoiceRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payment_run_invoices' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payment_run_invoices}</p>
 * <p><b>Module Deps      :</b> Paymentrun</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PaymentRunInvoiceRepository extends JpaRepository<PaymentRunInvoice, Long> {
    List<PaymentRunInvoice> findByPaymentRunId(Long paymentRunId);
}
