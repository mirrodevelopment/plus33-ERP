/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : PayrollRun.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollRunController
 * Related Service   : PayrollRunService, PayrollRunServiceImpl
 * Related Repository: PayrollRunRepository
 * Related Entity    : PayrollRun
 * Related DTO       : N/A
 * Related Mapper    : PayrollRunMapper
 * Related DB Table  : payroll_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollRunRepository, PayrollRunMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payroll_runs'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollRun}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payroll_runs'.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_runs}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves payroll period id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPayrollPeriodId() { return payrollPeriodId; }
    /**
     * Performs the setPayrollPeriodId operation in this module.
     *
     * @param payrollPeriodId the payrollPeriodId input value
     */
    public void setPayrollPeriodId(Long payrollPeriodId) { this.payrollPeriodId = payrollPeriodId; }
    /**
     * Retrieves run number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRunNumber() { return runNumber; }
    /**
     * Performs the setRunNumber operation in this module.
     *
     * @param runNumber the runNumber input value
     */
    public void setRunNumber(String runNumber) { this.runNumber = runNumber; }
    /**
     * Retrieves payroll calendar type data from the database.
     *
     * @return the PayrollCalendarType result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public PayrollCalendarType getPayrollCalendarType() { return payrollCalendarType; }
    /**
     * Performs the setPayrollCalendarType operation in this module.
     *
     * @param payrollCalendarType the payrollCalendarType input value
     */
    public void setPayrollCalendarType(PayrollCalendarType payrollCalendarType) { this.payrollCalendarType = payrollCalendarType; }
    /**
     * Retrieves country code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCountryCode() { return countryCode; }
    /**
     * Performs the setCountryCode operation in this module.
     *
     * @param countryCode the countryCode input value
     */
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    /**
     * Retrieves run type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRunType() { return runType; }
    /**
     * Performs the setRunType operation in this module.
     *
     * @param runType the runType input value
     */
    public void setRunType(String runType) { this.runType = runType; }
    /**
     * Retrieves status data from the database.
     *
     * @return the PayrollRunStatus result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public PayrollRunStatus getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(PayrollRunStatus status) { this.status = status; }
    /**
     * Retrieves total gross data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalGross() { return totalGross; }
    /**
     * Performs the setTotalGross operation in this module.
     *
     * @param totalGross the totalGross input value
     */
    public void setTotalGross(BigDecimal totalGross) { this.totalGross = totalGross; }
    /**
     * Retrieves total net data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalNet() { return totalNet; }
    /**
     * Performs the setTotalNet operation in this module.
     *
     * @param totalNet the totalNet input value
     */
    public void setTotalNet(BigDecimal totalNet) { this.totalNet = totalNet; }
    /**
     * Retrieves total employer contributions data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalEmployerContributions() { return totalEmployerContributions; }
    /**
     * Performs the setTotalEmployerContributions operation in this module.
     *
     * @param totalEmployerContributions the totalEmployerContributions input value
     */
    public void setTotalEmployerContributions(BigDecimal totalEmployerContributions) { this.totalEmployerContributions = totalEmployerContributions; }
    /**
     * Retrieves total taxes data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalTaxes() { return totalTaxes; }
    /**
     * Performs the setTotalTaxes operation in this module.
     *
     * @param totalTaxes the totalTaxes input value
     */
    public void setTotalTaxes(BigDecimal totalTaxes) { this.totalTaxes = totalTaxes; }
    /**
     * Retrieves executed by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExecutedBy() { return executedBy; }
    /**
     * Performs the setExecutedBy operation in this module.
     *
     * @param executedBy the executedBy input value
     */
    public void setExecutedBy(String executedBy) { this.executedBy = executedBy; }
    /**
     * Retrieves approved by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getApprovedBy() { return approvedBy; }
    /**
     * Performs the setApprovedBy operation in this module.
     *
     * @param approvedBy the approvedBy input value
     */
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    /**
     * Retrieves posted at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getPostedAt() { return postedAt; }
    /**
     * Performs the setPostedAt operation in this module.
     *
     * @param postedAt the postedAt input value
     */
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }
    /**
     * Retrieves paid at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getPaidAt() { return paidAt; }
    /**
     * Performs the setPaidAt operation in this module.
     *
     * @param paidAt the paidAt input value
     */
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
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
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}