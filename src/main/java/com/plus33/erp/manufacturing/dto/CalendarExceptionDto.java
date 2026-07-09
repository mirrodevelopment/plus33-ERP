/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : CalendarExceptionDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CalendarExceptionDtoController
 * Related Service   : CalendarExceptionDtoService, CalendarExceptionDtoServiceImpl
 * Related Repository: CalendarExceptionDtoRepository
 * Related Entity    : CalendarExceptionDto
 * Related DTO       : CalendarExceptionDto
 * Related Mapper    : CalendarExceptionDtoMapper
 * Related DB Table  : calendar_exception_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CalendarExceptionDtoController, CalendarExceptionDtoService, CalendarExceptionDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
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
     * Retrieves calendar id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCalendarId() { return calendarId; }
    /**
     * Performs the setCalendarId operation in this module.
     *
     * @param calendarId the calendarId input value
     */
    public void setCalendarId(Long calendarId) { this.calendarId = calendarId; }
    /**
     * Retrieves exception date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getExceptionDate() { return exceptionDate; }
    /**
     * Performs the setExceptionDate operation in this module.
     *
     * @param exceptionDate the exceptionDate input value
     */
    public void setExceptionDate(LocalDate exceptionDate) { this.exceptionDate = exceptionDate; }
    /**
     * Retrieves exception type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExceptionType() { return exceptionType; }
    /**
     * Performs the setExceptionType operation in this module.
     *
     * @param exceptionType the exceptionType input value
     */
    public void setExceptionType(String exceptionType) { this.exceptionType = exceptionType; }
    /**
     * Retrieves available hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAvailableHours() { return availableHours; }
    /**
     * Performs the setAvailableHours operation in this module.
     *
     * @param availableHours the availableHours input value
     */
    public void setAvailableHours(BigDecimal availableHours) { this.availableHours = availableHours; }
    /**
     * Retrieves description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; }
    /**
     * Performs the setDescription operation in this module.
     *
     * @param description the description input value
     */
    public void setDescription(String description) { this.description = description; }
}
