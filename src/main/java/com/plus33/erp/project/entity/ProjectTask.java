/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : ProjectTask.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectTaskController
 * Related Service   : ProjectTaskService, ProjectTaskServiceImpl
 * Related Repository: ProjectTaskRepository
 * Related Entity    : ProjectTask
 * Related DTO       : N/A
 * Related Mapper    : ProjectTaskMapper
 * Related DB Table  : project_tasks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectTaskRepository, ProjectTaskMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'project_tasks'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectTask}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'project_tasks'.</p>
 *
 * <p><b>Database Table   :</b> {@code project_tasks}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "project_tasks")
public class ProjectTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wbs_version_id", nullable = false)
    private Long wbsVersionId;

    @Column(name = "task_number", nullable = false, length = 50)
    private String taskNumber;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "estimated_hours", nullable = false)
    private BigDecimal estimatedHours = BigDecimal.ZERO;

    @Column(name = "actual_hours", nullable = false)
    private BigDecimal actualHours = BigDecimal.ZERO;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";

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
     * Retrieves wbs version id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWbsVersionId() { return wbsVersionId; }
    /**
     * Performs the setWbsVersionId operation in this module.
     *
     * @param wbsVersionId the wbsVersionId input value
     */
    public void setWbsVersionId(Long wbsVersionId) { this.wbsVersionId = wbsVersionId; }
    /**
     * Retrieves task number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTaskNumber() { return taskNumber; }
    /**
     * Performs the setTaskNumber operation in this module.
     *
     * @param taskNumber the taskNumber input value
     */
    public void setTaskNumber(String taskNumber) { this.taskNumber = taskNumber; }
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
     * Retrieves estimated hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEstimatedHours() { return estimatedHours; }
    /**
     * Performs the setEstimatedHours operation in this module.
     *
     * @param estimatedHours the estimatedHours input value
     */
    public void setEstimatedHours(BigDecimal estimatedHours) { this.estimatedHours = estimatedHours; }
    /**
     * Retrieves actual hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualHours() { return actualHours; }
    /**
     * Performs the setActualHours operation in this module.
     *
     * @param actualHours the actualHours input value
     */
    public void setActualHours(BigDecimal actualHours) { this.actualHours = actualHours; }
    /**
     * Retrieves start date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getStartDate() { return startDate; }
    /**
     * Performs the setStartDate operation in this module.
     *
     * @param startDate inclusive start date for date-range filtering
     */
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    /**
     * Retrieves end date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEndDate() { return endDate; }
    /**
     * Performs the setEndDate operation in this module.
     *
     * @param endDate inclusive end date for date-range filtering
     */
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
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