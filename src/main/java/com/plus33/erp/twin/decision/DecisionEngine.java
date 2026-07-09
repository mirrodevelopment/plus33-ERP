/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Twin Module
 * Package           : com.plus33.erp.twin.decision
 * File              : DecisionEngine.java
 * Purpose           : Business logic service layer for Twin Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DecisionEngineController
 * Related Service   : DecisionEngine
 * Related Repository: DecisionEngineRepository
 * Related Entity    : DecisionEngine
 * Related DTO       : N/A
 * Related Mapper    : DecisionEngineMapper
 * Related DB Table  : decision_engines
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : DecisionEngineController, DecisionEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Twin Module. Implements DecisionEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.twin.decision;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.twin.execution.ExecutionCoordinator;
import com.plus33.erp.twin.approval.ApprovalEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Twin Module</b>
 *
 * <p><b>Class  :</b> {@code DecisionEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.twin.decision}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Twin Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DecisionEngineController
 *   --> DecisionEngine (this)
 *   --> Validate business rules
 *   --> DecisionEngineRepository (read/write 'decision_engines')
 *   --> DecisionEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code decision_engines}</p>
 * <p><b>Module Deps      :</b> Platform, Twin</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DecisionEngine {
    @Autowired RecommendationEngine recommendationEngine;
    @Autowired ApprovalEngine approvalEngine;
    @Autowired ExecutionCoordinator executionCoordinator;
    @Autowired PlatformAutonomousExecutionRepository executionRepo;
    /**
     * Performs the evaluateDecision operation in this module.
     *
     * @param action the action input value
     * @param confidence the confidence input value
     */
    @Transactional
    public void evaluateDecision(PlatformAutonomousAction action, BigDecimal confidence) {
        String policy = confidence.compareTo(BigDecimal.valueOf(98.0)) >= 0 ? "AUTO" : 
                       (confidence.compareTo(BigDecimal.valueOf(85.0)) >= 0 ? "APPROVAL_REQUIRED" : "RECOMMEND_ONLY");

        PlatformAutonomousExecution exec = new PlatformAutonomousExecution();
        exec.setActionId(action.getId());
        exec.setConfidenceScore(confidence);
        exec.setDecisionPolicy(policy);

        if ("AUTO".equals(policy)) {
            exec.setStatus("EXECUTED");
            executionCoordinator.execute(action);
        } else if ("APPROVAL_REQUIRED".equals(policy)) {
            exec.setStatus("PENDING");
            approvalEngine.requestApproval(exec);
        } else {
            exec.setStatus("REJECTED");
            recommendationEngine.logRecommendation(action, confidence);
        }

        executionRepo.save(exec);
    }
}