/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : WarehouseWorkflow.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseWorkflowController
 * Related Service   : WarehouseWorkflowService, WarehouseWorkflowServiceImpl
 * Related Repository: WarehouseWorkflowRepository
 * Related Entity    : WarehouseWorkflow
 * Related DTO       : N/A
 * Related Mapper    : WarehouseWorkflowMapper
 * Related DB Table  : warehouse_workflows
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseWorkflowRepository, WarehouseWorkflowMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'warehouse_workflows'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseWorkflow}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'warehouse_workflows'.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_workflows}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "warehouse_workflows")
public class WarehouseWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "workflow_code", nullable = false, unique = true, length = 50)
    private String workflowCode;

    @Column(name = "workflow_name", nullable = false, length = 100)
    private String workflowName;

    @Column(name = "process_type", nullable = false, length = 30)
    private String processType;

    @Column(name = "steps_json", nullable = false, columnDefinition = "TEXT")
    private String stepsJson;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves workflow code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getWorkflowCode() { return workflowCode; }
    /**
     * Performs the setWorkflowCode operation in this module.
     *
     * @param workflowCode the workflowCode input value
     */
    public void setWorkflowCode(String workflowCode) { this.workflowCode = workflowCode; }
    /**
     * Retrieves workflow name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getWorkflowName() { return workflowName; }
    /**
     * Performs the setWorkflowName operation in this module.
     *
     * @param workflowName the workflowName input value
     */
    public void setWorkflowName(String workflowName) { this.workflowName = workflowName; }
    /**
     * Retrieves process type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProcessType() { return processType; }
    /**
     * Performs the setProcessType operation in this module.
     *
     * @param processType the processType input value
     */
    public void setProcessType(String processType) { this.processType = processType; }
    /**
     * Retrieves steps json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStepsJson() { return stepsJson; }
    /**
     * Performs the setStepsJson operation in this module.
     *
     * @param stepsJson the stepsJson input value
     */
    public void setStepsJson(String stepsJson) { this.stepsJson = stepsJson; }
    /**
     * Performs the isActive operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(boolean active) { this.active = active; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}