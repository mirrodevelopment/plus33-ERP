/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetController
 * Related Service   : BudgetService, BudgetServiceImpl
 * Related Repository: BudgetRepository
 * Related Entity    : Budget
 * Related DTO       : BudgetLineResponse, BudgetResponse
 * Related Mapper    : BudgetMapper
 * Related DB Table  : budgets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetController, BudgetService, BudgetServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record BudgetResponse(
    Long id,
    Long companyId,
    Long fiscalYearId,
    Long budgetPolicyId,
    Long workflowTemplateId,
    String code,
    String name,
    String budgetType,
    String periodType,
    String scenario,
    String status,
    Integer versionNumber,
    Boolean isForecast,
    String forecastType,
    String forecastCycleCode,
    Boolean isFrozen,
    Boolean isActive,
    String rateLockType,
    BigDecimal budgetExchangeRate,
    String createdBy,
    String approvedBy,
    LocalDateTime approvedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<BudgetLineResponse> lines
) {}
