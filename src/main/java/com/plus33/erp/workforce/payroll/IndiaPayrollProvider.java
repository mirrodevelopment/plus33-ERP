/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.payroll
 * File              : IndiaPayrollProvider.java
 * Purpose           : Component of Workforce Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IndiaPayrollProviderController
 * Related Service   : IndiaPayrollProviderService, IndiaPayrollProviderServiceImpl
 * Related Repository: IndiaPayrollProviderRepository
 * Related Entity    : IndiaPayrollProvider
 * Related DTO       : CalculationRequest
 * Related Mapper    : IndiaPayrollProviderMapper
 * Related DB Table  : india_payroll_providers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.payroll;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code IndiaPayrollProvider}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.payroll}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Workforce Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class IndiaPayrollProvider implements PayrollEngineProvider {

    /**
     * Retrieves country code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves country code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public String getCountryCode() {
        return "IN";
    }

    /**
     * Calculates employee payroll totals including subtotal, tax, discounts, and net amount.
     *
     * @param request the validated request DTO containing input data
     * @return the CalculationResult result
     */
    /**
     * Calculates employee payroll totals including subtotal, tax, discounts, and net amount.
     *
     * @param request the validated request DTO containing input data
     * @return the CalculationResult result
     */
    @Override
    public CalculationResult calculateEmployeePayroll(CalculationRequest request) {
        BigDecimal base = request.getBaseSalary() != null ? request.getBaseSalary() : BigDecimal.ZERO;
        BigDecimal overtimeHours = request.getOvertimeHours() != null ? request.getOvertimeHours() : BigDecimal.ZERO;
        BigDecimal overtimePay = overtimeHours.multiply(new BigDecimal("250.00"));

        BigDecimal grossPay = base.add(overtimePay);
        BigDecimal pfDeduction = base.multiply(new BigDecimal("0.1200")).setScale(2, RoundingMode.HALF_EVEN); // 12% PF
        BigDecimal professionalTax = new BigDecimal("200.00");
        BigDecimal tdsTax = grossPay.multiply(new BigDecimal("0.1000")).setScale(2, RoundingMode.HALF_EVEN); // 10% TDS slab

        BigDecimal taxWithheld = tdsTax;
        BigDecimal totalDeductions = pfDeduction.add(professionalTax).add(tdsTax);
        BigDecimal employerContributions = pfDeduction; // Matching PF
        BigDecimal netPay = grossPay.subtract(totalDeductions);

        return new CalculationResult(grossPay, netPay, totalDeductions, employerContributions, taxWithheld);
    }
}