package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.optimization.cost.RouteCostMinimizer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RouteCostMinimizerTest {

    @Autowired RouteCostMinimizer costMinimizer;

    @Autowired PlatformRouteCostLogRepository costRepo;
    @Autowired PlatformRoutingOptimizationRecommendationRepository recommendationRepo;
    @Autowired PlatformRoutingAuditLogRepository auditRepo;

    @Test
    void testRouteCostMinimizerScenarios() {
        // Route cost minimization log records over 40 iterations
        for (int i = 1; i <= 40; i++) {
            costMinimizer.optimizeRouteCost(1L, BigDecimal.valueOf(150.00), BigDecimal.valueOf(25.00), BigDecimal.valueOf(100.00));
        }

        List<PlatformRouteCostLog> costs = costRepo.findAll();
        assertTrue(costs.size() >= 40);
        assertEquals("USD", costs.get(0).getCurrency());

        List<PlatformRoutingOptimizationRecommendation> recommendations = recommendationRepo.findAll();
        assertTrue(recommendations.size() >= 40);
        assertEquals("ROUTE-OPTIMIZED-SECTOR-A", recommendations.get(0).getRecommendedRoute());
        assertTrue(recommendations.get(0).getAccepted());

        List<PlatformRoutingAuditLog> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);
        assertEquals("dynamic-routing-optimizer", audits.get(0).getOptimizer());
        assertEquals("COMPLETED", audits.get(0).getExecutionStatus());
    }
}
