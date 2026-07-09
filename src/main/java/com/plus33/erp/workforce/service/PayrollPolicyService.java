/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : PayrollPolicyService.java
 * Purpose           : Service interface contract defining the API for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollPolicyController
 * Related Service   : PayrollPolicyService, PayrollPolicyServiceImpl
 * Related Repository: PayrollPolicyRepository
 * Related Entity    : PayrollPolicy
 * Related DTO       : N/A
 * Related Mapper    : PayrollPolicyMapper
 * Related DB Table  : payroll_policys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.PayrollPolicy;
import com.plus33.erp.workforce.entity.PayrollPolicyVersion;

public interface PayrollPolicyService {
    PayrollPolicy createPolicy(Long companyId, String code, String name);
    PayrollPolicyVersion createPolicyVersion(Long policyId, Integer versionNumber);
}
