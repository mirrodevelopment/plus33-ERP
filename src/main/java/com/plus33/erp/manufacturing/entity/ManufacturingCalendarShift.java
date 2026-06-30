package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "manufacturing_calendar_shifts")
public class ManufacturingCalendarShift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private ManufacturingCalendar calendar;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek; // 1=Monday .. 7=Sunday

    @Column(name = "shift_name", nullable = false, length = 50)
    private String shiftName;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "break_minutes", nullable = false)
    private Integer breakMinutes = 0;

    @Column(name = "available_hours", nullable = false, precision = 6, scale = 2)
    private BigDecimal availableHours = new BigDecimal("8.00");

    @Column(name = "shift_type", nullable = false, length = 20)
    private String shiftType = "REGULAR"; // REGULAR, OVERTIME, SHUTDOWN

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ManufacturingCalendarShift() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ManufacturingCalendar getCalendar() { return calendar; }
    public void setCalendar(ManufacturingCalendar calendar) { this.calendar = calendar; }
    public Integer getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getShiftName() { return shiftName; }
    public void setShiftName(String shiftName) { this.shiftName = shiftName; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public Integer getBreakMinutes() { return breakMinutes; }
    public void setBreakMinutes(Integer breakMinutes) { this.breakMinutes = breakMinutes; }
    public BigDecimal getAvailableHours() { return availableHours; }
    public void setAvailableHours(BigDecimal availableHours) { this.availableHours = availableHours; }
    public String getShiftType() { return shiftType; }
    public void setShiftType(String shiftType) { this.shiftType = shiftType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
