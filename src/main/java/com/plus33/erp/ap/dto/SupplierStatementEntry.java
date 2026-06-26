package com.plus33.erp.ap.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SupplierStatementEntry(
        LocalDate entryDate,
        String referenceNumber,
        String entryType,
        String description,
        BigDecimal debitAmount,
        BigDecimal creditAmount,
        BigDecimal runningBalance
) {}
