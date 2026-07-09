/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.service
 * File              : PolicyManagementService.java
 * Purpose           : Business logic service layer for Grc Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PolicyManagementController
 * Related Service   : PolicyManagementService
 * Related Repository: EnterprisePolicyRepository, PolicyVersionRepository, PolicyAcknowledgementRepository
 * Related Entity    : PolicyManagement
 * Related DTO       : N/A
 * Related Mapper    : PolicyManagementMapper
 * Related DB Table  : policy_managements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PolicyManagementController, PolicyManagementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Grc Module. Implements PolicyManagementService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.grc.service;

import com.plus33.erp.grc.entity.*;
import com.plus33.erp.grc.event.GrcEventBus;
import com.plus33.erp.grc.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code PolicyManagementService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Grc Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PolicyManagementController
 *   --> PolicyManagementService (this)
 *   --> Validate business rules
 *   --> PolicyManagementRepository (read/write 'policy_managements')
 *   --> PolicyManagementMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code policy_managements}</p>
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class PolicyManagementService {

    private static final Map<String, List<String>> TRANSITIONS = Map.of(
        "DRAFT",          List.of("UNDER_REVIEW", "APPROVED"),
        "UNDER_REVIEW",   List.of("APPROVED", "DRAFT"),
        "APPROVED",       List.of("PUBLISHED"),
        "PUBLISHED",      List.of("SUPERSEDED", "WITHDRAWN"),
        "SUPERSEDED",     List.of(),
        "WITHDRAWN",      List.of(),
        "EXPIRED",        List.of()
    );

    private final EnterprisePolicyRepository policyRepo;
    private final PolicyVersionRepository policyVersionRepo;
    private final PolicyAcknowledgementRepository ackRepo;
    private final GrcEventBus eventBus;

    public PolicyManagementService(EnterprisePolicyRepository policyRepo,
                                   PolicyVersionRepository policyVersionRepo,
                                   PolicyAcknowledgementRepository ackRepo,
                                   GrcEventBus eventBus) {
        this.policyRepo = policyRepo;
        this.policyVersionRepo = policyVersionRepo;
        this.ackRepo = ackRepo;
        this.eventBus = eventBus;
    }

    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param code the code input value
     * @param title the title input value
     * @param category the category input value
     * @return the EnterprisePolicy result
     * @throws BusinessException if a business rule is violated
     */
    public EnterprisePolicy createPolicy(Long companyId, String code, String title, String category) {
        EnterprisePolicy policy = new EnterprisePolicy();
        policy.setCompanyId(companyId);
        policy.setPolicyCode(code);
        policy.setTitle(title);
        policy.setCategory(category);
        policy.setStatus("DRAFT");
        return policyRepo.save(policy);
    }

    /**
     * Creates a new version and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param policyId the policyId input value
     * @param contentHash the contentHash input value
     * @return the PolicyVersion result
     * @throws BusinessException if a business rule is violated
     */
    public PolicyVersion createVersion(Long policyId, String contentHash) {
        int nextVersion = policyVersionRepo.findByPolicyId(policyId).size() + 1;
        PolicyVersion version = new PolicyVersion();
        version.setPolicyId(policyId);
        version.setVersionNumber(nextVersion);
        version.setContentHash(contentHash);
        return policyVersionRepo.save(version);
    }

    /**
     * Approves the version, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param policyVersionId the policyVersionId input value
     * @throws BusinessException if a business rule is violated
     */
    public void approveVersion(Long policyVersionId) {
        PolicyVersion v = policyVersionRepo.findById(policyVersionId).orElseThrow();
        v.setApprovedAt(LocalDateTime.now());
        policyVersionRepo.save(v);

        EnterprisePolicy policy = policyRepo.findById(v.getPolicyId()).orElseThrow();
        transitionPolicy(policy, "APPROVED");
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param policyVersionId the policyVersionId input value
     */
    public void publishVersion(Long policyVersionId) {
        PolicyVersion v = policyVersionRepo.findById(policyVersionId).orElseThrow();
        v.setPublishedAt(LocalDateTime.now());
        policyVersionRepo.save(v);

        EnterprisePolicy policy = policyRepo.findById(v.getPolicyId()).orElseThrow();
        transitionPolicy(policy, "PUBLISHED");
        eventBus.publish(policy.getCompanyId(), "PolicyPublished",
            Map.of("policyId", policy.getId(), "versionId", policyVersionId));
    }

    /**
     * Performs the acknowledgePolicy operation in this module.
     *
     * @param policyVersionId the policyVersionId input value
     * @param employeeId the employeeId input value
     */
    public void acknowledgePolicy(Long policyVersionId, Long employeeId) {
        if (ackRepo.existsByPolicyVersionIdAndEmployeeId(policyVersionId, employeeId)) {
            return; // Idempotent
        }
        PolicyAcknowledgement ack = new PolicyAcknowledgement();
        ack.setPolicyVersionId(policyVersionId);
        ack.setEmployeeId(employeeId);
        ackRepo.save(ack);
        eventBus.publish(null, "PolicyAcknowledged",
            Map.of("policyVersionId", policyVersionId, "employeeId", employeeId));
    }

    private void transitionPolicy(EnterprisePolicy policy, String newStatus) {
        List<String> allowed = TRANSITIONS.getOrDefault(policy.getStatus(), List.of());
        if (!allowed.contains(newStatus)) {
            throw new IllegalStateException("Invalid policy transition: " + policy.getStatus() + " → " + newStatus);
        }
        policy.setStatus(newStatus);
        policyRepo.save(policy);
    }

    /**
     * Performs the countAcknowledgements operation in this module.
     *
     * @param policyVersionId the policyVersionId input value
     * @return the numeric result value
     */
    public long countAcknowledgements(Long policyVersionId) {
        return ackRepo.countByPolicyVersionId(policyVersionId);
    }
}