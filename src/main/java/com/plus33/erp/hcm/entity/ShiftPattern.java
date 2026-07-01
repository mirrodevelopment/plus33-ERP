package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hcm_shift_patterns")
public class ShiftPattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "weekly_hours", nullable = false)
    private BigDecimal weeklyHours = new BigDecimal("40.00");

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getWeeklyHours() { return weeklyHours; }
    public void setWeeklyHours(BigDecimal weeklyHours) { this.weeklyHours = weeklyHours; }
}
