package com.plus33.erp.compliance.policy;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompliancePolicyService {
    @Autowired PlatformDeviceCompliancePolicyRepository policyRepo;

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