package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CalendarExceptionDto {
    private Long id;
    private Long calendarId;
    private LocalDate exceptionDate;
    private String exceptionType;
    private BigDecimal availableHours;
    private String description;

    public CalendarExceptionDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCalendarId() { return calendarId; }
    public void setCalendarId(Long calendarId) { this.calendarId = calendarId; }
    public LocalDate getExceptionDate() { return exceptionDate; }
    public void setExceptionDate(LocalDate exceptionDate) { this.exceptionDate = exceptionDate; }
    public String getExceptionType() { return exceptionType; }
    public void setExceptionType(String exceptionType) { this.exceptionType = exceptionType; }
    public BigDecimal getAvailableHours() { return availableHours; }
    public void setAvailableHours(BigDecimal availableHours) { this.availableHours = availableHours; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
