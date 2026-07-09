/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Agent Module
 * Package           : com.plus33.erp.agent.knowledge
 * File              : ChunkingService.java
 * Purpose           : Business logic service layer for Agent Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ChunkingController
 * Related Service   : ChunkingService
 * Related Repository: ChunkingRepository
 * Related Entity    : Chunking
 * Related DTO       : N/A
 * Related Mapper    : ChunkingMapper
 * Related DB Table  : chunkings
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : ChunkingController, ChunkingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Agent Module. Implements ChunkingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.agent.knowledge;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Agent Module</b>
 *
 * <p><b>Class  :</b> {@code ChunkingService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.agent.knowledge}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Agent Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ChunkingController
 *   --> ChunkingService (this)
 *   --> Validate business rules
 *   --> ChunkingRepository (read/write 'chunkings')
 *   --> ChunkingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code chunkings}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ChunkingService {
    @Autowired PlatformKnowledgeSourceRepository sourceRepo;
    @Autowired PlatformKnowledgeChunkRepository chunkRepo;
    /**
     * Creates a new source and persists it to the database.
     *
     * @param name the name input value
     * @param type the type input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerSource(String name, String type) {
        PlatformKnowledgeSource src = new PlatformKnowledgeSource();
        src.setSourceName(name);
        src.setSourceType(type);
        src.setActive(true);
        sourceRepo.save(src);
    }

    /**
     * Imports and chunk data from an external source or file.
     *
     * @param sourceName the sourceName input value
     * @param text the text input value
     */
    @Transactional
    public void importAndChunk(String sourceName, String text) {
        PlatformKnowledgeSource src = sourceRepo.findAll().stream()
                .filter(s -> s.getSourceName().equals(sourceName))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Source not found"));

        // Split text into chunks
        String[] parts = text.split("\n\n");
        for (int i = 0; i < parts.length; i++) {
            PlatformKnowledgeChunk chunk = new PlatformKnowledgeChunk();
            chunk.setSourceId(src.getId());
            chunk.setChunkContent(parts[i]);
            chunk.setVectorPlaceholder("emb-placeholder-" + i);
            chunkRepo.save(chunk);
        }
    }
}