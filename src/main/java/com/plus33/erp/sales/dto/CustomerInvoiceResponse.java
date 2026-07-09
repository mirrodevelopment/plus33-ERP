/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CustomerInvoiceResponse.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceController
 * Related Service   : CustomerInvoiceService, CustomerInvoiceServiceImpl
 * Related Repository: CustomerInvoiceRepository
 * Related Entity    : CustomerInvoice
 * Related DTO       : CustomerInvoiceItemResponse, CustomerInvoiceResponse
 * Related Mapper    : CustomerInvoiceMapper
 * Related DB Table  : customer_invoices
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerInvoiceController, CustomerInvoiceService, CustomerInvoiceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.CustomerInvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerInvoiceResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record CustomerInvoiceResponse(
        Long id,
        Long companyId,
        Long customerId,
        String customerName,
        String customerCode,
        Long salesOrderId,
        String salesOrderNumber,
        String invoiceNumber,
        UUID clientReferenceId,
        LocalDate invoiceDate,
        LocalDate dueDate,
        BigDecimal subtotalAmount,
        BigDecimal taxAmount,
        BigDecimal discountAmount,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        BigDecimal outstandingBalance,
        CustomerInvoiceStatus status,
        String currencyCode,
        Long journalEntryId,
        String journalEntryNumber,
        Long createdById,
        String createdByName,
        Long submittedById,
        String submittedByName,
        Long approvedById,
        String approvedByName,
        Long cancelledById,
        String cancelledByName,
        LocalDateTime createdAt,
        LocalDateTime submittedAt,
        LocalDateTime approvedAt,
        LocalDateTime cancelledAt,
        String cancellationReason,
        List<CustomerInvoiceItemResponse> items,
        Long version
) {}
