package com.plus33.erp.platform.policy;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class OpaPolicyManager {
    @Autowired PlatformAccessPolicyRepository policyRepo;
    @Autowired PlatformPolicyAuditRepository auditRepo;
    @Autowired PlatformPolicyVersionRepository versionRepo;
    @Autowired PlatformPolicyHistoryRepository historyRepo;

    @Transactional
    public void registerPolicy(String code, String rego) {
        PlatformAccessPolicy policy = new PlatformAccessPolicy();
        policy.setPolicyCode(code);
        policy.setRegoContent(rego);
        policyRepo.save(policy);
    }

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