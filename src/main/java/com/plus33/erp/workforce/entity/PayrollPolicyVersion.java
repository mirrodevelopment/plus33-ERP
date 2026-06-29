package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public Integer getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    public String getProrationRule() { return prorationRule; }
    public void setProrationRule(String prorationRule) { this.prorationRule = prorationRule; }
    public BigDecimal getOvertimeMultiplier() { return overtimeMultiplier; }
    public void setOvertimeMultiplier(BigDecimal overtimeMultiplier) { this.overtimeMultiplier = overtimeMultiplier; }
    public BigDecimal getHolidayPayMultiplier() { return holidayPayMultiplier; }
    public void setHolidayPayMultiplier(BigDecimal holidayPayMultiplier) { this.holidayPayMultiplier = holidayPayMultiplier; }
    public String getRoundingRule() { return roundingRule; }
    public void setRoundingRule(String roundingRule) { this.roundingRule = roundingRule; }
    public boolean isAllowNegativePayroll() { return allowNegativePayroll; }
    public void setAllowNegativePayroll(boolean allowNegativePayroll) { this.allowNegativePayroll = allowNegativePayroll; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
