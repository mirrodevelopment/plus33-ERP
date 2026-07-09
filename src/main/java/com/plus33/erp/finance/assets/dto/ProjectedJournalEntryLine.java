/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : ProjectedJournalEntryLine.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectedJournalEntryLineController
 * Related Service   : ProjectedJournalEntryLineService, ProjectedJournalEntryLineServiceImpl
 * Related Repository: ProjectedJournalEntryLineRepository
 * Related Entity    : ProjectedJournalEntryLine
 * Related DTO       : N/A
 * Related Mapper    : ProjectedJournalEntryLineMapper
 * Related DB Table  : projected_journal_entry_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;

public record ProjectedJournalEntryLine(
    Long accountId,
    String accountCode,
    String accountName,
    BigDecimal debitAmount,
    BigDecimal creditAmount
) {}
