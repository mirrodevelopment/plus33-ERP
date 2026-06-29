package com.plus33.erp.workforce.payroll;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class UsPayrollProvider implements PayrollEngineProvider {

    @Override
    public String getCountryCode() {
        return "US";
    }

    @Override
    public CalculationResult calculateEmployeePayroll(CalculationRequest request) {
        BigDecimal base = request.getBaseSalary() != null ? request.getBaseSalary() : BigDecimal.ZERO;
        BigDecimal overtimeHours = request.getOvertimeHours() != null ? request.getOvertimeHours() : BigDecimal.ZERO;
        BigDecimal overtimePay = overtimeHours.multiply(new BigDecimal("35.00")); // standard overtime rate stub

        BigDecimal grossPay = base.add(overtimePay);
        BigDecimal ficaTax = grossPay.multiply(new BigDecimal("0.0765")).setScale(2, RoundingMode.HALF_EVEN); // 7.65% FICA
        BigDecimal federalTax = grossPay.multiply(new BigDecimal("0.1200")).setScale(2, RoundingMode.HALF_EVEN); // 12% Fed Tax
        BigDecimal taxWithheld = ficaTax.add(federalTax);

        BigDecimal employerContributions = grossPay.multiply(new BigDecimal("0.0765")).setScale(2, RoundingMode.HALF_EVEN); // Employer FICA match
        BigDecimal totalDeductions = taxWithheld;
        BigDecimal netPay = grossPay.subtract(totalDeductions);

        return new CalculationResult(grossPay, netPay, totalDeductions, employerContributions, taxWithheld);
    }
}
