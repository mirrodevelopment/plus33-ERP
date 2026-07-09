/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.optimization
 * File              : CrossModuleOptimizer.java
 * Purpose           : Business logic service layer for Intelligence Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrossModuleOptimizerController
 * Related Service   : CrossModuleOptimizer
 * Related Repository: CrossModuleOptimizerRepository
 * Related Entity    : CrossModuleOptimizer
 * Related DTO       : N/A
 * Related Mapper    : CrossModuleOptimizerMapper
 * Related DB Table  : cross_module_optimizers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : CrossModuleOptimizerController, CrossModuleOptimizerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Intelligence Module. Implements CrossModuleOptimizerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.intelligence.optimization;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code CrossModuleOptimizer}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.optimization}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Intelligence Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CrossModuleOptimizerController
 *   --> CrossModuleOptimizer (this)
 *   --> Validate business rules
 *   --> CrossModuleOptimizerRepository (read/write 'cross_module_optimizers')
 *   --> CrossModuleOptimizerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code cross_module_optimizers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class CrossModuleOptimizer {
    @Autowired PlatformOptimizationStrategyRepository strategyRepo;
    /**
     * Creates a new strategy and persists it to the database.
     *
     * @param code the code input value
     * @param name the name input value
     * @param params the params input value
     * @return the PlatformOptimizationStrategy result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformOptimizationStrategy registerStrategy(String code, String name, String params) {
        PlatformOptimizationStrategy strategy = new PlatformOptimizationStrategy();
        strategy.setStrategyCode(code);
        strategy.setStrategyName(name);
        strategy.setParametersJson(params);
        strategy.setActive(true);
        return strategyRepo.save(strategy);
    }
}