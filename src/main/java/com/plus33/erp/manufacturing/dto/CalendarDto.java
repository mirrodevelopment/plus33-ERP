/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : CalendarDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CalendarDtoController
 * Related Service   : CalendarDtoService, CalendarDtoServiceImpl
 * Related Repository: CalendarDtoRepository
 * Related Entity    : CalendarDto
 * Related DTO       : CalendarDto, CalendarExceptionDto, ShiftDto
 * Related Mapper    : CalendarDtoMapper
 * Related DB Table  : calendar_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CalendarDtoController, CalendarDtoService, CalendarDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
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

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
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
    /**
     * Retrieves active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(Boolean active) { this.active = active; }
    /**
     * Retrieves shifts data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<ShiftDto> getShifts() { return shifts; }
    /**
     * Performs the setShifts operation in this module.
     *
     * @param shifts the shifts input value
     */
    public void setShifts(List<ShiftDto> shifts) { this.shifts = shifts; }
    /**
     * Retrieves exceptions data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<CalendarExceptionDto> getExceptions() { return exceptions; }
    /**
     * Performs the setExceptions operation in this module.
     *
     * @param exceptions the exceptions input value
     */
    public void setExceptions(List<CalendarExceptionDto> exceptions) { this.exceptions = exceptions; }
}
