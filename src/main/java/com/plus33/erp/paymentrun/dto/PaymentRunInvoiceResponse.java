/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.dto
 * File              : PaymentRunInvoiceResponse.java
 * Purpose           : Data Transfer Object for request/response in Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunInvoiceController
 * Related Service   : PaymentRunInvoiceService, PaymentRunInvoiceServiceImpl
 * Related Repository: PaymentRunInvoiceRepository
 * Related Entity    : PaymentRunInvoice
 * Related DTO       : PaymentRunInvoiceResponse
 * Related Mapper    : PaymentRunInvoiceMapper
 * Related DB Table  : payment_run_invoices
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentRunInvoiceController, PaymentRunInvoiceService, PaymentRunInvoiceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Paymentrun Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.paymentrun.dto;

import java.math.BigDecimal;

public record PaymentRunInvoiceResponse(
        Long id,
        Long supplierInvoiceId,
        String invoiceNumber,
        Long supplierId,
        String supplierName,
        BigDecimal invoiceOutstandingBalance,
        BigDecimal paymentAmount,
        String paymentReference
) {}
