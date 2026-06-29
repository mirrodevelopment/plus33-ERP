package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public LocalDate getAccrualDate() { return accrualDate; }
    public void setAccrualDate(LocalDate accrualDate) { this.accrualDate = accrualDate; }
    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }
    public BigDecimal getAccruedHours() { return accruedHours; }
    public void setAccruedHours(BigDecimal accruedHours) { this.accruedHours = accruedHours; }
    public BigDecimal getMonetaryValue() { return monetaryValue; }
    public void setMonetaryValue(BigDecimal monetaryValue) { this.monetaryValue = monetaryValue; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
