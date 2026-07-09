/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.dto
 * File              : IncomeStatementResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IncomeStatementController
 * Related Service   : IncomeStatementService, IncomeStatementServiceImpl
 * Related Repository: IncomeStatementRepository
 * Related Entity    : IncomeStatement
 * Related DTO       : IncomeStatementResponse
 * Related Mapper    : IncomeStatementMapper
 * Related DB Table  : income_statements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IncomeStatementController, IncomeStatementService, IncomeStatementServiceImpl
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
 * <p><b>Class  :</b> {@code IncomeStatementResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record IncomeStatementResponse(
    List<IncomeStatementEntry> revenues,
    List<IncomeStatementEntry> expenses,
    BigDecimal totalRevenue,
    BigDecimal totalExpenses,
    BigDecimal netIncome,
    LocalDate startDate,
    LocalDate endDate,
    LocalDateTime generatedAt,
    String generatedBy
) {}
