/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : ProjectDependency.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectDependencyController
 * Related Service   : ProjectDependencyService, ProjectDependencyServiceImpl
 * Related Repository: ProjectDependencyRepository
 * Related Entity    : ProjectDependency
 * Related DTO       : N/A
 * Related Mapper    : ProjectDependencyMapper
 * Related DB Table  : project_task_dependencies
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectDependencyRepository, ProjectDependencyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'project_task_dependencies'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectDependency}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'project_task_dependencies'.</p>
 *
 * <p><b>Database Table   :</b> {@code project_task_dependencies}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "project_task_dependencies")
public class ProjectDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "predecessor_task_id", nullable = false)
    private Long predecessorTaskId;

    @Column(name = "dependency_type", nullable = false, length = 20)
    private String dependencyType = "FS";

    @Column(name = "lag_days", nullable = false)
    private Integer lagDays = 0;

    @Column(name = "lead_days", nullable = false)
    private Integer leadDays = 0;

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
     * Retrieves predecessor task id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPredecessorTaskId() { return predecessorTaskId; }
    /**
     * Performs the setPredecessorTaskId operation in this module.
     *
     * @param predecessorTaskId the predecessorTaskId input value
     */
    public void setPredecessorTaskId(Long predecessorTaskId) { this.predecessorTaskId = predecessorTaskId; }
    /**
     * Retrieves dependency type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDependencyType() { return dependencyType; }
    /**
     * Performs the setDependencyType operation in this module.
     *
     * @param dependencyType the dependencyType input value
     */
    public void setDependencyType(String dependencyType) { this.dependencyType = dependencyType; }
    /**
     * Retrieves lag days data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getLagDays() { return lagDays; }
    /**
     * Performs the setLagDays operation in this module.
     *
     * @param lagDays the lagDays input value
     */
    public void setLagDays(Integer lagDays) { this.lagDays = lagDays; }
    /**
     * Retrieves lead days data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getLeadDays() { return leadDays; }
    /**
     * Performs the setLeadDays operation in this module.
     *
     * @param leadDays the leadDays input value
     */
    public void setLeadDays(Integer leadDays) { this.leadDays = leadDays; }
}