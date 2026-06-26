package com.plus33.erp.ar.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response payload for an AR write-off record.
 */
public record ARWriteOffResponse(
        Long id,
        String writeOffNumber,
        Long companyId,
        Long customerInvoiceId,
        String invoiceNumber,
        Long customerId,
        String customerName,
        BigDecimal writeOffAmount,
        LocalDate writeOffDate,
        String reason,
        Long journalEntryId,
        String journalEntryNumber,
        Long writtenOffByUserId,
        String writtenOffByUserName,
        LocalDateTime createdAt
) {}
