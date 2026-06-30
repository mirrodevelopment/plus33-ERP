package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalTime;
import jakarta.validation.constraints.*;

public class CreateShiftRequest {
    @NotNull private Integer dayOfWeek;
    @NotBlank private String shiftName;
    @NotNull private LocalTime startTime;
    @NotNull private LocalTime endTime;
    private Integer breakMinutes = 0;
    @NotNull private BigDecimal availableHours;
    private String shiftType = "REGULAR"; // REGULAR, OVERTIME, SHUTDOWN

    public CreateShiftRequest() {}

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
