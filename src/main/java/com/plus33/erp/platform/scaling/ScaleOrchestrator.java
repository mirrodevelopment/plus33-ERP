/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.scaling
 * File              : ScaleOrchestrator.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ScaleOrchestratorController
 * Related Service   : ScaleOrchestrator
 * Related Repository: ScaleOrchestratorRepository
 * Related Entity    : ScaleOrchestrator
 * Related DTO       : N/A
 * Related Mapper    : ScaleOrchestratorMapper
 * Related DB Table  : scale_orchestrators
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ScaleOrchestratorController, ScaleOrchestratorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements ScaleOrchestratorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.scaling;

import com.plus33.erp.platform.entity.PlatformScalingActivity;
import com.plus33.erp.platform.entity.PlatformScalingDecision;
import com.plus33.erp.platform.entity.PlatformScalingPolicy;
import com.plus33.erp.platform.repository.PlatformScalingActivityRepository;
import com.plus33.erp.platform.repository.PlatformScalingDecisionRepository;
import com.plus33.erp.platform.repository.PlatformScalingPolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code ScaleOrchestrator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.scaling}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ScaleOrchestratorController
 *   --> ScaleOrchestrator (this)
 *   --> Validate business rules
 *   --> ScaleOrchestratorRepository (read/write 'scale_orchestrators')
 *   --> ScaleOrchestratorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code scale_orchestrators}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ScaleOrchestrator {
    @Autowired PlatformScalingPolicyRepository policyRepo;
    @Autowired PlatformScalingActivityRepository activityRepo;
    @Autowired PlatformScalingDecisionRepository decisionRepo;
    /**
     * Performs the evaluate operation in this module.
     *
     * @param metricName the metricName input value
     * @param currentValue the currentValue input value
     * @param currentReplicas the currentReplicas input value
     */
    @Transactional
    public void evaluate(String metricName, double currentValue, int currentReplicas) {
        PlatformScalingPolicy policy = policyRepo.findAll().stream()
                .filter(p -> p.getMetricName().equals(metricName))
                .findFirst().orElse(null);

        if (policy == null) return;

        if (BigDecimal.valueOf(currentValue).compareTo(policy.getThresholdValue()) > 0) {
            int desired = Math.min(currentReplicas + 2, policy.getMaxReplicas());
            if (desired > currentReplicas) {
                PlatformScalingDecision dec = new PlatformScalingDecision();
                dec.setMetricName(metricName);
                dec.setCurrentValue(BigDecimal.valueOf(currentValue));
                dec.setThresholdValue(policy.getThresholdValue());
                dec.setCurrentReplicas(currentReplicas);
                dec.setDesiredReplicas(desired);
                dec.setReason("Metric " + metricName + " exceeded threshold of " + policy.getThresholdValue());
                dec.setTimestamp(LocalDateTime.now());
                decisionRepo.save(dec);

                PlatformScalingActivity act = new PlatformScalingActivity();
                act.setActivityType("SCALE_UP");
                act.setCurrentReplicas(currentReplicas);
                act.setDesiredReplicas(desired);
                act.setStatus("COMPLETED");
                act.setStartedAt(LocalDateTime.now());
                act.setCompletedAt(LocalDateTime.now());
                activityRepo.save(act);
            }
        }
    }
}