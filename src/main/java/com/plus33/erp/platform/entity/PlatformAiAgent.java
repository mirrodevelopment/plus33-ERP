/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAiAgent.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAiAgentController
 * Related Service   : PlatformAiAgentService, PlatformAiAgentServiceImpl
 * Related Repository: PlatformAiAgentRepository
 * Related Entity    : PlatformAiAgent
 * Related DTO       : N/A
 * Related Mapper    : PlatformAiAgentMapper
 * Related DB Table  : platform_ai_agent
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAiAgentRepository, PlatformAiAgentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_ai_agent'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAiAgent}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_ai_agent'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ai_agent}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_ai_agent")
public class PlatformAiAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "agent_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String agentCode;

    @Column(name = "agent_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String agentName;

    @Column(name = "system_instruction", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String systemInstruction;

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
     * Retrieves agent code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAgentCode() { return agentCode; }
    /**
     * Performs the setAgentCode operation in this module.
     *
     * @param agentCode the agentCode input value
     */
    public void setAgentCode(String agentCode) { this.agentCode = agentCode; }
    /**
     * Retrieves agent name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAgentName() { return agentName; }
    /**
     * Performs the setAgentName operation in this module.
     *
     * @param agentName the agentName input value
     */
    public void setAgentName(String agentName) { this.agentName = agentName; }
    /**
     * Retrieves system instruction data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSystemInstruction() { return systemInstruction; }
    /**
     * Performs the setSystemInstruction operation in this module.
     *
     * @param systemInstruction the systemInstruction input value
     */
    public void setSystemInstruction(String systemInstruction) { this.systemInstruction = systemInstruction; }
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