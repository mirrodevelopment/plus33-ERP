/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Routing Module
 * Package           : com.plus33.erp.routing.dispatch.controller
 * File              : DispatchController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Routing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DispatchController
 * Related Service   : DispatchControllerService, DispatchControllerServiceImpl
 * Related Repository: DispatchControllerRepository
 * Related Entity    : DispatchController
 * Related DTO       : N/A
 * Related Mapper    : DispatchControllerMapper
 * Related DB Table  : dispatch_controllers
 * Related REST APIs : POST /api/routing/dispatch/policies, POST /api/routing/dispatch/assignments, POST /api/routing/dispatch/simulation/run, POST /api/routing/dispatch/constraint/verify
 * Depends On        : Platform Module
 * Used By           : Routing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Routing Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/routing/dispatch/policies, POST /api/routing/dispatch/assignments, POST /api/routing/dispatch/simulation/run, POST /api/routing/dispatch/constraint/verify
 ******************************************************************************/
package com.plus33.erp.routing.dispatch.controller;

import com.plus33.erp.routing.dispatch.engine.DispatchOptimizationEngine;
import com.plus33.erp.routing.dispatch.simulation.RouteSimulationEngine;
import com.plus33.erp.routing.dispatch.constraint.DispatchConstraintSolver;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <b>PLUS33 Coffee ERP -- Routing Module</b>
 *
 * <p><b>Class  :</b> {@code DispatchController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.routing.dispatch.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to DispatchService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> DispatchController.endpoint()
 *   --> DispatchService.method()
 *   --> DispatchRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/routing/dispatch/policies, POST /api/routing/dispatch/assignments, POST /api/routing/dispatch/simulation/run, POST /api/routing/dispatch/constraint/verify</p>
 * <p><b>Module Deps      :</b> Routing, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/routing/dispatch")
public class DispatchController {
    @Autowired DispatchOptimizationEngine dispatchEngine;
    @Autowired RouteSimulationEngine simulationEngine;
    @Autowired DispatchConstraintSolver constraintSolver;
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
            @RequestParam String strategy) {
        dispatchEngine.createPolicy(code, strategy);
        return ResponseEntity.ok().build();
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/assignments")
    public ResponseEntity<Void> dispatchJob(
            @RequestParam String code,
            @RequestParam Long vehicleId,
            @RequestParam Long driverId,
            @RequestParam Long routeId,
            @RequestParam Long shipmentId) {
        dispatchEngine.dispatchJob(code, vehicleId, driverId, routeId, shipmentId);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the runSimulation operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/simulation/run")
    public ResponseEntity<Void> runSimulation(
            @RequestParam String scenario,
            @RequestParam String baseRoute,
            @RequestParam String optimizedRoute) {
        simulationEngine.runSimulation(scenario, baseRoute, optimizedRoute);
        return ResponseEntity.ok().build();
    }

    /**
     * Validates business rules and constraints for constraints.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/constraint/verify")
    public ResponseEntity<Void> verifyConstraints(
            @RequestParam Long dispatchId,
            @RequestParam String type) {
        constraintSolver.verifyConstraints(dispatchId, type);
        return ResponseEntity.ok().build();
    }
}