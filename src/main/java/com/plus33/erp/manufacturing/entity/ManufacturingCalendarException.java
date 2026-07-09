/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ManufacturingCalendarException.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingCalendarExceptionController
 * Related Service   : ManufacturingCalendarExceptionService, ManufacturingCalendarExceptionServiceImpl
 * Related Repository: ManufacturingCalendarExceptionRepository
 * Related Entity    : ManufacturingCalendarException
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingCalendarExceptionMapper
 * Related DB Table  : manufacturing_calendar_exceptions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ManufacturingCalendarExceptionRepository, ManufacturingCalendarExceptionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'manufacturing_calendar_exceptions'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingCalendarException}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'manufacturing_calendar_exceptions'.</p>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_calendar_exceptions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "manufacturing_calendar_exceptions")
public class ManufacturingCalendarException {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private ManufacturingCalendar calendar;

    @Column(name = "exception_date", nullable = false)
    private LocalDate exceptionDate;

    @Column(name = "exception_type", nullable = false, length = 30)
    private String exceptionType; // HOLIDAY, MAINTENANCE_WINDOW, OVERTIME, SHUTDOWN

    @Column(name = "available_hours", nullable = false, precision = 6, scale = 2)
    private BigDecimal availableHours = BigDecimal.ZERO;

    @Column(length = 255)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ManufacturingCalendarException() {}

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
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}