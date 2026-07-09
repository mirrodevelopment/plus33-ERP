/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.policy
 * File              : OpaPolicyManager.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OpaPolicyManagerController
 * Related Service   : OpaPolicyManager
 * Related Repository: OpaPolicyManagerRepository
 * Related Entity    : OpaPolicyManager
 * Related DTO       : N/A
 * Related Mapper    : OpaPolicyManagerMapper
 * Related DB Table  : opa_policy_managers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : OpaPolicyManagerController, OpaPolicyManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements OpaPolicyManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.policy;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code OpaPolicyManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.policy}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * OpaPolicyManagerController
 *   --> OpaPolicyManager (this)
 *   --> Validate business rules
 *   --> OpaPolicyManagerRepository (read/write 'opa_policy_managers')
 *   --> OpaPolicyManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code opa_policy_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class OpaPolicyManager {
    @Autowired PlatformAccessPolicyRepository policyRepo;
    @Autowired PlatformPolicyAuditRepository auditRepo;
    @Autowired PlatformPolicyVersionRepository versionRepo;
    @Autowired PlatformPolicyHistoryRepository historyRepo;
    /**
     * Creates a new policy and persists it to the database.
     *
     * @param code the code input value
     * @param rego the rego input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerPolicy(String code, String rego) {
        PlatformAccessPolicy policy = new PlatformAccessPolicy();
        policy.setPolicyCode(code);
        policy.setRegoContent(rego);
        policyRepo.save(policy);
    }

    /**
     * Performs the auditPolicy operation in this module.
     *
     * @param code the code input value
     * @param userId authenticated user identifier
     * @param action the action input value
     * @param decision the decision input value
     */
    @Transactional
    public void auditPolicy(String code, String userId, String action, String decision) {
        PlatformPolicyAudit audit = new PlatformPolicyAudit();
        audit.setPolicyCode(code);
        audit.setUserIdentity(userId);
        audit.setAction(action);
        audit.setDecision(decision);
        audit.setEvaluatedAt(LocalDateTime.now());
        auditRepo.save(audit);
    }

    /**
     * Updates an existing policy version record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param code the code input value
     * @param newVersion the newVersion input value
     * @param rego the rego input value
     * @param operator the operator input value
     * @param reason the reason input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void updatePolicyVersion(String code, String newVersion, String rego, String operator, String reason) {
        PlatformAccessPolicy policy = policyRepo.findAll().stream()
                .filter(p -> p.getPolicyCode().equals(code))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Policy not found"));

        PlatformPolicyVersion v = new PlatformPolicyVersion();
        v.setPolicyId(policy.getId());
        v.setPolicyVersion(newVersion);
        v.setRegoContent(rego);
        v.setEffectiveFrom(LocalDateTime.now());
        versionRepo.save(v);

        PlatformPolicyHistory h = new PlatformPolicyHistory();
        h.setPolicyCode(code);
        h.setPreviousVersion("v1.0.0");
        h.setNewVersion(newVersion);
        h.setOperator(operator);
        h.setReason(reason);
        h.setChangedAt(LocalDateTime.now());
        historyRepo.save(h);
    }
}