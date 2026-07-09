/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationWorkflowTask.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationWorkflowTaskController
 * Related Service   : IntegrationWorkflowTaskService, IntegrationWorkflowTaskServiceImpl
 * Related Repository: IntegrationWorkflowTaskRepository
 * Related Entity    : IntegrationWorkflowTask
 * Related DTO       : N/A
 * Related Mapper    : IntegrationWorkflowTaskMapper
 * Related DB Table  : integration_workflow_task
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationWorkflowTaskRepository, IntegrationWorkflowTaskMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_workflow_task'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationWorkflowTask}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_workflow_task'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_workflow_task}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_workflow_task")
public class IntegrationWorkflowTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instance_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String instanceCode;

    @Column(name = "task_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String taskName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status;

    @Column(nullable = false)
    @NotNull
    private Integer attempts = 0;

    @Column(name = "started_at", nullable = false)
    @NotNull
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

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
     * Retrieves instance code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getInstanceCode() { return instanceCode; }
    /**
     * Performs the setInstanceCode operation in this module.
     *
     * @param instanceCode the instanceCode input value
     */
    public void setInstanceCode(String instanceCode) { this.instanceCode = instanceCode; }
    /**
     * Retrieves task name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTaskName() { return taskName; }
    /**
     * Performs the setTaskName operation in this module.
     *
     * @param taskName the taskName input value
     */
    public void setTaskName(String taskName) { this.taskName = taskName; }
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
    /**
     * Retrieves attempts data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getAttempts() { return attempts; }
    /**
     * Performs the setAttempts operation in this module.
     *
     * @param attempts the attempts input value
     */
    public void setAttempts(Integer attempts) { this.attempts = attempts; }
    /**
     * Retrieves started at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartedAt() { return startedAt; }
    /**
     * Performs the setStartedAt operation in this module.
     *
     * @param startedAt the startedAt input value
     */
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    /**
     * Retrieves completed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletedAt() { return completedAt; }
    /**
     * Performs the setCompletedAt operation in this module.
     *
     * @param completedAt the completedAt input value
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}