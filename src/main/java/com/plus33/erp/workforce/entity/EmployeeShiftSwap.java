package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "employee_shift_swaps")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeShiftSwap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /** The date the employee is currently scheduled (shift to be vacated). */
    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_shift_id", nullable = false)
    private Shift currentShift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preferred_shift_id", nullable = false)
    private Shift preferredShift;

    /** Employee's preferred date to work the new shift. Must be within the next 7 days from today. */
    @Column(name = "preferred_date")
    private LocalDate preferredDate;

    /** Final date approved by supervisor. May differ from preferredDate. Set on approval. */
    @Column(name = "approved_date")
    private LocalDate approvedDate;

    /** Final shift approved by supervisor. May differ from preferredShift. Set on approval. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_shift_id")
    private Shift approvedShift;

    /**
     * Employee assigned by supervisor to fill the vacated slot (shiftDate + currentShift).
     * Optional — supervisor may leave this null if no replacement is available.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replacement_employee_id")
    private Employee replacementEmployee;

    @Builder.Default
    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, ESCALATED, REJECTED_BY_ADMIN

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "admin_rejection_reason")
    private String adminRejectionReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
