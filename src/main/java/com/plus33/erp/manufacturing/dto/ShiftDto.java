/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : ShiftDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ShiftDtoController
 * Related Service   : ShiftDtoService, ShiftDtoServiceImpl
 * Related Repository: ShiftDtoRepository
 * Related Entity    : ShiftDto
 * Related DTO       : ShiftDto
 * Related Mapper    : ShiftDtoMapper
 * Related DB Table  : shift_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ShiftDtoController, ShiftDtoService, ShiftDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
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
     * Retrieves day of week data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getDayOfWeek() { return dayOfWeek; }
    /**
     * Performs the setDayOfWeek operation in this module.
     *
     * @param dayOfWeek the dayOfWeek input value
     */
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    /**
     * Retrieves shift name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getShiftName() { return shiftName; }
    /**
     * Performs the setShiftName operation in this module.
     *
     * @param shiftName the shiftName input value
     */
    public void setShiftName(String shiftName) { this.shiftName = shiftName; }
    /**
     * Retrieves start time data from the database.
     *
     * @return the LocalTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalTime getStartTime() { return startTime; }
    /**
     * Performs the setStartTime operation in this module.
     *
     * @param startTime the startTime input value
     */
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    /**
     * Retrieves end time data from the database.
     *
     * @return the LocalTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalTime getEndTime() { return endTime; }
    /**
     * Performs the setEndTime operation in this module.
     *
     * @param endTime the endTime input value
     */
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    /**
     * Retrieves break minutes data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getBreakMinutes() { return breakMinutes; }
    /**
     * Performs the setBreakMinutes operation in this module.
     *
     * @param breakMinutes the breakMinutes input value
     */
    public void setBreakMinutes(Integer breakMinutes) { this.breakMinutes = breakMinutes; }
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
     * Retrieves shift type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getShiftType() { return shiftType; }
    /**
     * Performs the setShiftType operation in this module.
     *
     * @param shiftType the shiftType input value
     */
    public void setShiftType(String shiftType) { this.shiftType = shiftType; }
}
