package com.plus33.erp.ar.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * One line in a customer AR statement ledger.
 * <p>
 * Entry types: INVOICE, PAYMENT, CREDIT_NOTE, WRITE_OFF
 */
public record CustomerStatementEntry(
        LocalDate entryDate,
        String referenceNumber,
        String entryType,
        String description,
        BigDecimal debitAmount,
        BigDecimal creditAmount,
        BigDecimal runningBalance
) {}
