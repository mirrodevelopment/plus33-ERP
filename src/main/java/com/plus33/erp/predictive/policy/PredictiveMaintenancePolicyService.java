package com.plus33.erp.predictive.policy;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class PredictiveMaintenancePolicyService {
    @Autowired PlatformPredictiveMaintenancePolicyRepository policyRepo;

    @Transactional
    public PlatformPredictiveMaintenancePolicy createPolicy(String code, String name, String assetType, String strategy) {
        PlatformPredictiveMaintenancePolicy p = new PlatformPredictiveMaintenancePolicy();
        p.setPolicyCode(code);
        p.setPolicyName(name);
        p.setAssetType(assetType);
        p.setPredictionModel("RANDOM_FOREST_PROB");
        p.setMinimumHealthScore(BigDecimal.valueOf(80.00));
        p.setRemainingUsefulLifeThreshold(BigDecimal.valueOf(168.00)); // 7 days in hours
        p.setFailureProbabilityThreshold(BigDecimal.valueOf(0.15));
        p.setMaintenanceStrategy(strategy);
        p.setPriority(2);
        p.setEnabled(true);
        p.setCreatedBy("reliability-manager");
        return policyRepo.save(p);
    }
}