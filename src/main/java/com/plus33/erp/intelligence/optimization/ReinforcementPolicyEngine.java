/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.optimization
 * File              : ReinforcementPolicyEngine.java
 * Purpose           : Business logic service layer for Intelligence Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReinforcementPolicyEngineController
 * Related Service   : ReinforcementPolicyEngine
 * Related Repository: ReinforcementPolicyEngineRepository
 * Related Entity    : ReinforcementPolicyEngine
 * Related DTO       : N/A
 * Related Mapper    : ReinforcementPolicyEngineMapper
 * Related DB Table  : reinforcement_policy_engines
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : ReinforcementPolicyEngineController, ReinforcementPolicyEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Intelligence Module. Implements ReinforcementPolicyEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.intelligence.optimization;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code ReinforcementPolicyEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.optimization}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Intelligence Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ReinforcementPolicyEngineController
 *   --> ReinforcementPolicyEngine (this)
 *   --> Validate business rules
 *   --> ReinforcementPolicyEngineRepository (read/write 'reinforcement_policy_engines')
 *   --> ReinforcementPolicyEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code reinforcement_policy_engines}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ReinforcementPolicyEngine {
    @Autowired PlatformRlPolicyRepository policyRepo;
    @Autowired PlatformRlPolicyUpdateRepository updateRepo;
    /**
     * Performs the trainPolicy operation in this module.
     *
     * @param code the code input value
     * @param action the action input value
     * @param reward the reward input value
     */
    @Transactional
    public void trainPolicy(String code, String action, BigDecimal reward) {
        PlatformRlPolicy policy = policyRepo.findAll().stream()
                .filter(p -> p.getPolicyCode().equals(code))
                .findFirst()
                .orElseGet(() -> {
                    PlatformRlPolicy p = new PlatformRlPolicy();
                    p.setPolicyCode(code);
                    p.setCurrentStateJson("{}");
                    return policyRepo.save(p);
                });

        PlatformRlPolicyUpdate update = new PlatformRlPolicyUpdate();
        update.setPolicyId(policy.getId());
        update.setActionTaken(action);
        update.setReward(reward);
        update.setStateJson("{\"trained\": true}");
        update.setUpdatedAt(LocalDateTime.now());
        updateRepo.save(update);
    }
}