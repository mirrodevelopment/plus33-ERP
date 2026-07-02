package com.plus33.erp.routing.dispatch.controller;

import com.plus33.erp.routing.dispatch.engine.DispatchOptimizationEngine;
import com.plus33.erp.routing.dispatch.simulation.RouteSimulationEngine;
import com.plus33.erp.routing.dispatch.constraint.DispatchConstraintSolver;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routing/dispatch")
public class DispatchController {
    @Autowired DispatchOptimizationEngine dispatchEngine;
    @Autowired RouteSimulationEngine simulationEngine;
    @Autowired DispatchConstraintSolver constraintSolver;

    @PostMapping("/policies")
    public ResponseEntity<Void> createPolicy(
            @RequestParam String code,
            @RequestParam String strategy) {
        dispatchEngine.createPolicy(code, strategy);
        return ResponseEntity.ok().build();
    }

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

    @PostMapping("/simulation/run")
    public ResponseEntity<Void> runSimulation(
            @RequestParam String scenario,
            @RequestParam String baseRoute,
            @RequestParam String optimizedRoute) {
        simulationEngine.runSimulation(scenario, baseRoute, optimizedRoute);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/constraint/verify")
    public ResponseEntity<Void> verifyConstraints(
            @RequestParam Long dispatchId,
            @RequestParam String type) {
        constraintSolver.verifyConstraints(dispatchId, type);
        return ResponseEntity.ok().build();
    }
}