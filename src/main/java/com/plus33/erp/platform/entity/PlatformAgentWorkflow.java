/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAgentWorkflow.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAgentWorkflowController
 * Related Service   : PlatformAgentWorkflowService, PlatformAgentWorkflowServiceImpl
 * Related Repository: PlatformAgentWorkflowRepository
 * Related Entity    : PlatformAgentWorkflow
 * Related DTO       : N/A
 * Related Mapper    : PlatformAgentWorkflowMapper
 * Related DB Table  : platform_agent_workflow
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAgentWorkflowRepository, PlatformAgentWorkflowMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_agent_workflow'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAgentWorkflow}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_agent_workflow'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_agent_workflow}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_agent_workflow")
public class PlatformAgentWorkflow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "workflow_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String workflowCode;

    @Column(name = "workflow_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String workflowName;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

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
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
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
}