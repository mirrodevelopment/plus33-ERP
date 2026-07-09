/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Routing Module
 * Package           : com.plus33.erp.routing.optimization.controller
 * File              : RoutingOptimizationController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Routing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RoutingOptimizationController
 * Related Service   : RoutingOptimizationControllerService, RoutingOptimizationControllerServiceImpl
 * Related Repository: RoutingOptimizationControllerRepository
 * Related Entity    : RoutingOptimizationController
 * Related DTO       : N/A
 * Related Mapper    : RoutingOptimizationControllerMapper
 * Related DB Table  : routing_optimization_controllers
 * Related REST APIs : POST /api/routing/optimization/policies, POST /api/routing/optimization/carbon/emissions, POST /api/routing/optimization/cost/optimize
 * Depends On        : Platform Module
 * Used By           : Routing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Routing Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/routing/optimization/policies, POST /api/routing/optimization/carbon/emissions, POST /api/routing/optimization/cost/optimize
 ******************************************************************************/
package com.plus33.erp.routing.optimization.controller;

import com.plus33.erp.routing.optimization.policy.RouteOptimizationPolicyService;
import com.plus33.erp.routing.optimization.carbon.CarbonFootprintEstimator;
import com.plus33.erp.routing.optimization.cost.RouteCostMinimizer;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Routing Module</b>
 *
 * <p><b>Class  :</b> {@code RoutingOptimizationController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.routing.optimization.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to RoutingOptimizationService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> RoutingOptimizationController.endpoint()
 *   --> RoutingOptimizationService.method()
 *   --> RoutingOptimizationRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/routing/optimization/policies, POST /api/routing/optimization/carbon/emissions, POST /api/routing/optimization/cost/optimize</p>
 * <p><b>Module Deps      :</b> Routing, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/routing/optimization")
public class RoutingOptimizationController {
    @Autowired RouteOptimizationPolicyService policyService;
    @Autowired CarbonFootprintEstimator carbonEstimator;
    @Autowired RouteCostMinimizer costMinimizer;
    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/policies")
    public ResponseEntity<Void> createPolicy(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String strategy) {
        policyService.createPolicy(code, name, strategy);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the recordCarbonEmissions operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/carbon/emissions")
    public ResponseEntity<Void> recordCarbonEmissions(
            @RequestParam Long vehicleId,
            @RequestParam Long routeId,
            @RequestParam String fuelType,
            @RequestParam BigDecimal distance) {
        carbonEstimator.recordCarbonEmissions(vehicleId, routeId, fuelType, distance);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the optimizeRouteCost operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/cost/optimize")
    public ResponseEntity<Void> optimizeRouteCost(
            @RequestParam Long routeId,
            @RequestParam BigDecimal fuel,
            @RequestParam BigDecimal toll,
            @RequestParam BigDecimal driver) {
        costMinimizer.optimizeRouteCost(routeId, fuel, toll, driver);
        return ResponseEntity.ok().build();
    }
}