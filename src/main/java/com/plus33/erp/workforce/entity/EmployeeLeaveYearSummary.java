package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @Entity @Table(name = "employee_leave_year_summary")
@NoArgsConstructor @AllArgsConstructor
public class EmployeeLeaveYearSummary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "annual_entitlement", precision = 5, scale = 2)
    private BigDecimal annualEntitlement = BigDecimal.ZERO;

    @Column(name = "carry_forward", precision = 5, scale = 2)
    private BigDecimal carryForward = BigDecimal.ZERO;

    @Column(name = "accrued", precision = 5, scale = 2)
    private BigDecimal accrued = BigDecimal.ZERO;

    @Column(name = "used", precision = 5, scale = 2)
    private BigDecimal used = BigDecimal.ZERO;

    @Column(name = "pending", precision = 5, scale = 2)
    private BigDecimal pending = BigDecimal.ZERO;

    @Column(name = "encashed", precision = 5, scale = 2)
    private BigDecimal encashed = BigDecimal.ZERO;

    @Column(name = "remaining", precision = 5, scale = 2)
    private BigDecimal remaining = BigDecimal.ZERO;

    @Column(name = "snapshot_date")
    private LocalDate snapshotDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}
