package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @Entity @Table(name = "employee_leave_transactions")
@NoArgsConstructor @AllArgsConstructor
public class EmployeeLeaveTransaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    /**
     * Transaction types:
     * ACCRUAL, OPENING, APPROVAL, CANCELLATION,
     * CARRY_FORWARD, ENCASHMENT, ADJUSTMENT, EXPIRY
     */
    @Column(name = "transaction_type", nullable = false, length = 30)
    private String transactionType;

    /** Positive = credit, Negative = debit */
    @Column(name = "days", nullable = false, precision = 5, scale = 2)
    private BigDecimal days;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_leave_id")
    private EmployeeLeave referenceLeave;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}
