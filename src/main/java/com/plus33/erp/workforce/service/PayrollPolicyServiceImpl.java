package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.PayrollPolicy;
import com.plus33.erp.workforce.entity.PayrollPolicyVersion;
import com.plus33.erp.workforce.repository.PayrollPolicyRepository;
import com.plus33.erp.workforce.repository.PayrollPolicyVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class PayrollPolicyServiceImpl implements PayrollPolicyService {

    private final PayrollPolicyRepository policyRepository;
    private final PayrollPolicyVersionRepository policyVersionRepository;

    public PayrollPolicyServiceImpl(PayrollPolicyRepository policyRepository,
                                     PayrollPolicyVersionRepository policyVersionRepository) {
        this.policyRepository = policyRepository;
        this.policyVersionRepository = policyVersionRepository;
    }

    @Override
    @Transactional
    public PayrollPolicy createPolicy(Long companyId, String code, String name) {
        PayrollPolicy policy = new PayrollPolicy();
        policy.setCompanyId(companyId);
        policy.setCode(code);
        policy.setName(name);
        return policyRepository.save(policy);
    }

    @Override
    @Transactional
    public PayrollPolicyVersion createPolicyVersion(Long policyId, Integer versionNumber) {
        PayrollPolicyVersion version = new PayrollPolicyVersion();
        version.setPolicyId(policyId);
        version.setVersionNumber(versionNumber);
        version.setEffectiveFrom(LocalDate.now());
        return policyVersionRepository.save(version);
    }
}
