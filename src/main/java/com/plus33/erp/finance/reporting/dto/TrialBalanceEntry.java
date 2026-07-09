/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.dto
 * File              : TrialBalanceEntry.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TrialBalanceEntryController
 * Related Service   : TrialBalanceEntryService, TrialBalanceEntryServiceImpl
 * Related Repository: TrialBalanceEntryRepository
 * Related Entity    : TrialBalanceEntry
 * Related DTO       : N/A
 * Related Mapper    : TrialBalanceEntryMapper
 * Related DB Table  : trial_balance_entrys
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

public record TrialBalanceEntry(
    Long accountId,
    String accountCode,
    String accountName,
    String accountType,
    Long parentAccountId,
    BigDecimal openingDebit,
    BigDecimal openingCredit,
    BigDecimal periodDebit,
    BigDecimal periodCredit,
    BigDecimal closingDebit,
    BigDecimal closingCredit
) {}
