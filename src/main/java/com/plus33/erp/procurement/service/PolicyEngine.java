package com.plus33.erp.procurement.service;

import com.plus33.erp.procurement.entity.ProcurementPolicy;
import com.plus33.erp.procurement.repository.ProcurementPolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PolicyEngine {

    private final ProcurementPolicyRepository policyRepository;

    public PolicyEngine(ProcurementPolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

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
