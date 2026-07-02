package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.fuel.engine.FuelOptimizationEngine;

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
public class FuelOptimizationTest {

    @Autowired FuelOptimizationEngine fuelEngine;

    @Autowired PlatformFuelOptimizationPolicyRepository policyRepo;
    @Autowired PlatformFuelEfficiencyAdvisorRepository advisorRepo;
    @Autowired PlatformFuelAuditLogRepository auditRepo;

    @Test
    void testFuelOptimizationScenarios() {
        // Fuel optimization policies and advisory over 40 iterations
        for (int i = 1; i <= 40; i++) {
            fuelEngine.createPolicy("FUEL_POLICY_" + i, "EcoSpeed");
            fuelEngine.suggestAdvice("ReduceIdle");
        }

        List<PlatformFuelOptimizationPolicy> policies = policyRepo.findAll();
        assertTrue(policies.size() >= 40);
        assertEquals("EcoSpeed", policies.get(0).getOptimizationStrategy());
        assertEquals("HEAVY_TRUCK_CLASS_8", policies.get(0).getVehicleType());

        List<PlatformFuelEfficiencyAdvisor> advisors = advisorRepo.findAll();
        assertTrue(advisors.size() >= 40);
        assertEquals("ReduceIdle", advisors.get(0).getRecommendationType());

        List<PlatformFuelAuditLog> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);
        assertEquals("sustainability-officer", audits.get(0).getOperator());
        assertEquals("EcoSpeed-v2.1", audits.get(0).getOptimizationAlgorithm());
    }
}
