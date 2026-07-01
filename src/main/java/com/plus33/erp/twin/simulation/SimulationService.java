package com.plus33.erp.twin.simulation;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class SimulationService {
    @Autowired PlatformTwinSimulationScenarioRepository scenarioRepo;
    @Autowired PlatformTwinSimulationResultRepository resultRepo;
    @Autowired PlatformTwinPredictionSnapshotRepository snapshotRepo;

    @Transactional
    public void registerScenario(String code, String name, String config) {
        PlatformTwinSimulationScenario scenario = new PlatformTwinSimulationScenario();
        scenario.setScenarioCode(code);
        scenario.setScenarioName(name);
        scenario.setConfigVariables(config);
        scenarioRepo.save(scenario);
    }

    @Transactional
    public void runSimulation(Long scenarioId, Long instanceId) {
        PlatformTwinSimulationResult res = new PlatformTwinSimulationResult();
        res.setScenarioId(scenarioId);
        res.setSimulationOutput("Warehouse throughput simulation results");
        res.setConfidence(BigDecimal.valueOf(95.0));
        res.setSimulatedAt(LocalDateTime.now());
        resultRepo.save(res);

        PlatformTwinPredictionSnapshot snap = new PlatformTwinPredictionSnapshot();
        snap.setInstanceId(instanceId);
        snap.setTargetTime(LocalDateTime.now().plusDays(7));
        snap.setPredictedStateJson("{\"predicted_temperature\": 85.50}");
        snap.setConfidence(BigDecimal.valueOf(92.50));
        snap.setCreatedAt(LocalDateTime.now());
        snapshotRepo.save(snap);
    }
}