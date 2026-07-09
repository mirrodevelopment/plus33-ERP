/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.payroll
 * File              : UaePayrollProvider.java
 * Purpose           : Component of Workforce Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: UaePayrollProviderController
 * Related Service   : UaePayrollProviderService, UaePayrollProviderServiceImpl
 * Related Repository: UaePayrollProviderRepository
 * Related Entity    : UaePayrollProvider
 * Related DTO       : CalculationRequest
 * Related Mapper    : UaePayrollProviderMapper
 * Related DB Table  : uae_payroll_providers
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
 * <p><b>Class  :</b> {@code UaePayrollProvider}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.payroll}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Workforce Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class UaePayrollProvider implements PayrollEngineProvider {

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
        return "AE";
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
        BigDecimal overtimePay = overtimeHours.multiply(new BigDecimal("40.00"));

        BigDecimal grossPay = base.add(overtimePay);
        BigDecimal taxWithheld = BigDecimal.ZERO; // UAE 0% Personal Income Tax
        BigDecimal totalDeductions = BigDecimal.ZERO;

        BigDecimal employerContributions = grossPay.multiply(new BigDecimal("0.0500")).setScale(2, RoundingMode.HALF_EVEN); // GPSSA / Gratuity provision
        BigDecimal netPay = grossPay;

        return new CalculationResult(grossPay, netPay, totalDeductions, employerContributions, taxWithheld);
    }
}