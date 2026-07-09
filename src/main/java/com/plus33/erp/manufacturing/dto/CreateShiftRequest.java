/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : CreateShiftRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreateShiftController
 * Related Service   : CreateShiftService, CreateShiftServiceImpl
 * Related Repository: CreateShiftRepository
 * Related Entity    : CreateShift
 * Related DTO       : CreateShiftRequest
 * Related Mapper    : CreateShiftMapper
 * Related DB Table  : create_shifts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreateShiftController, CreateShiftService, CreateShiftServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalTime;
import jakarta.validation.constraints.*;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code CreateShiftRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class CreateShiftRequest {
    @NotNull private Integer dayOfWeek;
    @NotBlank private String shiftName;
    @NotNull private LocalTime startTime;
    @NotNull private LocalTime endTime;
    private Integer breakMinutes = 0;
    @NotNull private BigDecimal availableHours;
    private String shiftType = "REGULAR"; // REGULAR, OVERTIME, SHUTDOWN

    public CreateShiftRequest() {}

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
