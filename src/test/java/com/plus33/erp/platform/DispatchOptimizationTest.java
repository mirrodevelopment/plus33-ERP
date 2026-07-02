package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.dispatch.engine.DispatchOptimizationEngine;

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
public class DispatchOptimizationTest {

    @Autowired DispatchOptimizationEngine dispatchEngine;

    @Autowired PlatformDispatchPolicyRepository policyRepo;
    @Autowired PlatformDispatchAssignmentRepository assignmentRepo;
    @Autowired PlatformDispatchAuditLogRepository auditRepo;

    @Test
    void testDispatchOptimizationScenarios() {
        // Dispatch policies and job assignments over 40 iterations
        for (int i = 1; i <= 40; i++) {
            dispatchEngine.createPolicy("DISPATCH_POLICY_" + i, "LowestCost");
            dispatchEngine.dispatchJob("DISPATCH_JOB_" + i, 1L, 1L, 1L, 1L);
        }

        List<PlatformDispatchPolicy> policies = policyRepo.findAll();
        assertTrue(policies.size() >= 40);
        assertEquals("LowestCost", policies.get(0).getDispatchStrategy());

        List<PlatformDispatchAssignment> assignments = assignmentRepo.findAll();
        assertTrue(assignments.size() >= 40);
        assertEquals("ASSIGNED", assignments.get(0).getAssignmentStatus());

        List<PlatformDispatchAuditLog> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);
        assertEquals("v57.0-AI-ENGINE", audits.get(0).getOptimizerVersion());
    }
}
