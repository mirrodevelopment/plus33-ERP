package com.plus33.erp.ar.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Chronological customer AR statement with a running balance after every entry.
 */
public record CustomerStatementResponse(
        Long customerId,
        String customerName,
        Long companyId,
        LocalDate fromDate,
        LocalDate toDate,
        BigDecimal openingBalance,
        BigDecimal closingBalance,
        List<CustomerStatementEntry> entries
) {}
