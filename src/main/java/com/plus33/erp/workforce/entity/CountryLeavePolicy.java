package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @Entity @Table(name = "country_leave_policies")
@NoArgsConstructor @AllArgsConstructor
public class CountryLeavePolicy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "annual_entitlement", precision = 5, scale = 2)
    private BigDecimal annualEntitlement;

    @Column(name = "monthly_accrual", precision = 5, scale = 2)
    private BigDecimal monthlyAccrual;

    @Column(name = "max_consecutive_days")
    private Integer maxConsecutiveDays;

    @Column(name = "carry_forward_max", precision = 5, scale = 2)
    private BigDecimal carryForwardMax = BigDecimal.ZERO;

    @Column(name = "min_notice_days")
    private Integer minNoticeDays = 0;

    @Column(name = "requires_document")
    private Boolean requiresDocument = false;

    @Column(name = "approval_level", length = 30)
    private String approvalLevel = "SUPERVISOR";

    @Column(name = "is_paid")
    private Boolean isPaid = true;

    @Column(name = "is_protected")
    private Boolean isProtected = false;

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
