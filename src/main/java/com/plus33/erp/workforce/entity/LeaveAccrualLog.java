/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : LeaveAccrualLog.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LeaveAccrualLogController
 * Related Service   : LeaveAccrualLogService, LeaveAccrualLogServiceImpl
 * Related Repository: LeaveAccrualLogRepository
 * Related Entity    : LeaveAccrualLog
 * Related DTO       : N/A
 * Related Mapper    : LeaveAccrualLogMapper
 * Related DB Table  : leave_accrual_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LeaveAccrualLogRepository, LeaveAccrualLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'leave_accrual_logs'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code LeaveAccrualLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'leave_accrual_logs'.</p>
 *
 * <p><b>Database Table   :</b> {@code leave_accrual_logs}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "leave_accrual_logs")
public class LeaveAccrualLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "accrual_date", nullable = false)
    private LocalDate accrualDate;

    @Column(name = "leave_type", nullable = false, length = 50)
    private String leaveType;

    @Column(name = "accrued_hours", nullable = false, precision = 7, scale = 2)
    private BigDecimal accruedHours = BigDecimal.ZERO;

    @Column(name = "monetary_value", nullable = false, precision = 14, scale = 2)
    private BigDecimal monetaryValue = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public LeaveAccrualLog() {}

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
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
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
     * Retrieves accrual date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getAccrualDate() { return accrualDate; }
    /**
     * Performs the setAccrualDate operation in this module.
     *
     * @param accrualDate the accrualDate input value
     */
    public void setAccrualDate(LocalDate accrualDate) { this.accrualDate = accrualDate; }
    /**
     * Retrieves leave type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLeaveType() { return leaveType; }
    /**
     * Performs the setLeaveType operation in this module.
     *
     * @param leaveType the leaveType input value
     */
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }
    /**
     * Retrieves accrued hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAccruedHours() { return accruedHours; }
    /**
     * Performs the setAccruedHours operation in this module.
     *
     * @param accruedHours the accruedHours input value
     */
    public void setAccruedHours(BigDecimal accruedHours) { this.accruedHours = accruedHours; }
    /**
     * Retrieves monetary value data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMonetaryValue() { return monetaryValue; }
    /**
     * Performs the setMonetaryValue operation in this module.
     *
     * @param monetaryValue the monetaryValue input value
     */
    public void setMonetaryValue(BigDecimal monetaryValue) { this.monetaryValue = monetaryValue; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}