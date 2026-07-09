/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.dto
 * File              : AROverdueInvoiceResponse.java
 * Purpose           : Data Transfer Object for request/response in Ar Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AROverdueInvoiceController
 * Related Service   : AROverdueInvoiceService, AROverdueInvoiceServiceImpl
 * Related Repository: AROverdueInvoiceRepository
 * Related Entity    : AROverdueInvoice
 * Related DTO       : AROverdueInvoiceResponse
 * Related Mapper    : AROverdueInvoiceMapper
 * Related DB Table  : a_r_overdue_invoices
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AROverdueInvoiceController, AROverdueInvoiceService, AROverdueInvoiceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ar Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ar.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Summary of one overdue invoice.
 * Overdue is derived: due_date < today AND outstanding_balance > 0.
 * No separate OVERDUE status is introduced.
 */
public record AROverdueInvoiceResponse(
        Long invoiceId,
        String invoiceNumber,
        Long customerId,
        String customerName,
        Long companyId,
        LocalDate invoiceDate,
        LocalDate dueDate,
        Long daysOverdue,
        BigDecimal totalAmount,
        BigDecimal outstandingBalance,
        String status
) {}
