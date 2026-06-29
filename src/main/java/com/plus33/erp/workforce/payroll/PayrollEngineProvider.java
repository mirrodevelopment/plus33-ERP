package com.plus33.erp.workforce.payroll;

import java.math.BigDecimal;

public interface PayrollEngineProvider {

    String getCountryCode();

    CalculationResult calculateEmployeePayroll(CalculationRequest request);

    class CalculationRequest {
        private Long employeeId;
        private BigDecimal baseSalary;
        private BigDecimal hoursWorked;
        private BigDecimal overtimeHours;

        public CalculationRequest(Long employeeId, BigDecimal baseSalary, BigDecimal hoursWorked, BigDecimal overtimeHours) {
            this.employeeId = employeeId;
            this.baseSalary = baseSalary;
            this.hoursWorked = hoursWorked;
            this.overtimeHours = overtimeHours;
        }

        public Long getEmployeeId() { return employeeId; }
        public BigDecimal getBaseSalary() { return baseSalary; }
        public BigDecimal getHoursWorked() { return hoursWorked; }
        public BigDecimal getOvertimeHours() { return overtimeHours; }
    }

    class CalculationResult {
        private BigDecimal grossPay;
        private BigDecimal netPay;
        private BigDecimal totalDeductions;
        private BigDecimal employerContributions;
        private BigDecimal taxWithheld;

        public CalculationResult(BigDecimal grossPay, BigDecimal netPay, BigDecimal totalDeductions, BigDecimal employerContributions, BigDecimal taxWithheld) {
            this.grossPay = grossPay;
            this.netPay = netPay;
            this.totalDeductions = totalDeductions;
            this.employerContributions = employerContributions;
            this.taxWithheld = taxWithheld;
        }

        public BigDecimal getGrossPay() { return grossPay; }
        public BigDecimal getNetPay() { return netPay; }
        public BigDecimal getTotalDeductions() { return totalDeductions; }
        public BigDecimal getEmployerContributions() { return employerContributions; }
        public BigDecimal getTaxWithheld() { return taxWithheld; }
    }
}
