package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.PayrollPolicy;
import com.plus33.erp.workforce.entity.PayrollPolicyVersion;

public interface PayrollPolicyService {
    PayrollPolicy createPolicy(Long companyId, String code, String name);
    PayrollPolicyVersion createPolicyVersion(Long policyId, Integer versionNumber);
}
