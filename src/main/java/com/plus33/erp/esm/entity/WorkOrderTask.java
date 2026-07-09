/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.entity
 * File              : WorkOrderTask.java
 * Purpose           : JPA Entity representing a persistent database record in Esm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WorkOrderTaskController
 * Related Service   : WorkOrderTaskService, WorkOrderTaskServiceImpl
 * Related Repository: WorkOrderTaskRepository
 * Related Entity    : WorkOrderTask
 * Related DTO       : N/A
 * Related Mapper    : WorkOrderTaskMapper
 * Related DB Table  : esm_work_order_tasks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WorkOrderTaskRepository, WorkOrderTaskMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'esm_work_order_tasks'. Defines persistent domain object for Esm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.esm.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code WorkOrderTask}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'esm_work_order_tasks'.</p>
 *
 * <p><b>Database Table   :</b> {@code esm_work_order_tasks}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "esm_work_order_tasks")
public class WorkOrderTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_order_id", nullable = false)
    private Long workOrderId;

    @Column(name = "task_sequence", nullable = false)
    private Integer taskSequence;

    @Column(name = "task_description", nullable = false, length = 255)
    private String taskDescription;

    @Column(name = "estimated_minutes", nullable = false)
    private Integer estimatedMinutes;

    @Column(name = "actual_minutes")
    private Integer actualMinutes;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";

    @Column(name = "required_skill", length = 50)
    private String requiredSkill;

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
     * Retrieves work order id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWorkOrderId() { return workOrderId; }
    /**
     * Performs the setWorkOrderId operation in this module.
     *
     * @param workOrderId the workOrderId input value
     */
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }
    /**
     * Retrieves task sequence data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getTaskSequence() { return taskSequence; }
    /**
     * Performs the setTaskSequence operation in this module.
     *
     * @param taskSequence the taskSequence input value
     */
    public void setTaskSequence(Integer taskSequence) { this.taskSequence = taskSequence; }
    /**
     * Retrieves task description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTaskDescription() { return taskDescription; }
    /**
     * Performs the setTaskDescription operation in this module.
     *
     * @param taskDescription the taskDescription input value
     */
    public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }
    /**
     * Retrieves estimated minutes data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getEstimatedMinutes() { return estimatedMinutes; }
    /**
     * Performs the setEstimatedMinutes operation in this module.
     *
     * @param estimatedMinutes the estimatedMinutes input value
     */
    public void setEstimatedMinutes(Integer estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }
    /**
     * Retrieves actual minutes data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getActualMinutes() { return actualMinutes; }
    /**
     * Performs the setActualMinutes operation in this module.
     *
     * @param actualMinutes the actualMinutes input value
     */
    public void setActualMinutes(Integer actualMinutes) { this.actualMinutes = actualMinutes; }
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
     * Retrieves required skill data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequiredSkill() { return requiredSkill; }
    /**
     * Performs the setRequiredSkill operation in this module.
     *
     * @param requiredSkill the requiredSkill input value
     */
    public void setRequiredSkill(String requiredSkill) { this.requiredSkill = requiredSkill; }
}