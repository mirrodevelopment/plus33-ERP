package com.plus33.erp.workforce.payroll;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class EuropePayrollProvider implements PayrollEngineProvider {

    @Override
    public String getCountryCode() {
        return "EU";
    }

    @Override
    public CalculationResult calculateEmployeePayroll(CalculationRequest request) {
        BigDecimal base = request.getBaseSalary() != null ? request.getBaseSalary() : BigDecimal.ZERO;
        BigDecimal overtimeHours = request.getOvertimeHours() != null ? request.getOvertimeHours() : BigDecimal.ZERO;
        BigDecimal overtimePay = overtimeHours.multiply(new BigDecimal("45.00"));

        BigDecimal grossPay = base.add(overtimePay);
        BigDecimal socialSecurity = grossPay.multiply(new BigDecimal("0.1500")).setScale(2, RoundingMode.HALF_EVEN); // 15% Social Security
        BigDecimal incomeTax = grossPay.multiply(new BigDecimal("0.2000")).setScale(2, RoundingMode.HALF_EVEN); // 20% Income Tax

        BigDecimal taxWithheld = incomeTax;
        BigDecimal totalDeductions = socialSecurity.add(incomeTax);
        BigDecimal employerContributions = grossPay.multiply(new BigDecimal("0.2000")).setScale(2, RoundingMode.HALF_EVEN); // Employer Social Match
        BigDecimal netPay = grossPay.subtract(totalDeductions);

        return new CalculationResult(grossPay, netPay, totalDeductions, employerContributions, taxWithheld);
    }
}
