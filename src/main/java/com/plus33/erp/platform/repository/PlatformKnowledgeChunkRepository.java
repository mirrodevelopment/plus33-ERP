/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformKnowledgeChunkRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformKnowledgeChunkController
 * Related Service   : PlatformKnowledgeChunkService, PlatformKnowledgeChunkServiceImpl
 * Related Repository: PlatformKnowledgeChunkRepository
 * Related Entity    : PlatformKnowledgeChunk
 * Related DTO       : N/A
 * Related Mapper    : PlatformKnowledgeChunkMapper
 * Related DB Table  : platform_knowledge_chunks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformKnowledgeChunkService, PlatformKnowledgeChunkServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_knowledge_chunks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformKnowledgeChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformKnowledgeChunkRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_knowledge_chunks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_knowledge_chunks}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformKnowledgeChunkRepository extends JpaRepository<PlatformKnowledgeChunk, Long> {
}