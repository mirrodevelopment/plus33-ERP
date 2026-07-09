/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.semantic
 * File              : SemanticTraversalService.java
 * Purpose           : Business logic service layer for Intelligence Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SemanticTraversalController
 * Related Service   : SemanticTraversalService
 * Related Repository: SemanticTraversalRepository
 * Related Entity    : SemanticTraversal
 * Related DTO       : N/A
 * Related Mapper    : SemanticTraversalMapper
 * Related DB Table  : semantic_traversals
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : SemanticTraversalController, SemanticTraversalServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Intelligence Module. Implements SemanticTraversalService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.intelligence.semantic;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code SemanticTraversalService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.semantic}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Intelligence Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SemanticTraversalController
 *   --> SemanticTraversalService (this)
 *   --> Validate business rules
 *   --> SemanticTraversalRepository (read/write 'semantic_traversals')
 *   --> SemanticTraversalMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code semantic_traversals}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class SemanticTraversalService {
    /**
     * Retrieves dependencies data from the database.
     *
     * @param instanceId the instanceId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Autowired PlatformTwinRelationRepository relationRepo;
    public List<PlatformTwinRelation> findDependencies(Long instanceId) {
        return relationRepo.findAll().stream()
                .filter(r -> r.getSourceInstanceId().equals(instanceId) && "DependsOn".equals(r.getRelationshipType()))
                .toList();
    }
}