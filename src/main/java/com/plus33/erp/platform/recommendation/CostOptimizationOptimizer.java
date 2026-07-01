package com.plus33.erp.platform.recommendation;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CostOptimizationOptimizer {
    @Autowired PlatformCloudResourceRepository resourceRepo;
    @Autowired PlatformCostRecommendationRepository recommendationRepo;

    @Transactional
    public void registerCloudResource(String id, String type, String provider, String region, double dailyCost) {
        PlatformCloudResource res = new PlatformCloudResource();
        res.setResourceId(id);
        res.setResourceType(type);
        res.setProvider(provider);
        res.setRegion(region);
        res.setCostDaily(BigDecimal.valueOf(dailyCost));
        res.setStatus("RUNNING");
        resourceRepo.save(res);
    }

    @Transactional
    public void recommendOptimization(String resourceId, String recType, double savings, String reason) {
        PlatformCostRecommendation rec = new PlatformCostRecommendation();
        rec.setResourceId(resourceId);
        rec.setRecommendationType(recType);
        rec.setSavingsPotential(BigDecimal.valueOf(savings));
        rec.setReason(reason);
        rec.setStatus("ACTIVE");
        rec.setCreatedAt(LocalDateTime.now());
        recommendationRepo.save(rec);
    }
}