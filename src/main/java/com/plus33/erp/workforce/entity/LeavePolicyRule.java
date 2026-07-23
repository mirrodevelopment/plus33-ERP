package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code LeavePolicyRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity mapped to {@code leave_policy_rules}.</p>
 *
 * <p>Replaces the legacy {@code CountryLeavePolicy} with a fully normalized,
 * policy-group-based model. Each row defines the configurable parameters for
 * one leave type within one policy group (INDIA, EU, or UAE). No leave-related
 * logic should be hardcoded in service classes — all values come from here.</p>
 *
 * <p><b>Key improvements over CountryLeavePolicy:</b>
 * <ul>
 *   <li>Uses {@link LeavePolicyGroup} (INDIA/EU/UAE) instead of raw country codes.</li>
 *   <li>Replaces {@code requiresDocument} boolean with {@code documentRequiredAfterDays} integer.</li>
 *   <li>Replaces {@code isProtected} boolean with {@code approvalMode} enum string.</li>
 *   <li>Adds carry-forward, encashment, half-day, and minimum leave unit fields.</li>
 *   <li>Adds {@code lifetimeLimit} for once-per-employment leaves (e.g. Marriage).</li>
 *   <li>Linked to tiered {@link LeavePayRule}s for partial/unpaid pay brackets.</li>
 * </ul></p>
 *
 * <p><b>Database Table:</b> {@code leave_policy_rules}</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(
    name = "leave_policy_rules",
    uniqueConstraints = @UniqueConstraint(columnNames = {"policy_group_id", "leave_type_id", "version"})
)
@NoArgsConstructor
@AllArgsConstructor
public class LeavePolicyRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The policy group this rule belongs to (INDIA, EU, UAE). */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_group_id", nullable = false)
    private LeavePolicyGroup policyGroup;

    /** The leave type this rule configures. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    // ─── Core Entitlement ────────────────────────────────────────────────────

    /**
     * Total entitlement per year. {@code null} = unlimited (e.g. EU Sick Leave).
     */
    @Column(name = "default_entitlement", precision = 7, scale = 2)
    private BigDecimal defaultEntitlement;

    /**
     * Monthly accrual amount. {@code null} = lump sum granted at period start.
     * Example: Annual Leave → 1.5 (India), 2.5 (EU/UAE).
     */
    @Column(name = "monthly_accrual", precision = 5, scale = 2)
    private BigDecimal monthlyAccrual;

    /**
     * Unit of the entitlement: WORKING_DAYS | CALENDAR_DAYS | WEEKS.
     * UAE Annual Leave and Sick Leave use CALENDAR_DAYS.
     */
    @Column(name = "entitlement_unit", nullable = false, length = 20)
    private String entitlementUnit = "WORKING_DAYS";

    // ─── Day Constraints ─────────────────────────────────────────────────────

    /** Maximum consecutive days per single leave request. {@code null} = no limit. */
    @Column(name = "max_consecutive_days")
    private Integer maxConsecutiveDays;

    /** Hard cap on total days per calendar year. {@code null} = no cap (e.g. EU Sick). */
    @Column(name = "max_per_year")
    private Integer maxPerYear;

    /** Minimum notice days required before leave start date. Default 0 = same-day allowed. */
    @Column(name = "min_notice_days", nullable = false)
    private Integer minNoticeDays = 0;

    // ─── Document Requirements ────────────────────────────────────────────────

    /**
     * Number of consecutive leave days after which a supporting document is mandatory.
     * {@code 0} = no document ever required.
     * {@code 2} = doctor certificate required for Sick Leave > 2 consecutive days.
     */
    @Column(name = "document_required_after_days", nullable = false)
    private Integer documentRequiredAfterDays = 0;

    // ─── Half-Day & Minimum Unit ──────────────────────────────────────────────

    /** Whether half-day leave is allowed for this leave type. */
    @Column(name = "allow_half_day", nullable = false)
    private Boolean allowHalfDay = true;

    /**
     * Minimum leave unit per request: 0.5 (half-day) or 1.0 (full-day).
     */
    @Column(name = "minimum_leave_unit", nullable = false, precision = 3, scale = 2)
    private BigDecimal minimumLeaveUnit = BigDecimal.ONE;

    // ─── Carry Forward ────────────────────────────────────────────────────────

    @Column(name = "carry_forward_allowed", nullable = false)
    private Boolean carryForwardAllowed = false;

    /** Maximum days that can be rolled to next year. {@code null} = unlimited if carry-forward is allowed. */
    @Column(name = "carry_forward_limit", precision = 5, scale = 2)
    private BigDecimal carryForwardLimit;

    /** Months after year-end before carry-forward days expire. {@code null} = never expires. */
    @Column(name = "carry_forward_expiry_months")
    private Integer carryForwardExpiryMonths;

    // ─── Encashment ───────────────────────────────────────────────────────────

    @Column(name = "encashment_allowed", nullable = false)
    private Boolean encashmentAllowed = false;

    @Column(name = "maximum_encashment_days", precision = 5, scale = 2)
    private BigDecimal maximumEncashmentDays;

    /** Minimum remaining balance employee must retain after encashment. */
    @Column(name = "minimum_balance_for_encashment", precision = 5, scale = 2)
    private BigDecimal minimumBalanceForEncashment;

    // ─── Approval Workflow ────────────────────────────────────────────────────

    /**
     * Enum-like string controlling who must approve this leave type.
     * Values: SHIFT_SUPERVISOR | STORE_ADMIN | REGIONAL_ADMIN | HR | AUTO_APPROVED
     */
    @Column(name = "approval_level", nullable = false, length = 30)
    private String approvalLevel = "SHIFT_SUPERVISOR";

    /**
     * Workflow mode enum string.
     * Values: MANAGER_APPROVAL | HR_APPROVAL | AUTO_APPROVE | SYSTEM_APPROVAL
     * SYSTEM_APPROVAL is set automatically for protected leave types.
     */
    @Column(name = "approval_mode", nullable = false, length = 30)
    private String approvalMode = "MANAGER_APPROVAL";

    // ─── Financial Treatment ──────────────────────────────────────────────────

    /** TRUE = full salary during leave; FALSE = unpaid. */
    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = true;

    /**
     * TRUE = leave cannot be rejected by a manager. Automatically triggers
     * SYSTEM_APPROVAL workflow. Applies to Maternity, Paternity, Bereavement, etc.
     */
    @Column(name = "is_protected", nullable = false)
    private Boolean isProtected = false;

    // ─── Lifetime Restriction ────────────────────────────────────────────────

    /**
     * Maximum number of times this leave type can be approved over an employee's
     * entire tenure. {@code null} = no lifetime restriction.
     * Example: Marriage Leave = 1 (once per employment).
     */
    @Column(name = "lifetime_limit")
    private Integer lifetimeLimit;

    // ─── Tiered Pay Rules ────────────────────────────────────────────────────

    /**
     * Tiered salary brackets for this policy rule.
     * Used by the payroll engine to determine pay percentage for each day of leave.
     * Primarily relevant for UAE Sick Leave (100%/50%/0% tiers).
     */
    @OneToMany(mappedBy = "policyRule", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("day_from ASC")
    private List<LeavePayRule> payRules = new ArrayList<>();

    // ─── Versioning & Validity ────────────────────────────────────────────────

    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom = LocalDate.of(2025, 1, 1);

    /** {@code null} = currently active with no end date. */
    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    // ─── Convenience helpers ─────────────────────────────────────────────────

    /** Returns true if a document is required for the given number of consecutive days. */
    public boolean isDocumentRequired(int consecutiveDays) {
        return documentRequiredAfterDays > 0 && consecutiveDays > documentRequiredAfterDays;
    }

    /** Returns true if this rule uses auto/system approval (protected leave or auto-approve mode). */
    public boolean isAutoApproved() {
        return "AUTO_APPROVE".equals(approvalMode) || "SYSTEM_APPROVAL".equals(approvalMode) || Boolean.TRUE.equals(isProtected);
    }
}
