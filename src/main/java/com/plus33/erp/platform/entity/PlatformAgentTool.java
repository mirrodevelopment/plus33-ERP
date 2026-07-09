/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAgentTool.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAgentToolController
 * Related Service   : PlatformAgentToolService, PlatformAgentToolServiceImpl
 * Related Repository: PlatformAgentToolRepository
 * Related Entity    : PlatformAgentTool
 * Related DTO       : N/A
 * Related Mapper    : PlatformAgentToolMapper
 * Related DB Table  : platform_agent_tool
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAgentToolRepository, PlatformAgentToolMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_agent_tool'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAgentTool}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_agent_tool'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_agent_tool}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_agent_tool")
public class PlatformAgentTool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "tool_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String toolCode;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String description;

    @Column(name = "module_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String moduleName;

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
     * Retrieves tool code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getToolCode() { return toolCode; }
    /**
     * Performs the setToolCode operation in this module.
     *
     * @param toolCode the toolCode input value
     */
    public void setToolCode(String toolCode) { this.toolCode = toolCode; }
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
    /**
     * Retrieves module name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getModuleName() { return moduleName; }
    /**
     * Performs the setModuleName operation in this module.
     *
     * @param moduleName the moduleName input value
     */
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
}