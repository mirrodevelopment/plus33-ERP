package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.compliance.profile.ConfigurationProfileService;

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
public class ConfigProfileTest {

    @Autowired ConfigurationProfileService profileService;

    @Autowired PlatformDeviceConfigProfileRepository profileRepo;
    @Autowired PlatformComplianceAuditLogRepository auditRepo;

    @Test
    void testConfigProfileScenarios() {
        // Device configuration profile schema registers over 40 iterations
        for (int i = 1; i <= 40; i++) {
            profileService.createProfile("PROFILE_CODE_" + i, "Warehouse Profile " + i, "v1.2." + i, "Warehouse Profile");
        }

        List<PlatformDeviceConfigProfile> profiles = profileRepo.findAll();
        assertTrue(profiles.size() >= 40);
        assertEquals("Warehouse Profile", profiles.get(0).getAssignmentScope());

        List<PlatformComplianceAuditLog> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);
        assertEquals("sec-admin", audits.get(0).getOperator());
        assertEquals("UPDATE_POLICY", audits.get(0).getActionType());
    }
}
