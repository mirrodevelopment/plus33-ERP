/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Predictive Module
 * Package           : com.plus33.erp.predictive.policy
 * File              : PredictiveMaintenancePolicyService.java
 * Purpose           : Business logic service layer for Predictive Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PredictiveMaintenancePolicyController
 * Related Service   : PredictiveMaintenancePolicyService
 * Related Repository: PredictiveMaintenancePolicyRepository
 * Related Entity    : PredictiveMaintenancePolicy
 * Related DTO       : N/A
 * Related Mapper    : PredictiveMaintenancePolicyMapper
 * Related DB Table  : predictive_maintenance_policys
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : PredictiveMaintenancePolicyController, PredictiveMaintenancePolicyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Predictive Module. Implements PredictiveMaintenancePolicyService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.predictive.policy;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Predictive Module</b>
 *
 * <p><b>Class  :</b> {@code PredictiveMaintenancePolicyService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.predictive.policy}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Predictive Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PredictiveMaintenancePolicyController
 *   --> PredictiveMaintenancePolicyService (this)
 *   --> Validate business rules
 *   --> PredictiveMaintenancePolicyRepository (read/write 'predictive_maintenance_policys')
 *   --> PredictiveMaintenancePolicyMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code predictive_maintenance_policys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PredictiveMaintenancePolicyService {
    @Autowired PlatformPredictiveMaintenancePolicyRepository policyRepo;
    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param code the code input value
     * @param name the name input value
     * @param assetType the assetType input value
     * @param strategy the strategy input value
     * @return the numeric result value
     * @throws BusinessException if a business rule is violated
     */
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