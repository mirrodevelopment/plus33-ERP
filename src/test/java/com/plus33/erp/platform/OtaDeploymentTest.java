package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.fleet.ota.OtaPackageManager;
import com.plus33.erp.fleet.ota.CampaignCoordinator;

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
public class OtaDeploymentTest {

    @Autowired OtaPackageManager otaPackageManager;
    @Autowired CampaignCoordinator campaignCoordinator;

    @Autowired PlatformOtaPackageRepository packageRepo;
    @Autowired PlatformOtaCampaignRepository campaignRepo;
    @Autowired PlatformOtaNodeExecutionRepository executionRepo;
    @Autowired PlatformOtaAuditLogRepository auditRepo;

    @Test
    void testOtaDeploymentScenarios() {
        // OTA Packages upload and registry over 40 iterations
        for (int i = 1; i <= 40; i++) {
            PlatformOtaPackage pkg = otaPackageManager.createPackage("edge-agent-service-" + i, "2.0." + i, "CHECKSUM-SHA256-" + i, "SIG-RSA-SHA256-" + i);
            assertNotNull(pkg);
        }

        List<PlatformOtaPackage> packages = packageRepo.findAll();
        assertTrue(packages.size() >= 40);
        assertEquals("GZIP", packages.get(0).getCompression());

        // Progressive rollout deployment campaigns over 40 iterations
        PlatformOtaPackage testPkg = packages.get(0);
        for (int i = 1; i <= 40; i++) {
            campaignCoordinator.createCampaign("CAMPAIGN_OTA_" + i, testPkg.getId(), 1L);
        }

        List<PlatformOtaCampaign> campaigns = campaignRepo.findAll();
        assertTrue(campaigns.size() >= 40);
        assertEquals("ACTIVE", campaigns.get(0).getStatus());

        List<PlatformOtaNodeExecution> executions = executionRepo.findAll();
        assertTrue(executions.size() >= 40);
        assertEquals("SUCCESS", executions.get(0).getExecutionStatus());

        List<PlatformOtaAuditLog> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);
        assertEquals("fleet-operator", audits.get(0).getOperator());
    }
}
