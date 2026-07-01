package com.plus33.erp.intelligence.api;

import com.plus33.erp.intelligence.predictive.MaintenanceForecaster;
import com.plus33.erp.intelligence.predictive.ForecastModelRegistry;
import com.plus33.erp.intelligence.optimization.CrossModuleOptimizer;
import com.plus33.erp.intelligence.optimization.ReinforcementPolicyEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/intelligence/predictive")
public class PredictionController {
    @Autowired MaintenanceForecaster maintenanceForecaster;
    @Autowired ForecastModelRegistry forecastModelRegistry;
    @Autowired CrossModuleOptimizer crossModuleOptimizer;
    @Autowired ReinforcementPolicyEngine reinforcementPolicyEngine;

    @PostMapping("/models")
    public ResponseEntity<Void> registerForecastModel(
            @RequestParam String code,
            @RequestParam BigDecimal score) {
        forecastModelRegistry.registerModel(code, score);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forecast")
    public ResponseEntity<Void> generateForecast(
            @RequestParam Long modelId,
            @RequestParam Long instanceId) {
        maintenanceForecaster.generateForecast(modelId, instanceId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/optimize")
    public ResponseEntity<Void> registerOptimizationStrategy(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String params) {
        crossModuleOptimizer.registerStrategy(code, name, params);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/train")
    public ResponseEntity<Void> trainRlPolicy(
            @RequestParam String code,
            @RequestParam String action,
            @RequestParam BigDecimal reward) {
        reinforcementPolicyEngine.trainPolicy(code, action, reward);
        return ResponseEntity.ok().build();
    }
}