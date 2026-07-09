package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @Entity @Table(name = "company_leave_policy_overrides")
@NoArgsConstructor @AllArgsConstructor
public class CompanyLeavePolicyOverride {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "country_code", length = 10)
    private String countryCode = "FR";

    @Column(name = "annual_entitlement", precision = 5, scale = 2)
    private BigDecimal annualEntitlement;

    @Column(name = "monthly_accrual", precision = 5, scale = 2)
    private BigDecimal monthlyAccrual;

    @Column(name = "max_consecutive_days")
    private Integer maxConsecutiveDays;

    @Column(name = "carry_forward_max", precision = 5, scale = 2)
    private BigDecimal carryForwardMax;

    @Column(name = "min_notice_days")
    private Integer minNoticeDays;

    @Column(name = "version")
    private Integer version = 1;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate  protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
