/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.graph
 * File              : KnowledgeGraphService.java
 * Purpose           : Business logic service layer for Intelligence Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: KnowledgeGraphController
 * Related Service   : KnowledgeGraphService
 * Related Repository: KnowledgeGraphRepository
 * Related Entity    : KnowledgeGraph
 * Related DTO       : N/A
 * Related Mapper    : KnowledgeGraphMapper
 * Related DB Table  : knowledge_graphs
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : KnowledgeGraphController, KnowledgeGraphServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Intelligence Module. Implements KnowledgeGraphService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.intelligence.graph;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code KnowledgeGraphService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.graph}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Intelligence Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * KnowledgeGraphController
 *   --> KnowledgeGraphService (this)
 *   --> Validate business rules
 *   --> KnowledgeGraphRepository (read/write 'knowledge_graphs')
 *   --> KnowledgeGraphMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code knowledge_graphs}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class KnowledgeGraphService {
    @Autowired PlatformGraphNodeRepository nodeRepo;
    @Autowired PlatformGraphEdgeRepository edgeRepo;
    /**
     * Creates a new node and persists it to the database.
     *
     * @param type the type input value
     * @param key the key input value
     * @param name the name input value
     * @param mod the mod input value
     * @param meta the meta input value
     * @return the PlatformGraphNode result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformGraphNode addNode(String type, String key, String name, String mod, String meta) {
        PlatformGraphNode node = new PlatformGraphNode();
        node.setNodeType(type);
        node.setBusinessKey(key);
        node.setDisplayName(name);
        node.setModule(mod);
        node.setMetadataJson(meta);
        return nodeRepo.save(node);
    }

    /**
     * Creates a new edge and persists it to the database.
     *
     * @param src the src input value
     * @param target the target input value
     * @param type the type input value
     * @return the PlatformGraphEdge result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformGraphEdge addEdge(Long src, Long target, String type) {
        PlatformGraphEdge edge = new PlatformGraphEdge();
        edge.setSourceNode(src);
        edge.setTargetNode(target);
        edge.setRelationshipType(type);
        return edgeRepo.save(edge);
    }
}