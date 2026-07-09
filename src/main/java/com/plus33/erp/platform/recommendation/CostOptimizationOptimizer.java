/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.recommendation
 * File              : CostOptimizationOptimizer.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CostOptimizationOptimizerController
 * Related Service   : CostOptimizationOptimizer
 * Related Repository: CostOptimizationOptimizerRepository
 * Related Entity    : CostOptimizationOptimizer
 * Related DTO       : N/A
 * Related Mapper    : CostOptimizationOptimizerMapper
 * Related DB Table  : cost_optimization_optimizers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CostOptimizationOptimizerController, CostOptimizationOptimizerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements CostOptimizationOptimizerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.recommendation;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code CostOptimizationOptimizer}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.recommendation}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CostOptimizationOptimizerController
 *   --> CostOptimizationOptimizer (this)
 *   --> Validate business rules
 *   --> CostOptimizationOptimizerRepository (read/write 'cost_optimization_optimizers')
 *   --> CostOptimizationOptimizerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code cost_optimization_optimizers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class CostOptimizationOptimizer {
    @Autowired PlatformCloudResourceRepository resourceRepo;
    @Autowired PlatformCostRecommendationRepository recommendationRepo;
    /**
     * Creates a new cloud resource and persists it to the database.
     *
     * @param id the unique database ID of the resource
     * @param type the type input value
     * @param provider the provider input value
     * @param region the region input value
     * @param dailyCost the dailyCost input value
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Performs the recommendOptimization operation in this module.
     *
     * @param resourceId the resourceId input value
     * @param recType the recType input value
     * @param savings the savings input value
     * @param reason the reason input value
     */
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