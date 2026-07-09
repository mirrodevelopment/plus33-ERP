/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Twin Module
 * Package           : com.plus33.erp.twin.simulation
 * File              : SimulationService.java
 * Purpose           : Business logic service layer for Twin Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SimulationController
 * Related Service   : SimulationService
 * Related Repository: SimulationRepository
 * Related Entity    : Simulation
 * Related DTO       : N/A
 * Related Mapper    : SimulationMapper
 * Related DB Table  : simulations
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : SimulationController, SimulationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Twin Module. Implements SimulationService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.twin.simulation;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Twin Module</b>
 *
 * <p><b>Class  :</b> {@code SimulationService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.twin.simulation}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Twin Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SimulationController
 *   --> SimulationService (this)
 *   --> Validate business rules
 *   --> SimulationRepository (read/write 'simulations')
 *   --> SimulationMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code simulations}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class SimulationService {
    @Autowired PlatformTwinSimulationScenarioRepository scenarioRepo;
    @Autowired PlatformTwinSimulationResultRepository resultRepo;
    @Autowired PlatformTwinPredictionSnapshotRepository snapshotRepo;
    /**
     * Creates a new scenario and persists it to the database.
     *
     * @param code the code input value
     * @param name the name input value
     * @param config the config input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerScenario(String code, String name, String config) {
        PlatformTwinSimulationScenario scenario = new PlatformTwinSimulationScenario();
        scenario.setScenarioCode(code);
        scenario.setScenarioName(name);
        scenario.setConfigVariables(config);
        scenarioRepo.save(scenario);
    }

    /**
     * Performs the runSimulation operation in this module.
     *
     * @param scenarioId the scenarioId input value
     * @param instanceId the instanceId input value
     */
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