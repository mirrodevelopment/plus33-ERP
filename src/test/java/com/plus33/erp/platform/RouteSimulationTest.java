package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.dispatch.simulation.RouteSimulationEngine;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RouteSimulationTest {

    @Autowired RouteSimulationEngine simulationEngine;

    @Autowired PlatformRouteSimulationRunRepository simulationRepo;

    @Test
    void testRouteSimulationScenarios() {
        // Route simulations scenario analysis over 40 iterations
        for (int i = 1; i <= 40; i++) {
            simulationEngine.runSimulation("Sim Scenario " + i, "ROUTE-BASE-X", "ROUTE-OPTIMIZED-Y");
        }

        List<PlatformRouteSimulationRun> runs = simulationRepo.findAll();
        assertTrue(runs.size() >= 40);
        assertEquals("Sim Scenario 1", runs.get(0).getScenarioName());
        assertEquals("v57.0-MONTECARLO", runs.get(0).getAlgorithmVersion());
    }
}
