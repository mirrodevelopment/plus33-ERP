package com.plus33.erp.routing.optimization.controller;

import com.plus33.erp.routing.optimization.policy.RouteOptimizationPolicyService;
import com.plus33.erp.routing.optimization.carbon.CarbonFootprintEstimator;
import com.plus33.erp.routing.optimization.cost.RouteCostMinimizer;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/routing/optimization")
public class RoutingOptimizationController {
    @Autowired RouteOptimizationPolicyService policyService;
    @Autowired CarbonFootprintEstimator carbonEstimator;
    @Autowired RouteCostMinimizer costMinimizer;

    @PostMapping("/policies")
    public ResponseEntity<Void> createPolicy(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String strategy) {
        policyService.createPolicy(code, name, strategy);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/carbon/emissions")
    public ResponseEntity<Void> recordCarbonEmissions(
            @RequestParam Long vehicleId,
            @RequestParam Long routeId,
            @RequestParam String fuelType,
            @RequestParam BigDecimal distance) {
        carbonEstimator.recordCarbonEmissions(vehicleId, routeId, fuelType, distance);
        return ResponseEntity.ok().build();
    }

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