/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformKnowledgeSource.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformKnowledgeSourceController
 * Related Service   : PlatformKnowledgeSourceService, PlatformKnowledgeSourceServiceImpl
 * Related Repository: PlatformKnowledgeSourceRepository
 * Related Entity    : PlatformKnowledgeSource
 * Related DTO       : N/A
 * Related Mapper    : PlatformKnowledgeSourceMapper
 * Related DB Table  : platform_knowledge_source
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformKnowledgeSourceRepository, PlatformKnowledgeSourceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_knowledge_source'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformKnowledgeSource}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_knowledge_source'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_knowledge_source}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_knowledge_source")
public class PlatformKnowledgeSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "source_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String sourceName;

    @Column(name = "source_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceType;

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
     * Retrieves source name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceName() { return sourceName; }
    /**
     * Performs the setSourceName operation in this module.
     *
     * @param sourceName the sourceName input value
     */
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
    /**
     * Retrieves source type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceType() { return sourceType; }
    /**
     * Performs the setSourceType operation in this module.
     *
     * @param sourceType the sourceType input value
     */
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
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