/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.versioning
 * File              : ConfigVersionManager.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ConfigVersionManagerController
 * Related Service   : ConfigVersionManager
 * Related Repository: ConfigVersionManagerRepository
 * Related Entity    : ConfigVersionManager
 * Related DTO       : N/A
 * Related Mapper    : ConfigVersionManagerMapper
 * Related DB Table  : config_version_managers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ConfigVersionManagerController, ConfigVersionManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements ConfigVersionManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.versioning;

import com.plus33.erp.platform.entity.PlatformConfig;
import com.plus33.erp.platform.entity.PlatformConfigVersion;
import com.plus33.erp.platform.repository.PlatformConfigRepository;
import com.plus33.erp.platform.repository.PlatformConfigVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code ConfigVersionManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.versioning}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ConfigVersionManagerController
 *   --> ConfigVersionManager (this)
 *   --> Validate business rules
 *   --> ConfigVersionManagerRepository (read/write 'config_version_managers')
 *   --> ConfigVersionManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code config_version_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ConfigVersionManager {
    @Autowired PlatformConfigVersionRepository versionRepo;
    @Autowired PlatformConfigRepository configRepo;
    /**
     * Performs the recordVersion operation in this module.
     *
     * @param config the config input value
     * @param modifiedBy the modifiedBy input value
     */
    @Transactional
    public void recordVersion(PlatformConfig config, String modifiedBy) {
        // Find if a version already exists
        Integer latestVer = versionRepo.findAll().stream()
                .filter(v -> v.getConfigId().equals(config.getId()))
                .map(PlatformConfigVersion::getVersion)
                .max(Integer::compareTo)
                .orElse(0);

        LocalDateTime now = LocalDateTime.now();

        // Expire previous version
        if (latestVer > 0) {
            versionRepo.findAll().stream()
                .filter(v -> v.getConfigId().equals(config.getId()) && v.getVersion().equals(latestVer))
                .findFirst()
                .ifPresent(prev -> {
                    prev.setEffectiveTo(now);
                    versionRepo.save(prev);
                });
        }

        PlatformConfigVersion ver = new PlatformConfigVersion();
        ver.setConfigId(config.getId());
        ver.setVersion(latestVer + 1);
        ver.setPreviousVersion(latestVer > 0 ? latestVer : null);
        ver.setConfigValue(config.getConfigValue());
        ver.setEffectiveFrom(now);
        ver.setChecksum(UUID.randomUUID().toString().replace("-", ""));
        ver.setModifiedBy(modifiedBy);
        ver.setCreatedAt(now);

        versionRepo.save(ver);
    }

    /**
     * Performs the rollback operation in this module.
     *
     * @param config the config input value
     * @param targetVersion the targetVersion input value
     * @param operator the operator input value
     */
    @Transactional
    public void rollback(PlatformConfig config, int targetVersion, String operator) {
        PlatformConfigVersion target = versionRepo.findAll().stream()
                .filter(v -> v.getConfigId().equals(config.getId()) && v.getVersion() == targetVersion)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Version not found"));

        config.setConfigValue(target.getConfigValue());
        config.setUpdatedAt(LocalDateTime.now());
        configRepo.save(config);

        recordVersion(config, operator);
    }
}