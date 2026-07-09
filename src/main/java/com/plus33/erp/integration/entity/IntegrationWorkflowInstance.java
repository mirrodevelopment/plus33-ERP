/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationWorkflowInstance.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationWorkflowInstanceController
 * Related Service   : IntegrationWorkflowInstanceService, IntegrationWorkflowInstanceServiceImpl
 * Related Repository: IntegrationWorkflowInstanceRepository
 * Related Entity    : IntegrationWorkflowInstance
 * Related DTO       : N/A
 * Related Mapper    : IntegrationWorkflowInstanceMapper
 * Related DB Table  : integration_workflow_instance
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationWorkflowInstanceRepository, IntegrationWorkflowInstanceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_workflow_instance'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationWorkflowInstance}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_workflow_instance'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_workflow_instance}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_workflow_instance")
public class IntegrationWorkflowInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "definition_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String definitionCode;

    @Column(name = "instance_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String instanceCode;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "RUNNING";

    @Column(name = "current_step")
    @Size(max = 100)
    private String currentStep;

    @Column(name = "variables_json", columnDefinition = "TEXT")
    private String variablesJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

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
     * Retrieves definition code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDefinitionCode() { return definitionCode; }
    /**
     * Performs the setDefinitionCode operation in this module.
     *
     * @param definitionCode the definitionCode input value
     */
    public void setDefinitionCode(String definitionCode) { this.definitionCode = definitionCode; }
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
     * Retrieves current step data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCurrentStep() { return currentStep; }
    /**
     * Performs the setCurrentStep operation in this module.
     *
     * @param currentStep the currentStep input value
     */
    public void setCurrentStep(String currentStep) { this.currentStep = currentStep; }
    /**
     * Retrieves variables json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getVariablesJson() { return variablesJson; }
    /**
     * Performs the setVariablesJson operation in this module.
     *
     * @param variablesJson the variablesJson input value
     */
    public void setVariablesJson(String variablesJson) { this.variablesJson = variablesJson; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}