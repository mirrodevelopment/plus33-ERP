package com.plus33.erp.manufacturing.dto;

import java.util.List;

public class CalendarDto {
    private Long id;
    private Long companyId;
    private String calendarType;
    private String referenceType;
    private Long referenceId;
    private String code;
    private String name;
    private String timezone;
    private Boolean active;
    private List<ShiftDto> shifts;
    private List<CalendarExceptionDto> exceptions;

    public CalendarDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCalendarType() { return calendarType; }
    public void setCalendarType(String calendarType) { this.calendarType = calendarType; }
    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public List<ShiftDto> getShifts() { return shifts; }
    public void setShifts(List<ShiftDto> shifts) { this.shifts = shifts; }
    public List<CalendarExceptionDto> getExceptions() { return exceptions; }
    public void setExceptions(List<CalendarExceptionDto> exceptions) { this.exceptions = exceptions; }
}
