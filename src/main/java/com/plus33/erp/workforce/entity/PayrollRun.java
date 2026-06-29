package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll_runs")
public class PayrollRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "payroll_period_id")
    private Long payrollPeriodId;

    @Column(name = "run_number", nullable = false, length = 100)
    private String runNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "payroll_calendar_type", nullable = false)
    private PayrollCalendarType payrollCalendarType = PayrollCalendarType.MONTHLY;

    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode = "US";

    @Column(name = "run_type", nullable = false, length = 50)
    private String runType = "REGULAR";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayrollRunStatus status = PayrollRunStatus.DRAFT;

    @Column(name = "total_gross", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalGross = BigDecimal.ZERO;

    @Column(name = "total_net", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalNet = BigDecimal.ZERO;

    @Column(name = "total_employer_contributions", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalEmployerContributions = BigDecimal.ZERO;

    @Column(name = "total_taxes", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalTaxes = BigDecimal.ZERO;

    @Column(name = "executed_by")
    private String executedBy;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public PayrollRun() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getPayrollPeriodId() { return payrollPeriodId; }
    public void setPayrollPeriodId(Long payrollPeriodId) { this.payrollPeriodId = payrollPeriodId; }
    public String getRunNumber() { return runNumber; }
    public void setRunNumber(String runNumber) { this.runNumber = runNumber; }
    public PayrollCalendarType getPayrollCalendarType() { return payrollCalendarType; }
    public void setPayrollCalendarType(PayrollCalendarType payrollCalendarType) { this.payrollCalendarType = payrollCalendarType; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getRunType() { return runType; }
    public void setRunType(String runType) { this.runType = runType; }
    public PayrollRunStatus getStatus() { return status; }
    public void setStatus(PayrollRunStatus status) { this.status = status; }
    public BigDecimal getTotalGross() { return totalGross; }
    public void setTotalGross(BigDecimal totalGross) { this.totalGross = totalGross; }
    public BigDecimal getTotalNet() { return totalNet; }
    public void setTotalNet(BigDecimal totalNet) { this.totalNet = totalNet; }
    public BigDecimal getTotalEmployerContributions() { return totalEmployerContributions; }
    public void setTotalEmployerContributions(BigDecimal totalEmployerContributions) { this.totalEmployerContributions = totalEmployerContributions; }
    public BigDecimal getTotalTaxes() { return totalTaxes; }
    public void setTotalTaxes(BigDecimal totalTaxes) { this.totalTaxes = totalTaxes; }
    public String getExecutedBy() { return executedBy; }
    public void setExecutedBy(String executedBy) { this.executedBy = executedBy; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getPostedAt() { return postedAt; }
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
