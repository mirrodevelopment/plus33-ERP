/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.dto
 * File              : IncomeStatementEntry.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IncomeStatementEntryController
 * Related Service   : IncomeStatementEntryService, IncomeStatementEntryServiceImpl
 * Related Repository: IncomeStatementEntryRepository
 * Related Entity    : IncomeStatementEntry
 * Related DTO       : N/A
 * Related Mapper    : IncomeStatementEntryMapper
 * Related DB Table  : income_statement_entrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.dto;

import java.math.BigDecimal;

public record IncomeStatementEntry(
    Long accountId,
    String accountCode,
    String accountName,
    BigDecimal balance
) {}
