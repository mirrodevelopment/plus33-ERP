/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.config
 * File              : DistributedConfigService.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DistributedConfigController
 * Related Service   : DistributedConfigService
 * Related Repository: DistributedConfigRepository
 * Related Entity    : DistributedConfig
 * Related DTO       : N/A
 * Related Mapper    : DistributedConfigMapper
 * Related DB Table  : distributed_configs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DistributedConfigController, DistributedConfigServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements DistributedConfigService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.config;

import com.plus33.erp.platform.entity.PlatformConfig;
import com.plus33.erp.platform.repository.PlatformConfigRepository;
import com.plus33.erp.platform.versioning.ConfigVersionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code DistributedConfigService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.config}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DistributedConfigController
 *   --> DistributedConfigService (this)
 *   --> Validate business rules
 *   --> DistributedConfigRepository (read/write 'distributed_configs')
 *   --> DistributedConfigMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code distributed_configs}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DistributedConfigService {
    /**
     * Retrieves config data from the database.
     *
     * @param key the key input value
     * @param profile the profile input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Autowired PlatformConfigRepository configRepo;
    @Autowired ConfigVersionManager versionManager;
    public String getConfig(String key, String profile) {
        return configRepo.findAll().stream()
                .filter(c -> c.getConfigKey().equals(key) && c.getProfile().equalsIgnoreCase(profile))
                .map(PlatformConfig::getConfigValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * Performs the setConfig operation in this module.
     *
     * @param key the key input value
     * @param value the value input value
     * @param profile the profile input value
     * @param operator the operator input value
     */
    @Transactional
    public void setConfig(String key, String value, String profile, String operator) {
        PlatformConfig config = configRepo.findAll().stream()
                .filter(c -> c.getConfigKey().equals(key) && c.getProfile().equalsIgnoreCase(profile))
                .findFirst()
                .orElse(null);

        if (config == null) {
            config = new PlatformConfig();
            config.setConfigKey(key);
            config.setConfigValue(value);
            config.setProfile(profile);
            config.setCreatedAt(LocalDateTime.now());
            config.setUpdatedAt(LocalDateTime.now());
            config = configRepo.save(config);
        } else {
            config.setConfigValue(value);
            config.setUpdatedAt(LocalDateTime.now());
            config = configRepo.save(config);
        }

        versionManager.recordVersion(config, operator);
    }

    /**
     * Performs the rollbackConfig operation in this module.
     *
     * @param key the key input value
     * @param profile the profile input value
     * @param targetVersion the targetVersion input value
     * @param operator the operator input value
     */
    @Transactional
    public void rollbackConfig(String key, String profile, int targetVersion, String operator) {
        PlatformConfig config = configRepo.findAll().stream()
                .filter(c -> c.getConfigKey().equals(key) && c.getProfile().equalsIgnoreCase(profile))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Config not found"));

        versionManager.rollback(config, targetVersion, operator);
    }
}