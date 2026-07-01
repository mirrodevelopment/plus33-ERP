package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.twin.decision.DecisionEngine;
import com.plus33.erp.twin.simulation.SimulationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AutonomousDecisionTest {

    @Autowired DecisionEngine decisionEngine;
    @Autowired SimulationService simulationService;

    @Autowired PlatformAutonomousActionRepository actionRepo;
    @Autowired PlatformAutonomousExecutionRepository executionRepo;
    @Autowired PlatformTwinSimulationScenarioRepository scenarioRepo;
    @Autowired PlatformTwinSimulationResultRepository resultRepo;
    @Autowired PlatformTwinPredictionSnapshotRepository snapshotRepo;
    @Autowired PlatformTwinConfigVersionRepository configRepo;
    @Autowired PlatformTwinConfigHistoryRepository configHistoryRepo;

    @Test
    void testAutonomousDecisionScenarios() {
        // Register action
        PlatformAutonomousAction action = new PlatformAutonomousAction();
        action.setActionCode("AUTO_PUMP_SHUTDOWN");
        action.setDescription("Trigger shutdown of pump when temperature exceeds thresholds limit");
        action.setActive(true);
        action = actionRepo.save(action);

        // Run 40 evaluations (Auto threshold checks)
        for (int i = 1; i <= 40; i++) {
            decisionEngine.evaluateDecision(action, BigDecimal.valueOf(98.50));
        }
        List<PlatformAutonomousExecution> executions = executionRepo.findAll();
        assertTrue(executions.size() >= 40);

        // Run scenario simulations 40 times
        simulationService.registerScenario("SCEN_001", "staffing simulation", "{}");
        PlatformTwinSimulationScenario scenario = scenarioRepo.findAll().get(0);

        for (int i = 1; i <= 40; i++) {
            simulationService.runSimulation(scenario.getId(), 1L);
        }
        List<PlatformTwinSimulationResult> results = resultRepo.findAll();
        assertTrue(results.size() >= 40);

        List<PlatformTwinPredictionSnapshot> snapshots = snapshotRepo.findAll();
        assertTrue(snapshots.size() >= 40);

        // Save 40 configuration histories
        for (int i = 1; i <= 40; i++) {
            PlatformTwinConfigHistory history = new PlatformTwinConfigHistory();
            history.setInstanceId(1L);
            history.setPreviousVersion("v1.0." + (i-1));
            history.setNewVersion("v1.0." + i);
            history.setOperator("grc-operator");
            history.setReason("Calibration run " + i);
            configHistoryRepo.save(history);
        }
        List<PlatformTwinConfigHistory> histories = configHistoryRepo.findAll();
        assertTrue(histories.size() >= 40);
    }
}
