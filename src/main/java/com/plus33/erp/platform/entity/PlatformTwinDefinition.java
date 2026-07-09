/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTwinDefinition.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTwinDefinitionController
 * Related Service   : PlatformTwinDefinitionService, PlatformTwinDefinitionServiceImpl
 * Related Repository: PlatformTwinDefinitionRepository
 * Related Entity    : PlatformTwinDefinition
 * Related DTO       : N/A
 * Related Mapper    : PlatformTwinDefinitionMapper
 * Related DB Table  : platform_twin_definition
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTwinDefinitionRepository, PlatformTwinDefinitionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_twin_definition'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTwinDefinition}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_twin_definition'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_twin_definition}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_twin_definition")
public class PlatformTwinDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "definition_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String definitionCode;

    @Column(name = "definition_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String definitionName;

    @Column(columnDefinition = "TEXT")
    private String description;

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
     * Retrieves definition name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDefinitionName() { return definitionName; }
    /**
     * Performs the setDefinitionName operation in this module.
     *
     * @param definitionName the definitionName input value
     */
    public void setDefinitionName(String definitionName) { this.definitionName = definitionName; }
    /**
     * Retrieves description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; }
    /**
     * Performs the setDescription operation in this module.
     *
     * @param description the description input value
     */
    public void setDescription(String description) { this.description = description; }
}