package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "country_work_policies")
@NoArgsConstructor
@AllArgsConstructor
public class CountryWorkPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false, unique = true, length = 10)
    private String countryCode;

    @Column(name = "weekly_required_hours", nullable = false)
    private BigDecimal weeklyRequiredHours = BigDecimal.valueOf(35.00);

    @Column(name = "overtime_threshold_hours", nullable = false)
    private BigDecimal overtimeThresholdHours = BigDecimal.valueOf(35.00);

    @Column(name = "overtime_rate", nullable = false)
    private BigDecimal overtimeRate = BigDecimal.valueOf(1.25);

    @Column(name = "night_start", nullable = false)
    private LocalTime nightStart = LocalTime.of(21, 0);

    @Column(name = "night_end", nullable = false)
    private LocalTime nightEnd = LocalTime.of(6, 0);

    @Column(name = "night_overtime_rate", nullable = false)
    private BigDecimal nightOvertimeRate = BigDecimal.valueOf(1.50);

    @Column(name = "holiday_overtime_rate", nullable = false)
    private BigDecimal holidayOvertimeRate = BigDecimal.valueOf(2.00);

    @Column(name = "grace_period_minutes", nullable = false)
    private Integer gracePeriodMinutes = 15;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
