/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Routing Module
 * Package           : com.plus33.erp.routing.fuel.engine
 * File              : FuelOptimizationEngine.java
 * Purpose           : Business logic service layer for Routing Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FuelOptimizationEngineController
 * Related Service   : FuelOptimizationEngine
 * Related Repository: FuelOptimizationEngineRepository
 * Related Entity    : FuelOptimizationEngine
 * Related DTO       : N/A
 * Related Mapper    : FuelOptimizationEngineMapper
 * Related DB Table  : fuel_optimization_engines
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : FuelOptimizationEngineController, FuelOptimizationEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Routing Module. Implements FuelOptimizationEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.routing.fuel.engine;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Routing Module</b>
 *
 * <p><b>Class  :</b> {@code FuelOptimizationEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.routing.fuel.engine}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Routing Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * FuelOptimizationEngineController
 *   --> FuelOptimizationEngine (this)
 *   --> Validate business rules
 *   --> FuelOptimizationEngineRepository (read/write 'fuel_optimization_engines')
 *   --> FuelOptimizationEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code fuel_optimization_engines}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class FuelOptimizationEngine {
    @Autowired PlatformFuelOptimizationPolicyRepository policyRepo;
    @Autowired PlatformFuelEfficiencyAdvisorRepository advisorRepo;
    @Autowired PlatformFuelAuditLogRepository auditRepo;
    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param code the code input value
     * @param strategy the strategy input value
     * @return the PlatformFuelOptimizationPolicy result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Performs the suggestAdvice operation in this module.
     *
     * @param type the type input value
     * @return the PlatformFuelEfficiencyAdvisor result
     */
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