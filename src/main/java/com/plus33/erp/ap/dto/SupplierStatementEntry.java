/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.dto
 * File              : SupplierStatementEntry.java
 * Purpose           : Component of Ap Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierStatementEntryController
 * Related Service   : SupplierStatementEntryService, SupplierStatementEntryServiceImpl
 * Related Repository: SupplierStatementEntryRepository
 * Related Entity    : SupplierStatementEntry
 * Related DTO       : N/A
 * Related Mapper    : SupplierStatementEntryMapper
 * Related DB Table  : supplier_statement_entrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Ap Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Ap Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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
