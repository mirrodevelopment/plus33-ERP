package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.intelligence.predictive.MaintenanceForecaster;
import com.plus33.erp.intelligence.predictive.ForecastModelRegistry;
import com.plus33.erp.intelligence.optimization.CrossModuleOptimizer;
import com.plus33.erp.intelligence.optimization.ReinforcementPolicyEngine;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PredictiveMaintenanceTest {

    @Autowired MaintenanceForecaster forecaster;
    @Autowired ForecastModelRegistry forecastModelRegistry;
    @Autowired CrossModuleOptimizer crossModuleOptimizer;
    @Autowired ReinforcementPolicyEngine policyEngine;

    @Autowired PlatformForecastModelRegistryRepository modelRepo;
    @Autowired PlatformPredictiveMaintenanceForecastRepository forecastRepo;
    @Autowired PlatformOptimizationStrategyRepository strategyRepo;
    @Autowired PlatformRlPolicyUpdateRepository policyUpdateRepo;

    @Test
    void testPredictiveMaintenanceScenarios() {
        // Register forecast models over 40 iterations
        for (int i = 1; i <= 40; i++) {
            forecastModelRegistry.registerModel("FORECAST_MODEL_" + i, BigDecimal.valueOf(95.00));
        }
        List<PlatformForecastModelRegistry> models = modelRepo.findAll();
        assertTrue(models.size() >= 40);

        // Generate forecasts over 40 iterations
        PlatformForecastModelRegistry testModel = models.get(0);
        for (int i = 1; i <= 40; i++) {
            forecaster.generateForecast(testModel.getId(), 1L);
        }
        List<PlatformPredictiveMaintenanceForecast> forecasts = forecastRepo.findAll();
        assertTrue(forecasts.size() >= 40);

        // Register cross-module optimization strategies over 40 iterations
        for (int i = 1; i <= 40; i++) {
            crossModuleOptimizer.registerStrategy("OPT_STRATEGY_" + i, "Staffing optimization", "{}");
        }
        List<PlatformOptimizationStrategy> strategies = strategyRepo.findAll();
        assertTrue(strategies.size() >= 40);

        // Train RL reinforcement learning policies over 40 iterations
        for (int i = 1; i <= 40; i++) {
            policyEngine.trainPolicy("POLICY_COOLING_SYSTEM", "SHUTDOWN", BigDecimal.valueOf(10.00 * i));
        }
        List<PlatformRlPolicyUpdate> updates = policyUpdateRepo.findAll();
        assertTrue(updates.size() >= 40);
    }
}
