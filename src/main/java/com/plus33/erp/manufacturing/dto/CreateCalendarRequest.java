/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : CreateCalendarRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreateCalendarController
 * Related Service   : CreateCalendarService, CreateCalendarServiceImpl
 * Related Repository: CreateCalendarRepository
 * Related Entity    : CreateCalendar
 * Related DTO       : CreateCalendarRequest
 * Related Mapper    : CreateCalendarMapper
 * Related DB Table  : create_calendars
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreateCalendarController, CreateCalendarService, CreateCalendarServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
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

    /**
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves calendar type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCalendarType() { return calendarType; }
    /**
     * Performs the setCalendarType operation in this module.
     *
     * @param calendarType the calendarType input value
     */
    public void setCalendarType(String calendarType) { this.calendarType = calendarType; }
    /**
     * Retrieves reference type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReferenceType() { return referenceType; }
    /**
     * Performs the setReferenceType operation in this module.
     *
     * @param referenceType the referenceType input value
     */
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    /**
     * Retrieves reference id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReferenceId() { return referenceId; }
    /**
     * Performs the setReferenceId operation in this module.
     *
     * @param referenceId the referenceId input value
     */
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    /**
     * Retrieves code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCode() { return code; }
    /**
     * Performs the setCode operation in this module.
     *
     * @param code the code input value
     */
    public void setCode(String code) { this.code = code; }
    /**
     * Retrieves name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getName() { return name; }
    /**
     * Performs the setName operation in this module.
     *
     * @param name the name input value
     */
    public void setName(String name) { this.name = name; }
    /**
     * Retrieves timezone data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTimezone() { return timezone; }
    /**
     * Performs the setTimezone operation in this module.
     *
     * @param timezone the timezone input value
     */
    public void setTimezone(String timezone) { this.timezone = timezone; }
}
