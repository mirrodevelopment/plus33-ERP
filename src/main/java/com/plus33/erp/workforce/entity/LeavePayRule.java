package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code LeavePayRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity mapped to {@code leave_pay_rules}.</p>
 *
 * <p>Stores tiered pay percentage brackets for a leave policy rule.
 * The payroll engine reads these tiers to compute partial salary deductions
 * without any hardcoded logic. Multiple rows per policy rule define
 * the full bracket set.</p>
 *
 * <p>Example — UAE Sick Leave:
 * <pre>
 *   day_from  day_to  pay_percentage
 *   1         15      100.00   (Full Pay)
 *   16        45       50.00   (Half Pay)
 *   46        90        0.00   (Unpaid)
 * </pre></p>
 *
 * <p>If no pay rules exist for a policy rule, the payroll engine defaults to
 * the {@code is_paid} flag: 100% if paid, 0% if unpaid.</p>
 *
 * <p><b>Database Table:</b> {@code leave_pay_rules}</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "leave_pay_rules")
@NoArgsConstructor
@AllArgsConstructor
public class LeavePayRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The parent policy rule this tier belongs to. */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_rule_id", nullable = false)
    private LeavePolicyRule policyRule;

    /** Human-readable tier label: "Full Pay", "Half Pay", "Unpaid". */
    @Column(name = "tier_label", length = 50)
    private String tierLabel;

    /**
     * 1-based inclusive start day of this pay tier.
     * Day 1 = first day of the leave instance.
     */
    @Column(name = "day_from", nullable = false)
    private Integer dayFrom;

    /**
     * 1-based inclusive end day of this pay tier.
     * {@code null} = open-ended (applies to all remaining days).
     */
    @Column(name = "day_to")
    private Integer dayTo;

    /**
     * Salary percentage paid during this tier.
     * Range: 0.00 (unpaid) – 100.00 (full pay).
     */
    @Column(name = "pay_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal payPercentage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    /**
     * Returns {@code true} if the given 1-based day index falls within this tier.
     *
     * @param dayIndex 1-based day number from the start of the leave
     */
    public boolean coversDay(int dayIndex) {
        if (dayIndex < dayFrom) return false;
        return dayTo == null || dayIndex <= dayTo;
    }
}
