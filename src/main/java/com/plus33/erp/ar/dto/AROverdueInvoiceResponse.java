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
