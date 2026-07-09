/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ManufacturingCalendarShift.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingCalendarShiftController
 * Related Service   : ManufacturingCalendarShiftService, ManufacturingCalendarShiftServiceImpl
 * Related Repository: ManufacturingCalendarShiftRepository
 * Related Entity    : ManufacturingCalendarShift
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingCalendarShiftMapper
 * Related DB Table  : manufacturing_calendar_shifts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ManufacturingCalendarShiftRepository, ManufacturingCalendarShiftMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'manufacturing_calendar_shifts'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingCalendarShift}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'manufacturing_calendar_shifts'.</p>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_calendar_shifts}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "manufacturing_calendar_shifts")
public class ManufacturingCalendarShift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private ManufacturingCalendar calendar;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek; // 1=Monday .. 7=Sunday

    @Column(name = "shift_name", nullable = false, length = 50)
    private String shiftName;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "break_minutes", nullable = false)
    private Integer breakMinutes = 0;

    @Column(name = "available_hours", nullable = false, precision = 6, scale = 2)
    private BigDecimal availableHours = new BigDecimal("8.00");

    @Column(name = "shift_type", nullable = false, length = 20)
    private String shiftType = "REGULAR"; // REGULAR, OVERTIME, SHUTDOWN

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ManufacturingCalendarShift() {}

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
     * Retrieves calendar data from the database.
     *
     * @return the ManufacturingCalendar result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ManufacturingCalendar getCalendar() { return calendar; }
    /**
     * Performs the setCalendar operation in this module.
     *
     * @param calendar the calendar input value
     */
    public void setCalendar(ManufacturingCalendar calendar) { this.calendar = calendar; }
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
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}