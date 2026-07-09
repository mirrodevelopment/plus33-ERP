/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : AttendanceSyncLog.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AttendanceSyncLogController
 * Related Service   : AttendanceSyncLogService, AttendanceSyncLogServiceImpl
 * Related Repository: AttendanceSyncLogRepository
 * Related Entity    : AttendanceSyncLog
 * Related DTO       : N/A
 * Related Mapper    : AttendanceSyncLogMapper
 * Related DB Table  : attendance_sync_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AttendanceSyncLogRepository, AttendanceSyncLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'attendance_sync_logs'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code AttendanceSyncLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'attendance_sync_logs'.</p>
 *
 * <p><b>Database Table   :</b> {@code attendance_sync_logs}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "attendance_sync_logs")
public class AttendanceSyncLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "sync_date", nullable = false)
    private LocalDate syncDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_source", nullable = false)
    private AttendanceSource attendanceSource;

    @Column(name = "hours_worked", nullable = false, precision = 5, scale = 2)
    private BigDecimal hoursWorked = BigDecimal.ZERO;

    @Column(name = "overtime_hours", nullable = false, precision = 5, scale = 2)
    private BigDecimal overtimeHours = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public AttendanceSyncLog() {}

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
     * Retrieves sync date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getSyncDate() { return syncDate; }
    /**
     * Performs the setSyncDate operation in this module.
     *
     * @param syncDate the syncDate input value
     */
    public void setSyncDate(LocalDate syncDate) { this.syncDate = syncDate; }
    /**
     * Retrieves attendance source data from the database.
     *
     * @return the AttendanceSource result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public AttendanceSource getAttendanceSource() { return attendanceSource; }
    /**
     * Performs the setAttendanceSource operation in this module.
     *
     * @param attendanceSource the attendanceSource input value
     */
    public void setAttendanceSource(AttendanceSource attendanceSource) { this.attendanceSource = attendanceSource; }
    /**
     * Retrieves hours worked data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getHoursWorked() { return hoursWorked; }
    /**
     * Performs the setHoursWorked operation in this module.
     *
     * @param hoursWorked the hoursWorked input value
     */
    public void setHoursWorked(BigDecimal hoursWorked) { this.hoursWorked = hoursWorked; }
    /**
     * Retrieves overtime hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getOvertimeHours() { return overtimeHours; }
    /**
     * Performs the setOvertimeHours operation in this module.
     *
     * @param overtimeHours the overtimeHours input value
     */
    public void setOvertimeHours(BigDecimal overtimeHours) { this.overtimeHours = overtimeHours; }
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