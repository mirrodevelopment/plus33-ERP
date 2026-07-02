package com.plus33.erp.routing.dispatch.simulation;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class RouteSimulationEngine {
    @Autowired PlatformRouteSimulationRunRepository simulationRepo;

    @Transactional
    public PlatformRouteSimulationRun runSimulation(String scenario, String baseRoute, String optimizedRoute) {
        PlatformRouteSimulationRun run = new PlatformRouteSimulationRun();
        run.setScenarioName(scenario);
        run.setBaselineRoute(baseRoute);
        run.setOptimizedRoute(optimizedRoute);
        run.setTravelTimeMins(180);
        run.setFuelCost(BigDecimal.valueOf(110.00));
        run.setCarbonOutputKg(BigDecimal.valueOf(28.50));
        run.setDelayProbability(BigDecimal.valueOf(12.40));
        run.setSimulationScore(BigDecimal.valueOf(95.50));
        run.setAlgorithmVersion("v57.0-MONTECARLO");
        run.setSimulatedAt(LocalDateTime.now());
        return simulationRepo.save(run);
    }
}