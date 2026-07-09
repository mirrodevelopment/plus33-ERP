/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.dto
 * File              : PayrollRunResponse.java
 * Purpose           : Data Transfer Object for request/response in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollRunController
 * Related Service   : PayrollRunService, PayrollRunServiceImpl
 * Related Repository: PayrollRunRepository
 * Related Entity    : PayrollRun
 * Related DTO       : PayrollRunResponse
 * Related Mapper    : PayrollRunMapper
 * Related DB Table  : payroll_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollRunController, PayrollRunService, PayrollRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Workforce Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.workforce.dto;

import com.plus33.erp.workforce.entity.PayrollCalendarType;
import com.plus33.erp.workforce.entity.PayrollRunStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollRunResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Workforce Module.</p>
 *
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record PayrollRunResponse(
        Long id,
        Long companyId,
        String runNumber,
        PayrollCalendarType calendarType,
        String countryCode,
        String runType,
        PayrollRunStatus status,
        BigDecimal totalGross,
        BigDecimal totalNet,
        BigDecimal totalEmployerContributions,
        BigDecimal totalTaxes,
        String executedBy,
        String approvedBy,
        LocalDateTime postedAt,
        LocalDateTime paidAt,
        LocalDateTime createdAt
) {}
