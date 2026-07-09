/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.dto
 * File              : BalanceSheetEntry.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BalanceSheetEntryController
 * Related Service   : BalanceSheetEntryService, BalanceSheetEntryServiceImpl
 * Related Repository: BalanceSheetEntryRepository
 * Related Entity    : BalanceSheetEntry
 * Related DTO       : N/A
 * Related Mapper    : BalanceSheetEntryMapper
 * Related DB Table  : balance_sheet_entrys
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

public record BalanceSheetEntry(
    Long accountId,
    String accountCode,
    String accountName,
    BigDecimal balance
) {}
