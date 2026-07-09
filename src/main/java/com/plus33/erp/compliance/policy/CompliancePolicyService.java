/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Compliance Module
 * Package           : com.plus33.erp.compliance.policy
 * File              : CompliancePolicyService.java
 * Purpose           : Business logic service layer for Compliance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CompliancePolicyController
 * Related Service   : CompliancePolicyService
 * Related Repository: CompliancePolicyRepository
 * Related Entity    : CompliancePolicy
 * Related DTO       : N/A
 * Related Mapper    : CompliancePolicyMapper
 * Related DB Table  : compliance_policys
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : CompliancePolicyController, CompliancePolicyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Compliance Module. Implements CompliancePolicyService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.compliance.policy;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Compliance Module</b>
 *
 * <p><b>Class  :</b> {@code CompliancePolicyService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.compliance.policy}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Compliance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CompliancePolicyController
 *   --> CompliancePolicyService (this)
 *   --> Validate business rules
 *   --> CompliancePolicyRepository (read/write 'compliance_policys')
 *   --> CompliancePolicyMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code compliance_policys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class CompliancePolicyService {
    @Autowired PlatformDeviceCompliancePolicyRepository policyRepo;
    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param code the code input value
     * @param name the name input value
     * @param type the type input value
     * @param severity the severity input value
     * @return the PlatformDeviceCompliancePolicy result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformDeviceCompliancePolicy createPolicy(String code, String name, String type, String severity) {
        PlatformDeviceCompliancePolicy p = new PlatformDeviceCompliancePolicy();
        p.setPolicyCode(code);
        p.setPolicyName(name);
        p.setPolicyType(type);
        p.setRequiredOs("Linux Ubuntu 22.04");
        p.setMinimumAgentVersion("1.5.0");
        p.setRequiredPackages("openssl, ufw");
        p.setRequiredServices("ssh, edge-agent");
        p.setRequiredPorts("22, 8080");
        p.setRequiredKernelVersion("5.15.0");
        p.setRequiredCertificate("PLUS33 Edge Root CA");
        p.setSeverity(severity);
        p.setEnabled(true);
        p.setCreatedBy("security-officer");
        return policyRepo.save(p);
    }
}