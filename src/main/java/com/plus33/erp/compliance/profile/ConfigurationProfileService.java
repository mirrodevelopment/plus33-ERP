package com.plus33.erp.compliance.profile;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class ConfigurationProfileService {
    @Autowired PlatformDeviceConfigProfileRepository profileRepo;
    @Autowired PlatformComplianceAuditLogRepository auditRepo;

    @Transactional
    public PlatformDeviceConfigProfile createProfile(String code, String name, String ver, String scope) {
        PlatformDeviceConfigProfile p = new PlatformDeviceConfigProfile();
        p.setProfileCode(code);
        p.setProfileName(name);
        p.setProfileVersion(ver);
        p.setChecksum("SHA256-PROFILE-CHECKSUM-" + code);
        p.setConfigurationJson("{ \"network\": \"dhcp\", \"dns\": [\"8.8.8.8\"] }");
        p.setRollbackProfileId(null);
        p.setEffectiveFrom(LocalDateTime.now());
        p.setEffectiveTo(LocalDateTime.now().plusYears(1));
        p.setAssignmentScope(scope);
        p = profileRepo.save(p);

        PlatformComplianceAuditLog audit = new PlatformComplianceAuditLog();
        audit.setDeviceId(1L);
        audit.setOperator("sec-admin");
        audit.setActionType("UPDATE_POLICY");
        audit.setNewState("{ \"profileId\": " + p.getId() + " }");
        audit.setTraceId("TRACE-ID-COMPLIANCE-INIT");
        audit.setIpAddress("127.0.0.1");
        auditRepo.save(audit);

        return p;
    }
}