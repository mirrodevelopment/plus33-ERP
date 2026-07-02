package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.optimization.policy.RouteOptimizationPolicyService;

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
public class RouteOptimizationTest {

    @Autowired RouteOptimizationPolicyService policyService;

    @Autowired PlatformRouteOptimizationPolicyRepository policyRepo;

    @Test
    void testRouteOptimizationScenarios() {
        // Route optimization policies registry over 40 iterations
        for (int i = 1; i <= 40; i++) {
            PlatformRouteOptimizationPolicy policy = policyService.createPolicy("RO_POLICY_" + i, "Fleet Routing Policy " + i, "MinCost");
            assertNotNull(policy);
        }

        List<PlatformRouteOptimizationPolicy> policies = policyRepo.findAll();
        assertTrue(policies.size() >= 40);
        assertEquals("MinCost", policies.get(0).getOptimizationStrategy());
        assertEquals("CAPACITY_MATCHING_VEHICLES", policies.get(0).getVehicleConstraints());
    }
}
