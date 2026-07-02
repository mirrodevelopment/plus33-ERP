package com.plus33.erp.routing.fuel.engine;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class FuelOptimizationEngine {
    @Autowired PlatformFuelOptimizationPolicyRepository policyRepo;
    @Autowired PlatformFuelEfficiencyAdvisorRepository advisorRepo;
    @Autowired PlatformFuelAuditLogRepository auditRepo;

    @Transactional
    public PlatformFuelOptimizationPolicy createPolicy(String code, String strategy) {
        PlatformFuelOptimizationPolicy p = new PlatformFuelOptimizationPolicy();
        p.setPolicyCode(code);
        p.setOptimizationStrategy(strategy);
        p.setVehicleType("HEAVY_TRUCK_CLASS_8");
        p.setEngineType("CUMMINS_ISX15");
        p.setFuelType("Diesel");
        p.setIdleLimitSeconds(300);
        p.setTargetFuelConsumption(BigDecimal.valueOf(32.40));
        p.setEcoSpeedMin(BigDecimal.valueOf(80.00));
        p.setEcoSpeedMax(BigDecimal.valueOf(95.00));
        p.setEnabled(true);
        p.setEffectiveFrom(LocalDateTime.now());
        p.setEffectiveTo(LocalDateTime.now().plusMonths(6));
        return policyRepo.save(p);
    }

    @Transactional
    public PlatformFuelEfficiencyAdvisor suggestAdvice(String type) {
        PlatformFuelEfficiencyAdvisor advice = new PlatformFuelEfficiencyAdvisor();
        advice.setRecommendationType(type);
        advice.setPriority("HIGH");
        advice.setExpectedFuelSavingL(BigDecimal.valueOf(14.50));
        advice.setExpectedCostSaving(BigDecimal.valueOf(22.80));
        advice.setExpectedEmissionReduce(BigDecimal.valueOf(38.00));
        advice.setGeneratedBy("eco-speed-advisory-engine");
        advice.setAcknowledged(false);
        advice = advisorRepo.save(advice);

        PlatformFuelAuditLog audit = new PlatformFuelAuditLog();
        audit.setPolicyVersion(1);
        audit.setOptimizationAlgorithm("EcoSpeed-v2.1");
        audit.setOperator("sustainability-officer");
        audit.setApprovalStatus("APPROVED");
        audit.setExecutionTimeMs(120L);
        audit.setPreviousConfiguration("TARGET_CONS=35.00");
        audit.setNewConfiguration("TARGET_CONS=32.40");
        audit.setTraceId("TRACE-ID-FUEL-OPT");
        auditRepo.save(audit);

        return advice;
    }
}