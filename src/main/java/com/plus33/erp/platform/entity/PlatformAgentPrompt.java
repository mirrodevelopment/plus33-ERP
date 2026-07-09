/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAgentPrompt.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAgentPromptController
 * Related Service   : PlatformAgentPromptService, PlatformAgentPromptServiceImpl
 * Related Repository: PlatformAgentPromptRepository
 * Related Entity    : PlatformAgentPrompt
 * Related DTO       : N/A
 * Related Mapper    : PlatformAgentPromptMapper
 * Related DB Table  : platform_agent_prompt
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAgentPromptRepository, PlatformAgentPromptMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_agent_prompt'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAgentPrompt}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_agent_prompt'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_agent_prompt}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_agent_prompt")
public class PlatformAgentPrompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "prompt_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String promptCode;

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
     * Retrieves prompt code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPromptCode() { return promptCode; }
    /**
     * Performs the setPromptCode operation in this module.
     *
     * @param promptCode the promptCode input value
     */
    public void setPromptCode(String promptCode) { this.promptCode = promptCode; }
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