/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.payroll
 * File              : PayrollEngineProvider.java
 * Purpose           : Service interface contract defining the API for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollEngineProviderController
 * Related Service   : PayrollEngineProviderService, PayrollEngineProviderServiceImpl
 * Related Repository: PayrollEngineProviderRepository
 * Related Entity    : PayrollEngineProvider
 * Related DTO       : CalculationRequest
 * Related Mapper    : PayrollEngineProviderMapper
 * Related DB Table  : payroll_engine_providers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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
