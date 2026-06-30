package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalTime;

public class ShiftDto {
    private Long id;
    private Long calendarId;
    private Integer dayOfWeek;
    private String shiftName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer breakMinutes;
    private BigDecimal availableHours;
    private String shiftType;

    public ShiftDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCalendarId() { return calendarId; }
    public void setCalendarId(Long calendarId) { this.calendarId = calendarId; }
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
}
