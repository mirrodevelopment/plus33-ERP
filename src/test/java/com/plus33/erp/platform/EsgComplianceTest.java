package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.esg.compliance.SustainabilityComplianceService;
import com.plus33.erp.routing.esg.report.EsgReportingService;

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
public class EsgComplianceTest {

    @Autowired SustainabilityComplianceService complianceService;
    @Autowired EsgReportingService reportService;

    @Autowired PlatformEsgCarbonOffsetRepository offsetRepo;
    @Autowired PlatformEsgComplianceLogRepository complianceRepo;
    @Autowired PlatformEsgAuditLogRepository auditRepo;

    @Test
    void testEsgComplianceScenarios() {
        // ESG compliance and offsets registry over 40 iterations
        for (int i = 1; i <= 40; i++) {
            complianceService.registerOffset("CERT_NUM_" + i);
            complianceService.verifyCompliance("GRI");
            reportService.auditEsgReport("v60.0");
        }

        List<PlatformEsgCarbonOffset> offsets = offsetRepo.findAll();
        assertTrue(offsets.size() >= 40);
        assertEquals("GoldStandard", offsets.get(0).getVerificationStandard());

        List<PlatformEsgComplianceLog> compliance = complianceRepo.findAll();
        assertTrue(compliance.size() >= 40);
        assertEquals("GRI", compliance.get(0).getFramework());

        List<PlatformEsgAuditLog> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);
        assertEquals("v60.0", audits.get(0).getReportVersion());
    }
}
