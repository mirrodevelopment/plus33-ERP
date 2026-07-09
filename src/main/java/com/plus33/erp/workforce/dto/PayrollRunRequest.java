/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.dto
 * File              : PayrollRunRequest.java
 * Purpose           : Data Transfer Object for request/response in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollRunController
 * Related Service   : PayrollRunService, PayrollRunServiceImpl
 * Related Repository: PayrollRunRepository
 * Related Entity    : PayrollRun
 * Related DTO       : PayrollRunRequest
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

public record PayrollRunRequest(
        Long companyId,
        Long payrollPeriodId,
        String runNumber,
        PayrollCalendarType calendarType,
        String countryCode,
        String runType
) {}
