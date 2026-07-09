/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : EmployeePayroll.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeePayrollController
 * Related Service   : EmployeePayrollService, EmployeePayrollServiceImpl
 * Related Repository: EmployeePayrollRepository
 * Related Entity    : EmployeePayroll
 * Related DTO       : N/A
 * Related Mapper    : EmployeePayrollMapper
 * Related DB Table  : employee_payrolls
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeePayrollRepository, EmployeePayrollMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'employee_payrolls'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeePayroll}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'employee_payrolls'.</p>
 *
 * <p><b>Database Table   :</b> {@code employee_payrolls}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "employee_payrolls")
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePayroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_period_id", nullable = false)
    private PayrollPeriod payrollPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "base_salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal baseSalary = BigDecimal.ZERO;

    @Column(name = "overtime_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal overtimeAmount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal allowances = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal deductions = BigDecimal.ZERO;

    @Column(name = "gross_salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal grossSalary = BigDecimal.ZERO;

    @Column(name = "net_salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal netSalary = BigDecimal.ZERO;

    @Column(name = "payment_status", nullable = false, length = 30)
    private String paymentStatus = "PENDING";

    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
}