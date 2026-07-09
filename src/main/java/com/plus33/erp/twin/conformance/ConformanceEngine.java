/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Twin Module
 * Package           : com.plus33.erp.twin.conformance
 * File              : ConformanceEngine.java
 * Purpose           : Business logic service layer for Twin Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ConformanceEngineController
 * Related Service   : ConformanceEngine
 * Related Repository: ConformanceEngineRepository
 * Related Entity    : ConformanceEngine
 * Related DTO       : N/A
 * Related Mapper    : ConformanceEngineMapper
 * Related DB Table  : conformance_engines
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : ConformanceEngineController, ConformanceEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Twin Module. Implements ConformanceEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.twin.conformance;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Twin Module</b>
 *
 * <p><b>Class  :</b> {@code ConformanceEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.twin.conformance}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Twin Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ConformanceEngineController
 *   --> ConformanceEngine (this)
 *   --> Validate business rules
 *   --> ConformanceEngineRepository (read/write 'conformance_engines')
 *   --> ConformanceEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code conformance_engines}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ConformanceEngine {
    @Autowired PlatformConformanceRuleRepository ruleRepo;
    @Autowired PlatformConformanceDeviationRepository deviationRepo;
    @Autowired SlaPredictionService slaPredictor;
    /**
     * Validates business rules and constraints for conformance.
     *
     * @param caseId the caseId input value
     * @param processName the processName input value
     * @param transitionActivities the transitionActivities input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void checkConformance(Long caseId, String processName, List<String> transitionActivities) {
        List<PlatformConformanceRule> rules = ruleRepo.findAll().stream()
                .filter(r -> r.getProcessName().equals(processName))
                .toList();

        for (int i = 0; i < Math.min(rules.size(), transitionActivities.size()); i++) {
            PlatformConformanceRule rule = rules.get(i);
            String activity = transitionActivities.get(i);

            if (!rule.getExpectedActivity().equals(activity)) {
                PlatformConformanceDeviation deviation = new PlatformConformanceDeviation();
                deviation.setCaseId(caseId);
                deviation.setRuleId(rule.getId());
                deviation.setDeviationDetails("Expected activity " + rule.getExpectedActivity() + " but got " + activity);
                deviation.setSlaBreachRisk(slaPredictor.predictSlaBreach(processName, activity));
                deviation.setDetectedAt(LocalDateTime.now());
                deviationRepo.save(deviation);
            }
        }
    }
}