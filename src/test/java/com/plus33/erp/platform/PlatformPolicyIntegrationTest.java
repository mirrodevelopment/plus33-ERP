package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.platform.policy.OpaPolicyManager;
import com.plus33.erp.platform.compliance.ComplianceControlService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PlatformPolicyIntegrationTest {

    @Autowired OpaPolicyManager policyManager;
    @Autowired ComplianceControlService complianceService;

    @Autowired PlatformAccessPolicyRepository policyRepo;
    @Autowired PlatformPolicyAuditRepository auditRepo;
    @Autowired PlatformPolicyVersionRepository versionRepo;
    @Autowired PlatformPolicyHistoryRepository historyRepo;
    @Autowired PlatformComplianceFrameworkRepository frameworkRepo;
    @Autowired PlatformComplianceControlRepository controlRepo;

    @Test
    void testPolicyScenarios() {
        // Register 40 policies
        for (int i = 1; i <= 40; i++) {
            policyManager.registerPolicy("policy-" + i, "package authz default allow = false");
        }
        List<PlatformAccessPolicy> policies = policyRepo.findAll();
        assertTrue(policies.size() >= 40);

        // Run 40 audit logs
        for (int i = 1; i <= 40; i++) {
            policyManager.auditPolicy("policy-" + i, "user-" + i, "READ", "ALLOW");
        }
        List<PlatformPolicyAudit> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);

        // Run 40 policy version updates
        for (int i = 1; i <= 40; i++) {
            policyManager.updatePolicyVersion("policy-" + i, "v1.1.0", "package authz default allow = true", "admin", "Upgrade security rules " + i);
        }
        List<PlatformPolicyVersion> versions = versionRepo.findAll();
        assertTrue(versions.size() >= 40);

        List<PlatformPolicyHistory> history = historyRepo.findAll();
        assertTrue(history.size() >= 40);

        // Register 40 compliance controls
        complianceService.registerFramework("SOC2", "SOC 2 Type II Compliance Framework");
        for (int i = 1; i <= 40; i++) {
            complianceService.addControl("SOC2", "CC6.1-" + i, "Access Control Policy verification " + i);
        }
        List<PlatformComplianceControl> controls = controlRepo.findAll();
        assertTrue(controls.size() >= 40);
    }
}
