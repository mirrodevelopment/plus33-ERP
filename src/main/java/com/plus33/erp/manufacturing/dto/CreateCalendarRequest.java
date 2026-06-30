package com.plus33.erp.manufacturing.dto;

import jakarta.validation.constraints.*;

public class CreateCalendarRequest {
    @NotNull private Long companyId;
    @NotBlank private String calendarType; // PLANT, WORK_CENTER, MACHINE, SHIFT, HOLIDAY, OVERTIME
    private String referenceType;
    private Long referenceId;
    @NotBlank private String code;
    @NotBlank private String name;
    private String timezone = "UTC";

    public CreateCalendarRequest() {}

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
}
