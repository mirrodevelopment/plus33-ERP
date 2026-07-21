/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.dto
 * File              : PayslipResponse.java
 * Purpose           : Data Transfer Object for request/response in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayslipController
 * Related Service   : PayslipService, PayslipServiceImpl
 * Related Repository: PayslipRepository
 * Related Entity    : Payslip
 * Related DTO       : PayslipResponse
 * Related Mapper    : PayslipMapper
 * Related DB Table  : payslips
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayslipController, PayslipService, PayslipServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Workforce Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.workforce.dto;

import java.math.BigDecimal;

public record PayslipResponse(
        Long runId,
        Long employeeId,
        String employeeName,
        String employeeCode,
        String runNumber,
        String paidAt,
        String status,
        BigDecimal grossPay,
        BigDecimal netPay,
        BigDecimal deductions,
        BigDecimal taxWithheld,
        String currencyCode,
        String attendanceSnapshot,
        String leaveSnapshot,
        String salarySnapshot,
        String workingHourSnapshot,
        String overtimeSnapshot,
        String benefitSnapshot,
        String taxSnapshot,
        String employerContributionSnapshot,
        String payrollAudit,
        String bankName,
        String bankAccountNumber,
        String ifscNumber
) {}
