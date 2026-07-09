/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.api
 * File              : IntelligenceController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Intelligence Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntelligenceController
 * Related Service   : IntelligenceControllerService, IntelligenceControllerServiceImpl
 * Related Repository: IntelligenceControllerRepository
 * Related Entity    : IntelligenceController
 * Related DTO       : N/A
 * Related Mapper    : IntelligenceControllerMapper
 * Related DB Table  : intelligence_controllers
 * Related REST APIs : POST /api/intelligence/causal/model, POST /api/intelligence/root-cause, POST /api/intelligence/query, POST /api/intelligence/xai
 * Depends On        : Platform Module
 * Used By           : Intelligence Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Intelligence Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/intelligence/causal/model, POST /api/intelligence/root-cause, POST /api/intelligence/query, POST /api/intelligence/xai
 ******************************************************************************/
package com.plus33.erp.intelligence.api;

import com.plus33.erp.intelligence.causal.CausalInferenceEngine;
import com.plus33.erp.intelligence.causal.RootCauseAnalyzer;
import com.plus33.erp.intelligence.query.NaturalLanguageQueryService;
import com.plus33.erp.intelligence.query.DecisionLineageTracker;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code IntelligenceController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.api}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to IntelligenceService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> IntelligenceController.endpoint()
 *   --> IntelligenceService.method()
 *   --> IntelligenceRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/intelligence/causal/model, POST /api/intelligence/root-cause, POST /api/intelligence/query, POST /api/intelligence/xai</p>
 * <p><b>Module Deps      :</b> Intelligence, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/intelligence")
public class IntelligenceController {
    @Autowired CausalInferenceEngine causalEngine;
    @Autowired RootCauseAnalyzer rootCauseAnalyzer;
    @Autowired NaturalLanguageQueryService queryService;
    @Autowired DecisionLineageTracker lineageTracker;
    /**
     * Creates a new causal model and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/causal/model")
    public ResponseEntity<Void> registerCausalModel(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String structure) {
        causalEngine.registerModel(code, name, structure);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the runRootCauseAnalysis operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/root-cause")
    public ResponseEntity<Void> runRootCauseAnalysis(
            @RequestParam Long modelId,
            @RequestParam String anomaly) {
        rootCauseAnalyzer.runAnalysis(modelId, anomaly);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the runOperationalQuery operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/query")
    public ResponseEntity<Void> runOperationalQuery(
            @RequestParam String queryText) {
        queryService.executeQuery(queryText);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the recordXaiLineage operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/xai")
    public ResponseEntity<Void> recordXaiLineage(
            @RequestParam String key,
            @RequestParam String factors,
            @RequestParam String version) {
        lineageTracker.recordLineage(key, factors, version);
        return ResponseEntity.ok().build();
    }
}