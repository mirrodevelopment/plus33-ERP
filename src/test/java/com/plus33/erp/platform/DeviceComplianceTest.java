package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.compliance.policy.CompliancePolicyService;
import com.plus33.erp.compliance.policy.ComplianceChecker;
import com.plus33.erp.compliance.profile.DriftDetector;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DeviceComplianceTest {

    @Autowired CompliancePolicyService policyService;
    @Autowired ComplianceChecker complianceChecker;
    @Autowired DriftDetector driftDetector;

    @Autowired PlatformDeviceCompliancePolicyRepository policyRepo;
    @Autowired PlatformDeviceComplianceLogRepository complianceLogRepo;
    @Autowired PlatformDeviceDriftLogRepository driftRepo;

    @Test
    void testDeviceComplianceScenarios() {
        // Device compliance policies registry over 40 iterations
        for (int i = 1; i <= 40; i++) {
            PlatformDeviceCompliancePolicy policy = policyService.createPolicy("POLICY_CODE_" + i, "Compliance Policy " + i, "SECURITY", "High");
            assertNotNull(policy);
        }

        List<PlatformDeviceCompliancePolicy> policies = policyRepo.findAll();
        assertTrue(policies.size() >= 40);
        assertEquals("openssl, ufw", policies.get(0).getRequiredPackages());

        // Compliance checks evaluations over 40 iterations
        PlatformDeviceCompliancePolicy testPolicy = policies.get(0);
        for (int i = 1; i <= 40; i++) {
            complianceChecker.recordCompliance(1L, testPolicy.getId(), "PASS");
        }

        List<PlatformDeviceComplianceLog> logs = complianceLogRepo.findAll();
        assertTrue(logs.size() >= 40);
        assertEquals("PASS", logs.get(0).getResult());

        // Device configuration drift reports logs over 40 iterations
        for (int i = 1; i <= 40; i++) {
            driftDetector.recordDrift(1L, "BASELINE-SHA256-HASH-" + i, "CURRENT-SHA256-HASH-" + i);
        }

        List<PlatformDeviceDriftLog> drifts = driftRepo.findAll();
        assertTrue(drifts.size() >= 40);
        assertEquals("/etc/ssh/sshd_config", drifts.get(0).getChangedFiles());
    }
}
