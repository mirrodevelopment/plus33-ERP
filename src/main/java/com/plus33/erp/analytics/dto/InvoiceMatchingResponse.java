/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : InvoiceMatchingResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InvoiceMatchingController
 * Related Service   : InvoiceMatchingService, InvoiceMatchingServiceImpl
 * Related Repository: InvoiceMatchingRepository
 * Related Entity    : InvoiceMatching
 * Related DTO       : InvoiceMatchingResponse
 * Related Mapper    : InvoiceMatchingMapper
 * Related DB Table  : invoice_matchings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InvoiceMatchingController, InvoiceMatchingService, InvoiceMatchingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record InvoiceMatchingResponse(
        Long companyId,
        Long supplierInvoiceId,
        String invoiceNumber,
        Long purchaseOrderId,
        String orderNumber,
        String supplierName,
        BigDecimal invoiceTotalAmount,
        BigDecimal poTotalAmount,
        Boolean hasQuantityMismatch,
        Boolean hasPriceMismatch
) {}
