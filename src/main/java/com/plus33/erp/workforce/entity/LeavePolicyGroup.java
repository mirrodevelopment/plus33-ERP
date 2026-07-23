package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code LeavePolicyGroup}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity mapped to {@code leave_policy_groups}.</p>
 *
 * <p>Represents a named leave policy group (INDIA, EU, UAE). Companies and stores
 * reference a policy group. All policy rules and working-week configurations
 * are stored per group — no country-code hardcoding in service logic.</p>
 *
 * <p><b>Database Table:</b> {@code leave_policy_groups}</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "leave_policy_groups")
@NoArgsConstructor
@AllArgsConstructor
public class LeavePolicyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique code used for lookup: INDIA | EU | UAE */
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Comma-separated ISO day names for working days.
     * Example: "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY" (India/UAE)
     *          "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY" (EU)
     */
    @Column(name = "working_days", nullable = false, length = 100)
    private String workingDays = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY";

    /** Standard contracted hours per working day */
    @Column(name = "hours_per_day", precision = 4, scale = 2)
    private BigDecimal hoursPerDay = new BigDecimal("8.00");

    /** Standard contracted hours per week */
    @Column(name = "hours_per_week", precision = 5, scale = 2)
    private BigDecimal hoursPerWeek = new BigDecimal("40.00");

    @Column(nullable = false)
    private Boolean active = true;

    /** Weekly off rules (SUNDAY for India/UAE; SATURDAY+SUNDAY for EU) */
    @JsonIgnore
    @OneToMany(mappedBy = "policyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeeklyOffRule> weeklyOffRules = new ArrayList<>();

    /** Leave policy rules configured for this group */
    @JsonIgnore
    @OneToMany(mappedBy = "policyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeavePolicyRule> policyRules = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
