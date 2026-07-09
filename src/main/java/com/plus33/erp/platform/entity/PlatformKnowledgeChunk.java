/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformKnowledgeChunk.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformKnowledgeChunkController
 * Related Service   : PlatformKnowledgeChunkService, PlatformKnowledgeChunkServiceImpl
 * Related Repository: PlatformKnowledgeChunkRepository
 * Related Entity    : PlatformKnowledgeChunk
 * Related DTO       : N/A
 * Related Mapper    : PlatformKnowledgeChunkMapper
 * Related DB Table  : platform_knowledge_chunk
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformKnowledgeChunkRepository, PlatformKnowledgeChunkMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_knowledge_chunk'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformKnowledgeChunk}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_knowledge_chunk'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_knowledge_chunk}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_knowledge_chunk")
public class PlatformKnowledgeChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", nullable = false)
    @NotNull
    private Long sourceId;

    @Column(name = "chunk_content", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String chunkContent;

    @Column(name = "vector_placeholder")
    @Size(max = 250)
    private String vectorPlaceholder;

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
     * Retrieves chunk content data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChunkContent() { return chunkContent; }
    /**
     * Performs the setChunkContent operation in this module.
     *
     * @param chunkContent the chunkContent input value
     */
    public void setChunkContent(String chunkContent) { this.chunkContent = chunkContent; }
    /**
     * Retrieves vector placeholder data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getVectorPlaceholder() { return vectorPlaceholder; }
    /**
     * Performs the setVectorPlaceholder operation in this module.
     *
     * @param vectorPlaceholder the vectorPlaceholder input value
     */
    public void setVectorPlaceholder(String vectorPlaceholder) { this.vectorPlaceholder = vectorPlaceholder; }
}