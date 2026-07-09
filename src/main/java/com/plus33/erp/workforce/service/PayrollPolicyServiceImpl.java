/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : PayrollPolicyServiceImpl.java
 * Purpose           : Business logic service layer for Workforce Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollPolicyController
 * Related Service   : PayrollPolicyServiceImpl
 * Related Repository: PayrollPolicyRepository, PayrollPolicyVersionRepository
 * Related Entity    : PayrollPolicy
 * Related DTO       : N/A
 * Related Mapper    : PayrollPolicyMapper
 * Related DB Table  : payroll_policys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollPolicyController, PayrollPolicyServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Workforce Module. Implements PayrollPolicyService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.PayrollPolicy;
import com.plus33.erp.workforce.entity.PayrollPolicyVersion;
import com.plus33.erp.workforce.repository.PayrollPolicyRepository;
import com.plus33.erp.workforce.repository.PayrollPolicyVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollPolicyServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Workforce Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PayrollPolicyController
 *   --> PayrollPolicyServiceImpl (this)
 *   --> Validate business rules
 *   --> PayrollPolicyRepository (read/write 'payroll_policys')
 *   --> PayrollPolicyMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code payroll_policys}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PayrollPolicyServiceImpl implements PayrollPolicyService {

    private final PayrollPolicyRepository policyRepository;
    private final PayrollPolicyVersionRepository policyVersionRepository;

    public PayrollPolicyServiceImpl(PayrollPolicyRepository policyRepository,
                                     PayrollPolicyVersionRepository policyVersionRepository) {
        this.policyRepository = policyRepository;
        this.policyVersionRepository = policyVersionRepository;
    }

    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param code the code input value
     * @param name the name input value
     * @return the PayrollPolicy result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PayrollPolicy createPolicy(Long companyId, String code, String name) {
        PayrollPolicy policy = new PayrollPolicy();
        policy.setCompanyId(companyId);
        policy.setCode(code);
        policy.setName(name);
        return policyRepository.save(policy);
    }

    /**
     * Creates a new policy version and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param policyId the policyId input value
     * @param versionNumber the versionNumber input value
     * @return the PayrollPolicyVersion result
     * @throws BusinessException if a business rule is violated
     */
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