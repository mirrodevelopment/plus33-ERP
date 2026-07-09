/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service.impl
 * File              : WarehouseRulesEngine.java
 * Purpose           : Business logic service layer for Wms Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseRulesEngineController
 * Related Service   : WarehouseRulesEngine
 * Related Repository: WarehouseRuleRepository
 * Related Entity    : WarehouseRulesEngine
 * Related DTO       : N/A
 * Related Mapper    : WarehouseRulesEngineMapper
 * Related DB Table  : warehouse_rules_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseRulesEngineController, WarehouseRulesEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Wms Module. Implements WarehouseRulesEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.WarehouseRule;
import com.plus33.erp.wms.repository.WarehouseRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseRulesEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Wms Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * WarehouseRulesEngineController
 *   --> WarehouseRulesEngine (this)
 *   --> Validate business rules
 *   --> WarehouseRulesEngineRepository (read/write 'warehouse_rules_engines')
 *   --> WarehouseRulesEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code warehouse_rules_engines}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class WarehouseRulesEngine {

    private final WarehouseRuleRepository ruleRepo;

    public WarehouseRulesEngine(WarehouseRuleRepository ruleRepo) {
        this.ruleRepo = ruleRepo;
    }

    /**
     * Retrieves active rules data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<WarehouseRule> getActiveRules(Long companyId) {
        return ruleRepo.findByCompanyIdAndActiveTrueOrderByPriorityAsc(companyId);
    }
}