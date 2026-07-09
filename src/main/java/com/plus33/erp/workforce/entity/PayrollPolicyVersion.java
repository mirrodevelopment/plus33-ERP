/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : PayrollPolicyVersion.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollPolicyVersionController
 * Related Service   : PayrollPolicyVersionService, PayrollPolicyVersionServiceImpl
 * Related Repository: PayrollPolicyVersionRepository
 * Related Entity    : PayrollPolicyVersion
 * Related DTO       : N/A
 * Related Mapper    : PayrollPolicyVersionMapper
 * Related DB Table  : payroll_policy_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollPolicyVersionRepository, PayrollPolicyVersionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payroll_policy_versions'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollPolicyVersion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payroll_policy_versions'.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_policy_versions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "payroll_policy_versions")
public class PayrollPolicyVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_id", nullable = false)
    private Long policyId;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "proration_rule", nullable = false)
    private String prorationRule = "CALENDAR_DAYS";

    @Column(name = "overtime_multiplier", nullable = false)
    private BigDecimal overtimeMultiplier = new BigDecimal("1.50");

    @Column(name = "holiday_pay_multiplier", nullable = false)
    private BigDecimal holidayPayMultiplier = new BigDecimal("2.00");

    @Column(name = "rounding_rule", nullable = false)
    private String roundingRule = "HALF_EVEN";

    @Column(name = "allow_negative_payroll", nullable = false)
    private boolean allowNegativePayroll = false;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public PayrollPolicyVersion() {}

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
     * Retrieves policy id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPolicyId() { return policyId; }
    /**
     * Performs the setPolicyId operation in this module.
     *
     * @param policyId the policyId input value
     */
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    /**
     * Retrieves version number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersionNumber() { return versionNumber; }
    /**
     * Performs the setVersionNumber operation in this module.
     *
     * @param versionNumber the versionNumber input value
     */
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }
    /**
     * Retrieves effective from data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    /**
     * Performs the setEffectiveFrom operation in this module.
     *
     * @param effectiveFrom the effectiveFrom input value
     */
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    /**
     * Retrieves effective to data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveTo() { return effectiveTo; }
    /**
     * Performs the setEffectiveTo operation in this module.
     *
     * @param effectiveTo the effectiveTo input value
     */
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    /**
     * Retrieves proration rule data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProrationRule() { return prorationRule; }
    /**
     * Performs the setProrationRule operation in this module.
     *
     * @param prorationRule the prorationRule input value
     */
    public void setProrationRule(String prorationRule) { this.prorationRule = prorationRule; }
    /**
     * Retrieves overtime multiplier data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getOvertimeMultiplier() { return overtimeMultiplier; }
    /**
     * Performs the setOvertimeMultiplier operation in this module.
     *
     * @param overtimeMultiplier the overtimeMultiplier input value
     */
    public void setOvertimeMultiplier(BigDecimal overtimeMultiplier) { this.overtimeMultiplier = overtimeMultiplier; }
    /**
     * Retrieves holiday pay multiplier data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getHolidayPayMultiplier() { return holidayPayMultiplier; }
    /**
     * Performs the setHolidayPayMultiplier operation in this module.
     *
     * @param holidayPayMultiplier the holidayPayMultiplier input value
     */
    public void setHolidayPayMultiplier(BigDecimal holidayPayMultiplier) { this.holidayPayMultiplier = holidayPayMultiplier; }
    /**
     * Retrieves rounding rule data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRoundingRule() { return roundingRule; }
    /**
     * Performs the setRoundingRule operation in this module.
     *
     * @param roundingRule the roundingRule input value
     */
    public void setRoundingRule(String roundingRule) { this.roundingRule = roundingRule; }
    /**
     * Performs the isAllowNegativePayroll operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isAllowNegativePayroll() { return allowNegativePayroll; }
    /**
     * Performs the setAllowNegativePayroll operation in this module.
     *
     * @param allowNegativePayroll the allowNegativePayroll input value
     */
    public void setAllowNegativePayroll(boolean allowNegativePayroll) { this.allowNegativePayroll = allowNegativePayroll; }
    /**
     * Performs the isActive operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(boolean active) { this.active = active; }
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