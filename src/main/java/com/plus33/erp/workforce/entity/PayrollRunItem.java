package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payroll_run_items")
public class PayrollRunItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payroll_run_id", nullable = false)
    private Long payrollRunId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "gross_pay", nullable = false, precision = 14, scale = 2)
    private BigDecimal grossPay = BigDecimal.ZERO;

    @Column(name = "net_pay", nullable = false, precision = 14, scale = 2)
    private BigDecimal netPay = BigDecimal.ZERO;

    @Column(name = "total_deductions", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalDeductions = BigDecimal.ZERO;

    @Column(name = "employer_contributions", nullable = false, precision = 14, scale = 2)
    private BigDecimal employerContributions = BigDecimal.ZERO;

    @Column(name = "tax_withheld", nullable = false, precision = 14, scale = 2)
    private BigDecimal taxWithheld = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    private String status = "CALCULATED";

    public PayrollRunItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPayrollRunId() { return payrollRunId; }
    public void setPayrollRunId(Long payrollRunId) { this.payrollRunId = payrollRunId; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public BigDecimal getGrossPay() { return grossPay; }
    public void setGrossPay(BigDecimal grossPay) { this.grossPay = grossPay; }
    public BigDecimal getNetPay() { return netPay; }
    public void setNetPay(BigDecimal netPay) { this.netPay = netPay; }
    public BigDecimal getTotalDeductions() { return totalDeductions; }
    public void setTotalDeductions(BigDecimal totalDeductions) { this.totalDeductions = totalDeductions; }
    public BigDecimal getEmployerContributions() { return employerContributions; }
    public void setEmployerContributions(BigDecimal employerContributions) { this.employerContributions = employerContributions; }
    public BigDecimal getTaxWithheld() { return taxWithheld; }
    public void setTaxWithheld(BigDecimal taxWithheld) { this.taxWithheld = taxWithheld; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
