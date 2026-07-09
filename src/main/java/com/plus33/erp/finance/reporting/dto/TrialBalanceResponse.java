/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.dto
 * File              : TrialBalanceResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TrialBalanceController
 * Related Service   : TrialBalanceService, TrialBalanceServiceImpl
 * Related Repository: TrialBalanceRepository
 * Related Entity    : TrialBalance
 * Related DTO       : TrialBalanceResponse
 * Related Mapper    : TrialBalanceMapper
 * Related DB Table  : trial_balances
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TrialBalanceController, TrialBalanceService, TrialBalanceServiceImpl
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
 * <p><b>Class  :</b> {@code TrialBalanceResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record TrialBalanceResponse(
    List<TrialBalanceEntry> entries,
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal totalDebits,
    BigDecimal totalCredits,
    BigDecimal difference,
    boolean balanced,
    String validationMessage,
    List<ReportWarning> warnings,
    int numberOfAccounts,
    LocalDateTime generatedAt,
    String generatedBy
) {}
