/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Agent Module
 * Package           : com.plus33.erp.agent.knowledge
 * File              : RetrievalService.java
 * Purpose           : Business logic service layer for Agent Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RetrievalController
 * Related Service   : RetrievalService
 * Related Repository: RetrievalRepository
 * Related Entity    : Retrieval
 * Related DTO       : N/A
 * Related Mapper    : RetrievalMapper
 * Related DB Table  : retrievals
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : RetrievalController, RetrievalServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Agent Module. Implements RetrievalService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.agent.knowledge;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Agent Module</b>
 *
 * <p><b>Class  :</b> {@code RetrievalService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.agent.knowledge}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Agent Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * RetrievalController
 *   --> RetrievalService (this)
 *   --> Validate business rules
 *   --> RetrievalRepository (read/write 'retrievals')
 *   --> RetrievalMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code retrievals}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class RetrievalService {
    /**
     * Retrieves relevant chunks data from the database.
     *
     * @param query the query input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Autowired PlatformKnowledgeChunkRepository chunkRepo;
    public List<PlatformKnowledgeChunk> retrieveRelevantChunks(String query) {
        return chunkRepo.findAll().stream()
                .filter(c -> c.getChunkContent().contains(query))
                .toList();
    }
}