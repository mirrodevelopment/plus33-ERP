/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : Attendance.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AttendanceController
 * Related Service   : AttendanceService, AttendanceServiceImpl
 * Related Repository: AttendanceRepository
 * Related Entity    : Attendance
 * Related DTO       : N/A
 * Related Mapper    : AttendanceMapper
 * Related DB Table  : attendance
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AttendanceRepository, AttendanceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'attendance'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code Attendance}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'attendance'.</p>
 *
 * <p><b>Database Table   :</b> {@code attendance}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "attendance")
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(name = "work_minutes")
    private Integer workMinutes = 0;

    @Column(name = "overtime_minutes")
    private Integer overtimeMinutes = 0;

    @Column(name = "late_minutes")
    private Integer lateMinutes = 0;

    @Column(name = "early_out_minutes")
    private Integer earlyOutMinutes = 0;

    @Column(name = "gps_coordinates", length = 100)
    private String gpsCoordinates;

    @Column(name = "device_info", length = 255)
    private String deviceInfo;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "paid_leave")
    private Boolean paidLeave = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_id")
    private EmployeeLeave leave;

    @Column(name = "deduction")
    private Boolean deduction = false;

    @Column(name = "payroll_status", length = 30)
    private String payrollStatus = "Pending";

    @Column(name = "leave_minutes")
    private Integer leaveMinutes = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}