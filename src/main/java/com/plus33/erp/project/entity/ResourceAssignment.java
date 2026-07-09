/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : ResourceAssignment.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ResourceAssignmentController
 * Related Service   : ResourceAssignmentService, ResourceAssignmentServiceImpl
 * Related Repository: ResourceAssignmentRepository
 * Related Entity    : ResourceAssignment
 * Related DTO       : N/A
 * Related Mapper    : ResourceAssignmentMapper
 * Related DB Table  : project_resource_assignments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ResourceAssignmentRepository, ResourceAssignmentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'project_resource_assignments'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ResourceAssignment}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'project_resource_assignments'.</p>
 *
 * <p><b>Database Table   :</b> {@code project_resource_assignments}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "project_resource_assignments")
public class ResourceAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "resource_id", nullable = false)
    private Long resourceId;

    @Column(name = "allocation_percentage", nullable = false)
    private BigDecimal allocationPercentage = new BigDecimal("100.00");

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

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
     * Retrieves project id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProjectId() { return projectId; }
    /**
     * Performs the setProjectId operation in this module.
     *
     * @param projectId the projectId input value
     */
    public void setProjectId(Long projectId) { this.projectId = projectId; }
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
     * Retrieves a paginated list of allocation percentage records.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAllocationPercentage() { return allocationPercentage; }
    /**
     * Performs the setAllocationPercentage operation in this module.
     *
     * @param allocationPercentage the allocationPercentage input value
     */
    public void setAllocationPercentage(BigDecimal allocationPercentage) { this.allocationPercentage = allocationPercentage; }
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
}