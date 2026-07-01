package com.plus33.erp.twin.decision;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.twin.execution.ExecutionCoordinator;
import com.plus33.erp.twin.approval.ApprovalEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class DecisionEngine {
    @Autowired RecommendationEngine recommendationEngine;
    @Autowired ApprovalEngine approvalEngine;
    @Autowired ExecutionCoordinator executionCoordinator;
    @Autowired PlatformAutonomousExecutionRepository executionRepo;

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