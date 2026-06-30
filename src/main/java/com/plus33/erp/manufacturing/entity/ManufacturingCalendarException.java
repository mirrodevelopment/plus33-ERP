package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "manufacturing_calendar_exceptions")
public class ManufacturingCalendarException {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private ManufacturingCalendar calendar;

    @Column(name = "exception_date", nullable = false)
    private LocalDate exceptionDate;

    @Column(name = "exception_type", nullable = false, length = 30)
    private String exceptionType; // HOLIDAY, MAINTENANCE_WINDOW, OVERTIME, SHUTDOWN

    @Column(name = "available_hours", nullable = false, precision = 6, scale = 2)
    private BigDecimal availableHours = BigDecimal.ZERO;

    @Column(length = 255)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ManufacturingCalendarException() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ManufacturingCalendar getCalendar() { return calendar; }
    public void setCalendar(ManufacturingCalendar calendar) { this.calendar = calendar; }
    public LocalDate getExceptionDate() { return exceptionDate; }
    public void setExceptionDate(LocalDate exceptionDate) { this.exceptionDate = exceptionDate; }
    public String getExceptionType() { return exceptionType; }
    public void setExceptionType(String exceptionType) { this.exceptionType = exceptionType; }
    public BigDecimal getAvailableHours() { return availableHours; }
    public void setAvailableHours(BigDecimal availableHours) { this.availableHours = availableHours; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
