package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.CustomerInvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
