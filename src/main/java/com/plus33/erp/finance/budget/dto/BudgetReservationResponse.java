/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetReservationResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetReservationController
 * Related Service   : BudgetReservationService, BudgetReservationServiceImpl
 * Related Repository: BudgetReservationRepository
 * Related Entity    : BudgetReservation
 * Related DTO       : BudgetReservationResponse
 * Related Mapper    : BudgetReservationMapper
 * Related DB Table  : budget_reservations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetReservationController, BudgetReservationService, BudgetReservationServiceImpl
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
 * <p><b>Class  :</b> {@code BudgetReservationResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record BudgetReservationResponse(
    Long id,
    Long budgetLineId,
    String sourceModule,
    Long sourceReferenceId,
    String referenceNumber,
    BigDecimal reservedAmount,
    String status,
    LocalDate expiryDate,
    LocalDateTime createdAt
) {}
