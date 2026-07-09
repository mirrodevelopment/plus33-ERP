/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ManufacturingCalendar.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingCalendarController
 * Related Service   : ManufacturingCalendarService, ManufacturingCalendarServiceImpl
 * Related Repository: ManufacturingCalendarRepository
 * Related Entity    : ManufacturingCalendar
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingCalendarMapper
 * Related DB Table  : manufacturing_calendars
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ManufacturingCalendarRepository, ManufacturingCalendarMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'manufacturing_calendars'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingCalendar}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'manufacturing_calendars'.</p>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_calendars}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "manufacturing_calendars")
public class ManufacturingCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "calendar_type", nullable = false, length = 30)
    private String calendarType; // PLANT, WORK_CENTER, MACHINE, SHIFT, HOLIDAY, OVERTIME

    @Column(name = "reference_type", length = 30)
    private String referenceType; // WORK_CENTER, MACHINE

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 60)
    private String timezone = "UTC";

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ManufacturingCalendar() {}

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
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}