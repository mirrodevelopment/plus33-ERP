/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetLineResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetLineController
 * Related Service   : BudgetLineService, BudgetLineServiceImpl
 * Related Repository: BudgetLineRepository
 * Related Entity    : BudgetLine
 * Related DTO       : BudgetDimensionSetResponse, BudgetLineResponse
 * Related Mapper    : BudgetLineMapper
 * Related DB Table  : budget_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetLineController, BudgetLineService, BudgetLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetLineResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record BudgetLineResponse(
    Long id,
    Long budgetId,
    Long budgetVersionId,
    Long accountId,
    String accountCode,
    String accountName,
    BudgetDimensionSetResponse dimensionSet,
    LocalDate periodStartDate,
    LocalDate periodEndDate,
    BigDecimal allocatedAmount,
    BigDecimal reservedAmount,
    BigDecimal consumedAmount,
    BigDecimal availableAmount,
    Boolean isLocked,
    String distributionMethod,
    String formulaExpression,
    BigDecimal forecastConfidence,
    BigDecimal predictedSpend,
    BigDecimal predictedRevenue,
    String aiRecommendation,
    LocalDateTime aiGeneratedAt,
    String notes
) {}
