package com.plus33.erp.workforce.payroll;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class UaePayrollProvider implements PayrollEngineProvider {

    @Override
    public String getCountryCode() {
        return "AE";
    }

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
