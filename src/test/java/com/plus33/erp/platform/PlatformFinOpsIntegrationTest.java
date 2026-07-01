package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.platform.chargeback.CostChargebackManager;
import com.plus33.erp.platform.recommendation.CostOptimizationOptimizer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PlatformFinOpsIntegrationTest {

    @Autowired CostChargebackManager chargebackManager;
    @Autowired CostOptimizationOptimizer optimizer;

    @Autowired PlatformFinopsBudgetRepository budgetRepo;
    @Autowired PlatformCloudCostItemRepository costRepo;
    @Autowired PlatformCostCenterRepository centerRepo;
    @Autowired PlatformChargebackRepository chargebackRepo;
    @Autowired PlatformCloudResourceRepository resourceRepo;
    @Autowired PlatformCostRecommendationRepository recommendationRepo;

    @Test
    void testFinopsScenarios() {
        // Create 40 budgets
        for (int i = 1; i <= 40; i++) {
            PlatformFinopsBudget b = new PlatformFinopsBudget();
            b.setBudgetName("budget-" + i);
            b.setLimitAmount(BigDecimal.valueOf(1000 + i * 100));
            b.setAlertThreshold(BigDecimal.valueOf(85.0));
            budgetRepo.save(b);
        }
        List<PlatformFinopsBudget> budgets = budgetRepo.findAll();
        assertTrue(budgets.size() >= 40);

        // Record 40 daily cost items
        for (int i = 1; i <= 40; i++) {
            PlatformCloudCostItem item = new PlatformCloudCostItem();
            item.setResourceId("vm-" + i);
            item.setCost(BigDecimal.valueOf(12.5 + i));
            item.setBillingPeriod("2026-07");
            costRepo.save(item);
        }
        List<PlatformCloudCostItem> costs = costRepo.findAll();
        assertTrue(costs.size() >= 40);

        // Create 40 cost centers and allocations
        for (int i = 1; i <= 40; i++) {
            chargebackManager.createCostCenter("cc-" + i, "FinOps Center " + i);
            chargebackManager.allocateCost("cc-" + i, "vm-" + i, 100.0);
            chargebackManager.recordChargeback("cc-" + i, 120.0 + i, "2026-07");
        }
        List<PlatformCostCenter> centers = centerRepo.findAll();
        assertTrue(centers.size() >= 40);

        List<PlatformChargeback> chargebacks = chargebackRepo.findAll();
        assertTrue(chargebacks.size() >= 40);

        // Register 40 cloud resources and recommendations
        for (int i = 1; i <= 40; i++) {
            optimizer.registerCloudResource("res-" + i, "VM", "AWS", "us-east-1", 50.0 + i);
            optimizer.recommendOptimization("res-" + i, "RIGHTSIZE", 15.0, "Resource is under-utilized " + i);
        }
        List<PlatformCloudResource> resources = resourceRepo.findAll();
        assertTrue(resources.size() >= 40);

        List<PlatformCostRecommendation> recommendations = recommendationRepo.findAll();
        assertTrue(recommendations.size() >= 40);
    }
}
