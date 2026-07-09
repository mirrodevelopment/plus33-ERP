/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.dto
 * File              : BalanceSheetResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BalanceSheetController
 * Related Service   : BalanceSheetService, BalanceSheetServiceImpl
 * Related Repository: BalanceSheetRepository
 * Related Entity    : BalanceSheet
 * Related DTO       : BalanceSheetResponse
 * Related Mapper    : BalanceSheetMapper
 * Related DB Table  : balance_sheets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BalanceSheetController, BalanceSheetService, BalanceSheetServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BalanceSheetResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record BalanceSheetResponse(
    List<BalanceSheetEntry> assets,
    List<BalanceSheetEntry> liabilities,
    List<BalanceSheetEntry> equities,
    BigDecimal totalAssets,
    BigDecimal totalLiabilities,
    BigDecimal totalEquity,
    BigDecimal difference,
    boolean balanced,
    String validationMessage,
    LocalDate asOfDate,
    LocalDateTime generatedAt,
    String generatedBy
) {}
