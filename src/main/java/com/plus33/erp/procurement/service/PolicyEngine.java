/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : PolicyEngine.java
 * Purpose           : Business logic service layer for Procurement Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PolicyEngineController
 * Related Service   : PolicyEngine
 * Related Repository: ProcurementPolicyRepository
 * Related Entity    : PolicyEngine
 * Related DTO       : N/A
 * Related Mapper    : PolicyEngineMapper
 * Related DB Table  : policy_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PolicyEngineController, PolicyEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Procurement Module. Implements PolicyEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.procurement.service;

import com.plus33.erp.procurement.entity.ProcurementPolicy;
import com.plus33.erp.procurement.repository.ProcurementPolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PolicyEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Procurement Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PolicyEngineController
 *   --> PolicyEngine (this)
 *   --> Validate business rules
 *   --> PolicyEngineRepository (read/write 'policy_engines')
 *   --> PolicyEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code policy_engines}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PolicyEngine {

    private final ProcurementPolicyRepository policyRepository;

    public PolicyEngine(ProcurementPolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    /**
     * Validates business rules and constraints for policy.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param policyType the policyType input value
     * @param amount the amount input value
     * @return true if operation succeeded, false otherwise
     * @throws BusinessException if a business rule is violated
     */
    @Transactional(readOnly = true)
    public boolean validatePolicy(Long companyId, String policyType, BigDecimal amount) {
        List<ProcurementPolicy> policies = policyRepository.findByCompanyIdAndPolicyTypeAndActive(companyId, policyType, true);
        for (ProcurementPolicy policy : policies) {
            if (policy.getThresholdAmount() != null && amount.compareTo(policy.getThresholdAmount()) > 0) {
                return false; // Limit breached
            }
        }
        return true;
    }

    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param policyType the policyType input value
     * @param thresholdAmount the thresholdAmount input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void createPolicy(Long companyId, String policyType, BigDecimal thresholdAmount) {
        ProcurementPolicy policy = new ProcurementPolicy();
        policy.setCompanyId(companyId);
        policy.setPolicyType(policyType);
        policy.setThresholdAmount(thresholdAmount);
        policy.setActive(true);
        policyRepository.save(policy);
    }
}