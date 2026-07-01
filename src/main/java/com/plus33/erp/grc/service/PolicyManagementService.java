package com.plus33.erp.grc.service;

import com.plus33.erp.grc.entity.*;
import com.plus33.erp.grc.event.GrcEventBus;
import com.plus33.erp.grc.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    public EnterprisePolicy createPolicy(Long companyId, String code, String title, String category) {
        EnterprisePolicy policy = new EnterprisePolicy();
        policy.setCompanyId(companyId);
        policy.setPolicyCode(code);
        policy.setTitle(title);
        policy.setCategory(category);
        policy.setStatus("DRAFT");
        return policyRepo.save(policy);
    }

    public PolicyVersion createVersion(Long policyId, String contentHash) {
        int nextVersion = policyVersionRepo.findByPolicyId(policyId).size() + 1;
        PolicyVersion version = new PolicyVersion();
        version.setPolicyId(policyId);
        version.setVersionNumber(nextVersion);
        version.setContentHash(contentHash);
        return policyVersionRepo.save(version);
    }

    public void approveVersion(Long policyVersionId) {
        PolicyVersion v = policyVersionRepo.findById(policyVersionId).orElseThrow();
        v.setApprovedAt(LocalDateTime.now());
        policyVersionRepo.save(v);

        EnterprisePolicy policy = policyRepo.findById(v.getPolicyId()).orElseThrow();
        transitionPolicy(policy, "APPROVED");
    }

    public void publishVersion(Long policyVersionId) {
        PolicyVersion v = policyVersionRepo.findById(policyVersionId).orElseThrow();
        v.setPublishedAt(LocalDateTime.now());
        policyVersionRepo.save(v);

        EnterprisePolicy policy = policyRepo.findById(v.getPolicyId()).orElseThrow();
        transitionPolicy(policy, "PUBLISHED");
        eventBus.publish(policy.getCompanyId(), "PolicyPublished",
            Map.of("policyId", policy.getId(), "versionId", policyVersionId));
    }

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

    public long countAcknowledgements(Long policyVersionId) {
        return ackRepo.countByPolicyVersionId(policyVersionId);
    }
}
