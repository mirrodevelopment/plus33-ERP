/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : Timesheet.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TimesheetController
 * Related Service   : TimesheetService, TimesheetServiceImpl
 * Related Repository: TimesheetRepository
 * Related Entity    : Timesheet
 * Related DTO       : N/A
 * Related Mapper    : TimesheetMapper
 * Related DB Table  : project_timesheets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TimesheetRepository, TimesheetMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'project_timesheets'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code Timesheet}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'project_timesheets'.</p>
 *
 * <p><b>Database Table   :</b> {@code project_timesheets}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "project_timesheets")
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_id", nullable = false)
    private Long resourceId;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "hours_worked", nullable = false)
    private BigDecimal hoursWorked;

    @Column(nullable = false, length = 30)
    private String status = "SUBMITTED";

    // Getters and setters
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
     * Retrieves resource id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getResourceId() { return resourceId; }
    /**
     * Performs the setResourceId operation in this module.
     *
     * @param resourceId the resourceId input value
     */
    public void setResourceId(Long resourceId) { this.resourceId = resourceId; }
    /**
     * Retrieves task id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTaskId() { return taskId; }
    /**
     * Performs the setTaskId operation in this module.
     *
     * @param taskId the taskId input value
     */
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    /**
     * Retrieves work date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getWorkDate() { return workDate; }
    /**
     * Performs the setWorkDate operation in this module.
     *
     * @param workDate the workDate input value
     */
    public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }
    /**
     * Retrieves hours worked data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getHoursWorked() { return hoursWorked; }
    /**
     * Performs the setHoursWorked operation in this module.
     *
     * @param hoursWorked the hoursWorked input value
     */
    public void setHoursWorked(BigDecimal hoursWorked) { this.hoursWorked = hoursWorked; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
}