package com.plus33.erp.routing.optimization.policy;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class RouteOptimizationPolicyService {
    @Autowired PlatformRouteOptimizationPolicyRepository policyRepo;

    @Transactional
    public PlatformRouteOptimizationPolicy createPolicy(String code, String name, String strategy) {
        PlatformRouteOptimizationPolicy p = new PlatformRouteOptimizationPolicy();
        p.setPolicyCode(code);
        p.setPolicyName(name);
        p.setOptimizationStrategy(strategy);
        p.setVehicleConstraints("CAPACITY_MATCHING_VEHICLES");
        p.setDriverConstraints("SHIFT_HOURS_BALANCING");
        p.setPriority(3);
        p.setTimeWindowMinutes(120);
        p.setMaximumDistanceKm(BigDecimal.valueOf(500.00));
        p.setMaximumDurationMins(600);
        p.setMaximumLoadKg(BigDecimal.valueOf(15000.00));
        p.setTrafficWeight(BigDecimal.valueOf(0.40));
        p.setFuelWeight(BigDecimal.valueOf(0.20));
        p.setCarbonWeight(BigDecimal.valueOf(0.10));
        p.setCostWeight(BigDecimal.valueOf(0.30));
        p.setEnabled(true);
        p.setCreatedBy("operations-director");
        return policyRepo.save(p);
    }
}