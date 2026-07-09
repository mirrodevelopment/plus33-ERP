/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformPromptVersion.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformPromptVersionController
 * Related Service   : PlatformPromptVersionService, PlatformPromptVersionServiceImpl
 * Related Repository: PlatformPromptVersionRepository
 * Related Entity    : PlatformPromptVersion
 * Related DTO       : N/A
 * Related Mapper    : PlatformPromptVersionMapper
 * Related DB Table  : platform_prompt_version
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformPromptVersionRepository, PlatformPromptVersionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_prompt_version'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformPromptVersion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_prompt_version'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_prompt_version}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_prompt_version")
public class PlatformPromptVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "prompt_id", nullable = false)
    @NotNull
    private Long promptId;

    @Column(name = "prompt_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String promptVersion;

    @Column(name = "system_prompt", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String systemPrompt;

    @Column(name = "user_template", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String userTemplate;

    @Column(name = "created_at", nullable = false)
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
     * Retrieves prompt id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPromptId() { return promptId; }
    /**
     * Performs the setPromptId operation in this module.
     *
     * @param promptId the promptId input value
     */
    public void setPromptId(Long promptId) { this.promptId = promptId; }
    /**
     * Retrieves prompt version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPromptVersion() { return promptVersion; }
    /**
     * Performs the setPromptVersion operation in this module.
     *
     * @param promptVersion the promptVersion input value
     */
    public void setPromptVersion(String promptVersion) { this.promptVersion = promptVersion; }
    /**
     * Retrieves system prompt data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSystemPrompt() { return systemPrompt; }
    /**
     * Performs the setSystemPrompt operation in this module.
     *
     * @param systemPrompt the systemPrompt input value
     */
    public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }
    /**
     * Retrieves user template data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getUserTemplate() { return userTemplate; }
    /**
     * Performs the setUserTemplate operation in this module.
     *
     * @param userTemplate the userTemplate input value
     */
    public void setUserTemplate(String userTemplate) { this.userTemplate = userTemplate; }
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