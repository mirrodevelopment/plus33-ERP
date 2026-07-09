/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformKnowledgeVersion.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformKnowledgeVersionController
 * Related Service   : PlatformKnowledgeVersionService, PlatformKnowledgeVersionServiceImpl
 * Related Repository: PlatformKnowledgeVersionRepository
 * Related Entity    : PlatformKnowledgeVersion
 * Related DTO       : N/A
 * Related Mapper    : PlatformKnowledgeVersionMapper
 * Related DB Table  : platform_knowledge_version
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformKnowledgeVersionRepository, PlatformKnowledgeVersionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_knowledge_version'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformKnowledgeVersion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_knowledge_version'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_knowledge_version}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_knowledge_version")
public class PlatformKnowledgeVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "source_id", nullable = false)
    @NotNull
    private Long sourceId;

    @Column(name = "index_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String indexVersion;

    @Column(name = "total_chunks", nullable = false)
    @NotNull
    private Integer totalChunks;

    @Column(name = "indexed_at", nullable = false)
    @NotNull
    private LocalDateTime indexedAt = LocalDateTime.now();

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
     * Retrieves source id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceId() { return sourceId; }
    /**
     * Performs the setSourceId operation in this module.
     *
     * @param sourceId the sourceId input value
     */
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    /**
     * Retrieves index version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIndexVersion() { return indexVersion; }
    /**
     * Performs the setIndexVersion operation in this module.
     *
     * @param indexVersion the indexVersion input value
     */
    public void setIndexVersion(String indexVersion) { this.indexVersion = indexVersion; }
    /**
     * Retrieves total chunks data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getTotalChunks() { return totalChunks; }
    /**
     * Performs the setTotalChunks operation in this module.
     *
     * @param totalChunks the totalChunks input value
     */
    public void setTotalChunks(Integer totalChunks) { this.totalChunks = totalChunks; }
    /**
     * Retrieves indexed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getIndexedAt() { return indexedAt; }
    /**
     * Performs the setIndexedAt operation in this module.
     *
     * @param indexedAt the indexedAt input value
     */
    public void setIndexedAt(LocalDateTime indexedAt) { this.indexedAt = indexedAt; }
}