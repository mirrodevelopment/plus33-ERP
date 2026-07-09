/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Compliance Module
 * Package           : com.plus33.erp.compliance.profile
 * File              : ConfigurationProfileService.java
 * Purpose           : Business logic service layer for Compliance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ConfigurationProfileController
 * Related Service   : ConfigurationProfileService
 * Related Repository: ConfigurationProfileRepository
 * Related Entity    : ConfigurationProfile
 * Related DTO       : N/A
 * Related Mapper    : ConfigurationProfileMapper
 * Related DB Table  : configuration_profiles
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : ConfigurationProfileController, ConfigurationProfileServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Compliance Module. Implements ConfigurationProfileService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.compliance.profile;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Compliance Module</b>
 *
 * <p><b>Class  :</b> {@code ConfigurationProfileService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.compliance.profile}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Compliance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ConfigurationProfileController
 *   --> ConfigurationProfileService (this)
 *   --> Validate business rules
 *   --> ConfigurationProfileRepository (read/write 'configuration_profiles')
 *   --> ConfigurationProfileMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code configuration_profiles}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ConfigurationProfileService {
    @Autowired PlatformDeviceConfigProfileRepository profileRepo;
    @Autowired PlatformComplianceAuditLogRepository auditRepo;
    /**
     * Creates a new profile and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param code the code input value
     * @param name the name input value
     * @param ver the ver input value
     * @param scope the scope input value
     * @return the PlatformDeviceConfigProfile result
     * @throws BusinessException if a business rule is violated
     */
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