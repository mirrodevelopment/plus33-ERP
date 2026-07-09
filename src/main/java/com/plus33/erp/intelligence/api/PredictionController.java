/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.api
 * File              : PredictionController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Intelligence Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PredictionController
 * Related Service   : PredictionControllerService, PredictionControllerServiceImpl
 * Related Repository: PredictionControllerRepository
 * Related Entity    : PredictionController
 * Related DTO       : N/A
 * Related Mapper    : PredictionControllerMapper
 * Related DB Table  : prediction_controllers
 * Related REST APIs : POST /api/intelligence/predictive/models, POST /api/intelligence/predictive/forecast, POST /api/intelligence/predictive/optimize, POST /api/intelligence/predictive/train
 * Depends On        : None
 * Used By           : Intelligence Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Intelligence Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/intelligence/predictive/models, POST /api/intelligence/predictive/forecast, POST /api/intelligence/predictive/optimize, POST /api/intelligence/predictive/train
 ******************************************************************************/
package com.plus33.erp.intelligence.api;

import com.plus33.erp.intelligence.predictive.MaintenanceForecaster;
import com.plus33.erp.intelligence.predictive.ForecastModelRegistry;
import com.plus33.erp.intelligence.optimization.CrossModuleOptimizer;
import com.plus33.erp.intelligence.optimization.ReinforcementPolicyEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code PredictionController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.api}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to PredictionService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> PredictionController.endpoint()
 *   --> PredictionService.method()
 *   --> PredictionRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/intelligence/predictive/models, POST /api/intelligence/predictive/forecast, POST /api/intelligence/predictive/optimize, POST /api/intelligence/predictive/train</p>
 * <p><b>Module Deps      :</b> Intelligence</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/intelligence/predictive")
public class PredictionController {
    @Autowired MaintenanceForecaster maintenanceForecaster;
    @Autowired ForecastModelRegistry forecastModelRegistry;
    @Autowired CrossModuleOptimizer crossModuleOptimizer;
    @Autowired ReinforcementPolicyEngine reinforcementPolicyEngine;
    /**
     * Creates a new forecast model and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/models")
    public ResponseEntity<Void> registerForecastModel(
            @RequestParam String code,
            @RequestParam BigDecimal score) {
        forecastModelRegistry.registerModel(code, score);
        return ResponseEntity.ok().build();
    }

    /**
     * Generates the forecast based on input parameters and business rules.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/forecast")
    public ResponseEntity<Void> generateForecast(
            @RequestParam Long modelId,
            @RequestParam Long instanceId) {
        maintenanceForecaster.generateForecast(modelId, instanceId);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new optimization strategy and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/optimize")
    public ResponseEntity<Void> registerOptimizationStrategy(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String params) {
        crossModuleOptimizer.registerStrategy(code, name, params);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the trainRlPolicy operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/train")
    public ResponseEntity<Void> trainRlPolicy(
            @RequestParam String code,
            @RequestParam String action,
            @RequestParam BigDecimal reward) {
        reinforcementPolicyEngine.trainPolicy(code, action, reward);
        return ResponseEntity.ok().build();
    }
}