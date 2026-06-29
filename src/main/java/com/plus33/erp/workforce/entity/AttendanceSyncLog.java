package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public LocalDate getSyncDate() { return syncDate; }
    public void setSyncDate(LocalDate syncDate) { this.syncDate = syncDate; }
    public AttendanceSource getAttendanceSource() { return attendanceSource; }
    public void setAttendanceSource(AttendanceSource attendanceSource) { this.attendanceSource = attendanceSource; }
    public BigDecimal getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(BigDecimal hoursWorked) { this.hoursWorked = hoursWorked; }
    public BigDecimal getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(BigDecimal overtimeHours) { this.overtimeHours = overtimeHours; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
