package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @Entity @Table(name = "employee_leave_balances")
@NoArgsConstructor @AllArgsConstructor
public class EmployeeLeaveBalance {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "opening_balance", precision = 5, scale = 2)
    private BigDecimal openingBalance = BigDecimal.ZERO;

    @Column(name = "accrued", precision = 5, scale = 2)
    private BigDecimal accrued = BigDecimal.ZERO;

    @Column(name = "carry_forward", precision = 5, scale = 2)
    private BigDecimal carryForward = BigDecimal.ZERO;

    @Column(name = "used", precision = 5, scale = 2)
    private BigDecimal used = BigDecimal.ZERO;

    @Column(name = "pending", precision = 5, scale = 2)
    private BigDecimal pending = BigDecimal.ZERO;

    @Column(name = "encashed_days", precision = 5, scale = 2)
    private BigDecimal encashedDays = BigDecimal.ZERO;

    @Column(name = "encashed_amount", precision = 10, scale = 2)
    private BigDecimal encashedAmount = BigDecimal.ZERO;

    @Column(name = "encashment_date")
    private LocalDate encashmentDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate  protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    /** Computed: opening + accrued + carryForward - used - pending - encashedDays */
    @Transient
    public BigDecimal getRemaining() {
        return openingBalance.add(accrued).add(carryForward)
                .subtract(used).subtract(pending).subtract(encashedDays);
    }
}
