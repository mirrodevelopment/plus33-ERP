/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.dto
 * File              : JournalEntryLineResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JournalEntryLineController
 * Related Service   : JournalEntryLineService, JournalEntryLineServiceImpl
 * Related Repository: JournalEntryLineRepository
 * Related Entity    : JournalEntryLine
 * Related DTO       : JournalEntryLineResponse
 * Related Mapper    : JournalEntryLineMapper
 * Related DB Table  : journal_entry_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : JournalEntryLineController, JournalEntryLineService, JournalEntryLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.dto;

import java.math.BigDecimal;

public record JournalEntryLineResponse(
        Long id,
        Long accountId,
        String accountCode,
        String accountName,
        BigDecimal debitAmount,
        BigDecimal creditAmount,
        Long dimensionSetId
) {}
