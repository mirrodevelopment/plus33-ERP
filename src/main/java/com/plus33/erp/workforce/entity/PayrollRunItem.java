/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : PayrollRunItem.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollRunItemController
 * Related Service   : PayrollRunItemService, PayrollRunItemServiceImpl
 * Related Repository: PayrollRunItemRepository
 * Related Entity    : PayrollRunItem
 * Related DTO       : N/A
 * Related Mapper    : PayrollRunItemMapper
 * Related DB Table  : payroll_run_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollRunItemRepository, PayrollRunItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payroll_run_items'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollRunItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payroll_run_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_run_items}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    @Column(name = "attendance_snapshot")
    private String attendanceSnapshot;

    @Column(name = "leave_snapshot")
    private String leaveSnapshot;

    @Column(name = "salary_snapshot")
    private String salarySnapshot;

    @Column(name = "working_hour_snapshot")
    private String workingHourSnapshot;

    @Column(name = "overtime_snapshot")
    private String overtimeSnapshot;

    @Column(name = "benefit_snapshot")
    private String benefitSnapshot;

    @Column(name = "tax_snapshot")
    private String taxSnapshot;

    @Column(name = "employer_contribution_snapshot")
    private String employerContributionSnapshot;

    @Column(name = "payroll_audit")
    private String payrollAudit;

    public PayrollRunItem() {}

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves payroll run id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPayrollRunId() { return payrollRunId; }
    /**
     * Performs the setPayrollRunId operation in this module.
     *
     * @param payrollRunId the payrollRunId input value
     */
    public void setPayrollRunId(Long payrollRunId) { this.payrollRunId = payrollRunId; }
    /**
     * Retrieves employee id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getEmployeeId() { return employeeId; }
    /**
     * Performs the setEmployeeId operation in this module.
     *
     * @param employeeId the employeeId input value
     */
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    /**
     * Retrieves gross pay data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getGrossPay() { return grossPay; }
    /**
     * Performs the setGrossPay operation in this module.
     *
     * @param grossPay the grossPay input value
     */
    public void setGrossPay(BigDecimal grossPay) { this.grossPay = grossPay; }
    /**
     * Retrieves net pay data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getNetPay() { return netPay; }
    /**
     * Performs the setNetPay operation in this module.
     *
     * @param netPay the netPay input value
     */
    public void setNetPay(BigDecimal netPay) { this.netPay = netPay; }
    /**
     * Retrieves total deductions data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalDeductions() { return totalDeductions; }
    /**
     * Performs the setTotalDeductions operation in this module.
     *
     * @param totalDeductions the totalDeductions input value
     */
    public void setTotalDeductions(BigDecimal totalDeductions) { this.totalDeductions = totalDeductions; }
    /**
     * Retrieves employer contributions data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEmployerContributions() { return employerContributions; }
    /**
     * Performs the setEmployerContributions operation in this module.
     *
     * @param employerContributions the employerContributions input value
     */
    public void setEmployerContributions(BigDecimal employerContributions) { this.employerContributions = employerContributions; }
    /**
     * Retrieves tax withheld data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTaxWithheld() { return taxWithheld; }
    /**
     * Performs the setTaxWithheld operation in this module.
     *
     * @param taxWithheld the taxWithheld input value
     */
    public void setTaxWithheld(BigDecimal taxWithheld) { this.taxWithheld = taxWithheld; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }

    public String getAttendanceSnapshot() { return attendanceSnapshot; }
    public void setAttendanceSnapshot(String attendanceSnapshot) { this.attendanceSnapshot = attendanceSnapshot; }

    public String getLeaveSnapshot() { return leaveSnapshot; }
    public void setLeaveSnapshot(String leaveSnapshot) { this.leaveSnapshot = leaveSnapshot; }

    public String getSalarySnapshot() { return salarySnapshot; }
    public void setSalarySnapshot(String salarySnapshot) { this.salarySnapshot = salarySnapshot; }

    public String getWorkingHourSnapshot() { return workingHourSnapshot; }
    public void setWorkingHourSnapshot(String workingHourSnapshot) { this.workingHourSnapshot = workingHourSnapshot; }

    public String getOvertimeSnapshot() { return overtimeSnapshot; }
    public void setOvertimeSnapshot(String overtimeSnapshot) { this.overtimeSnapshot = overtimeSnapshot; }

    public String getBenefitSnapshot() { return benefitSnapshot; }
    public void setBenefitSnapshot(String benefitSnapshot) { this.benefitSnapshot = benefitSnapshot; }

    public String getTaxSnapshot() { return taxSnapshot; }
    public void setTaxSnapshot(String taxSnapshot) { this.taxSnapshot = taxSnapshot; }

    public String getEmployerContributionSnapshot() { return employerContributionSnapshot; }
    public void setEmployerContributionSnapshot(String employerContributionSnapshot) { this.employerContributionSnapshot = employerContributionSnapshot; }

    public String getPayrollAudit() { return payrollAudit; }
    public void setPayrollAudit(String payrollAudit) { this.payrollAudit = payrollAudit; }
}