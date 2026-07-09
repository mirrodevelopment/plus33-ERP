/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationWorkflowDefinition.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationWorkflowDefinitionController
 * Related Service   : IntegrationWorkflowDefinitionService, IntegrationWorkflowDefinitionServiceImpl
 * Related Repository: IntegrationWorkflowDefinitionRepository
 * Related Entity    : IntegrationWorkflowDefinition
 * Related DTO       : N/A
 * Related Mapper    : IntegrationWorkflowDefinitionMapper
 * Related DB Table  : integration_workflow_definition
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationWorkflowDefinitionRepository, IntegrationWorkflowDefinitionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_workflow_definition'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationWorkflowDefinition}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_workflow_definition'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_workflow_definition}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_workflow_definition")
public class IntegrationWorkflowDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "definition_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String definitionCode;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String name;

    @Column(name = "layout_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String layoutJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves layout json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLayoutJson() { return layoutJson; }
    /**
     * Performs the setLayoutJson operation in this module.
     *
     * @param layoutJson the layoutJson input value
     */
    public void setLayoutJson(String layoutJson) { this.layoutJson = layoutJson; }
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
}