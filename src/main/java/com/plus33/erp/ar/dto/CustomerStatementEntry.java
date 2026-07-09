/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.dto
 * File              : CustomerStatementEntry.java
 * Purpose           : Component of Ar Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerStatementEntryController
 * Related Service   : CustomerStatementEntryService, CustomerStatementEntryServiceImpl
 * Related Repository: CustomerStatementEntryRepository
 * Related Entity    : CustomerStatementEntry
 * Related DTO       : N/A
 * Related Mapper    : CustomerStatementEntryMapper
 * Related DB Table  : customer_statement_entrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Ar Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Ar Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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
