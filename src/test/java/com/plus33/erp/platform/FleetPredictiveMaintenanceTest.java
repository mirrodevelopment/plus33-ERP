package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.predictive.policy.PredictiveMaintenancePolicyService;

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
public class FleetPredictiveMaintenanceTest {

    @Autowired PredictiveMaintenancePolicyService policyService;

    @Autowired PlatformPredictiveMaintenancePolicyRepository policyRepo;

    @Test
    void testFleetPredictiveMaintenanceScenarios() {
        // Predictive maintenance policies registry over 40 iterations
        for (int i = 1; i <= 40; i++) {
            PlatformPredictiveMaintenancePolicy policy = policyService.createPolicy("PM_POLICY_" + i, "Fleet Asset Policy " + i, "MOTOR", "Predictive");
            assertNotNull(policy);
        }

        List<PlatformPredictiveMaintenancePolicy> policies = policyRepo.findAll();
        assertTrue(policies.size() >= 40);
        assertEquals("Predictive", policies.get(0).getMaintenanceStrategy());
        assertEquals("RANDOM_FOREST_PROB", policies.get(0).getPredictionModel());
    }
}
