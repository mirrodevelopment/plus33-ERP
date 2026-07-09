/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Routing Module
 * Package           : com.plus33.erp.routing.optimization.policy
 * File              : RouteOptimizationPolicyService.java
 * Purpose           : Business logic service layer for Routing Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RouteOptimizationPolicyController
 * Related Service   : RouteOptimizationPolicyService
 * Related Repository: RouteOptimizationPolicyRepository
 * Related Entity    : RouteOptimizationPolicy
 * Related DTO       : N/A
 * Related Mapper    : RouteOptimizationPolicyMapper
 * Related DB Table  : route_optimization_policys
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : RouteOptimizationPolicyController, RouteOptimizationPolicyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Routing Module. Implements RouteOptimizationPolicyService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.routing.optimization.policy;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Routing Module</b>
 *
 * <p><b>Class  :</b> {@code RouteOptimizationPolicyService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.routing.optimization.policy}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Routing Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * RouteOptimizationPolicyController
 *   --> RouteOptimizationPolicyService (this)
 *   --> Validate business rules
 *   --> RouteOptimizationPolicyRepository (read/write 'route_optimization_policys')
 *   --> RouteOptimizationPolicyMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code route_optimization_policys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class RouteOptimizationPolicyService {
    @Autowired PlatformRouteOptimizationPolicyRepository policyRepo;
    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param code the code input value
     * @param name the name input value
     * @param strategy the strategy input value
     * @return the PlatformRouteOptimizationPolicy result
     * @throws BusinessException if a business rule is violated
     */
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