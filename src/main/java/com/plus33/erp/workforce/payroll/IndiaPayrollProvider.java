package com.plus33.erp.workforce.payroll;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class IndiaPayrollProvider implements PayrollEngineProvider {

    @Override
    public String getCountryCode() {
        return "IN";
    }

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
