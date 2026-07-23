package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeLeaveBalanceHistory}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity mapped to {@code employee_leave_balance_history}.</p>
 *
 * <p>Stores yearly (or ad-hoc mid-year) snapshots of an employee's leave balance
 * per leave type. Records the full movement ledger: opening, earned, adjustments,
 * used, expired, carry-forward, encashed, and closing balance.</p>
 *
 * <p>Used for:
 * <ul>
 *   <li>HR audit trails and annual leave reports.</li>
 *   <li>Carry-forward calculations at year-end.</li>
 *   <li>Payroll verification of LOP deductions.</li>
 * </ul></p>
 *
 * <p><b>Database Table:</b> {@code employee_leave_balance_history}</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(
    name = "employee_leave_balance_history",
    uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "leave_type_id", "year", "snapshot_date"})
)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLeaveBalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    /** Calendar year this snapshot covers (e.g. 2025). */
    @Column(name = "year", nullable = false)
    private Integer year;

    /** Date the snapshot was taken. Multiple snapshots per year are allowed (audit trail). */
    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate = LocalDate.now();

    // ─── Balance Components ───────────────────────────────────────────────────

    /** Balance at start of the year (including prior year carry-forward). */
    @Column(name = "opening", nullable = false, precision = 7, scale = 2)
    private BigDecimal opening = BigDecimal.ZERO;

    /** Total days accrued/earned during the year. */
    @Column(name = "earned", nullable = false, precision = 7, scale = 2)
    private BigDecimal earned = BigDecimal.ZERO;

    /** Manual HR adjustments (credits or debits). */
    @Column(name = "manual_adjustments", nullable = false, precision = 7, scale = 2)
    private BigDecimal manualAdjustments = BigDecimal.ZERO;

    /** Total days of approved leave taken during the year. */
    @Column(name = "used", nullable = false, precision = 7, scale = 2)
    private BigDecimal used = BigDecimal.ZERO;

    /** Leave days currently in PENDING approval (not yet deducted from balance). */
    @Column(name = "pending", nullable = false, precision = 7, scale = 2)
    private BigDecimal pending = BigDecimal.ZERO;

    /** Carry-forward days that expired during the year (use-or-lose). */
    @Column(name = "expired", nullable = false, precision = 7, scale = 2)
    private BigDecimal expired = BigDecimal.ZERO;

    /** Days carried forward to the following year. */
    @Column(name = "carry_forward", nullable = false, precision = 7, scale = 2)
    private BigDecimal carryForward = BigDecimal.ZERO;

    /** Days encashed for monetary payout during the year. */
    @Column(name = "encashed", nullable = false, precision = 7, scale = 2)
    private BigDecimal encashed = BigDecimal.ZERO;

    /**
     * Closing balance at snapshot date.
     * Formula: opening + earned + manualAdjustments - used - expired - encashed
     */
    @Column(name = "closing", nullable = false, precision = 7, scale = 2)
    private BigDecimal closing = BigDecimal.ZERO;

    /** Optional notes (e.g. "Year-end snapshot", "Carry-forward expiry run"). */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    /**
     * Recalculates the closing balance from component fields.
     * Call before persisting any snapshot record.
     */
    public void recalculateClosing() {
        this.closing = opening
                .add(earned)
                .add(manualAdjustments)
                .subtract(used)
                .subtract(expired)
                .subtract(encashed);
    }
}
