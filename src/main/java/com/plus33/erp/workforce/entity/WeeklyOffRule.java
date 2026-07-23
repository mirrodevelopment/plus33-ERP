package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code WeeklyOffRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity mapped to {@code weekly_off_rules}.</p>
 *
 * <p>Stores the weekly off days per policy group. The {@link WorkingDayCalculatorService}
 * reads this table instead of hardcoding country-specific weekend logic.</p>
 *
 * <p>Examples:
 * <ul>
 *   <li>INDIA → SUNDAY</li>
 *   <li>UAE   → SUNDAY</li>
 *   <li>EU    → SATURDAY, SUNDAY</li>
 * </ul></p>
 *
 * <p><b>Database Table:</b> {@code weekly_off_rules}</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(
    name = "weekly_off_rules",
    uniqueConstraints = @UniqueConstraint(columnNames = {"policy_group_id", "day_of_week"})
)
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyOffRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_group_id", nullable = false)
    private LeavePolicyGroup policyGroup;

    /**
     * ISO day-of-week name: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY.
     * Stored as VARCHAR — compared with {@code java.time.DayOfWeek.name()}.
     */
    @Column(name = "day_of_week", nullable = false, length = 15)
    private String dayOfWeek;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }
}
