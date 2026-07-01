package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.logistics.routing.AutonomousReroutingCoordinator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AutonomousRoutingTest {

    @Autowired AutonomousReroutingCoordinator coordinator;

    @Autowired PlatformReroutingPolicyRepository policyRepo;
    @Autowired PlatformAutonomousReroutingRepository reroutingRepo;
    @Autowired PlatformReroutingExecutionRepository executionRepo;
    @Autowired PlatformRouteAuditLogRepository auditRepo;

    @Test
    void testAutonomousRoutingScenarios() {
        // Save rerouting policy
        PlatformReroutingPolicy policy = new PlatformReroutingPolicy();
        policy.setPolicyCode("POLICY_CONGESTION_REROUTE");
        policy.setTriggerThresholdMinutes(30);
        policy = policyRepo.save(policy);

        // Run 40 autonomous rerouting coordinator updates
        for (int i = 1; i <= 40; i++) {
            coordinator.executeReroute((long) i, policy.getId(), "{\"alternative_route\": [\"node-a\", \"node-b\"]}");
        }

        List<PlatformAutonomousRerouting> reroutes = reroutingRepo.findAll();
        assertTrue(reroutes.size() >= 40);

        List<PlatformReroutingExecution> executions = executionRepo.findAll();
        assertTrue(executions.size() >= 40);

        List<PlatformRouteAuditLog> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);
        assertEquals("sys-routing-coordinator", audits.get(0).getOperator());
    }
}
