/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.api
 * File              : GraphController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Intelligence Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GraphController
 * Related Service   : GraphControllerService, GraphControllerServiceImpl
 * Related Repository: GraphControllerRepository
 * Related Entity    : GraphController
 * Related DTO       : N/A
 * Related Mapper    : GraphControllerMapper
 * Related DB Table  : graph_controllers
 * Related REST APIs : POST /api/intelligence/graph/node, POST /api/intelligence/graph/edge, POST /api/intelligence/graph/relation, GET /api/intelligence/graph/traverse
 * Depends On        : Platform Module
 * Used By           : Intelligence Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Intelligence Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/intelligence/graph/node, POST /api/intelligence/graph/edge, POST /api/intelligence/graph/relation, GET /api/intelligence/graph/traverse
 ******************************************************************************/
package com.plus33.erp.intelligence.api;

import com.plus33.erp.intelligence.graph.KnowledgeGraphService;
import com.plus33.erp.intelligence.semantic.TwinRelationshipService;
import com.plus33.erp.intelligence.semantic.SemanticTraversalService;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code GraphController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.api}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to GraphService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> GraphController.endpoint()
 *   --> GraphService.method()
 *   --> GraphRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/intelligence/graph/node, POST /api/intelligence/graph/edge, POST /api/intelligence/graph/relation, GET /api/intelligence/graph/traverse</p>
 * <p><b>Module Deps      :</b> Intelligence, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/intelligence/graph")
public class GraphController {
    @Autowired KnowledgeGraphService graphService;
    @Autowired TwinRelationshipService twinRelationshipService;
    @Autowired SemanticTraversalService semanticTraversalService;
    /**
     * Creates a new node and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/node")
    public ResponseEntity<Void> addNode(
            @RequestParam String type,
            @RequestParam String key,
            @RequestParam String name,
            @RequestParam String module,
            @RequestParam(required = false) String metadata) {
        graphService.addNode(type, key, name, module, metadata);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new edge and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/edge")
    public ResponseEntity<Void> addEdge(
            @RequestParam Long source,
            @RequestParam Long target,
            @RequestParam String type) {
        graphService.addEdge(source, target, type);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new relation and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/relation")
    public ResponseEntity<Void> addRelation(
            @RequestParam Long source,
            @RequestParam Long target,
            @RequestParam String type) {
        twinRelationshipService.relate(source, target, type);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the traverse operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     */
    @GetMapping("/traverse")
    public ResponseEntity<List<PlatformTwinRelation>> traverse(
            @RequestParam Long instanceId) {
        return ResponseEntity.ok(semanticTraversalService.findDependencies(instanceId));
    }
}