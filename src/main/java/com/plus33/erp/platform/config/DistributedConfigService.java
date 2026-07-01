package com.plus33.erp.platform.config;

import com.plus33.erp.platform.entity.PlatformConfig;
import com.plus33.erp.platform.repository.PlatformConfigRepository;
import com.plus33.erp.platform.versioning.ConfigVersionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DistributedConfigService {
    @Autowired PlatformConfigRepository configRepo;
    @Autowired ConfigVersionManager versionManager;

    public String getConfig(String key, String profile) {
        return configRepo.findAll().stream()
                .filter(c -> c.getConfigKey().equals(key) && c.getProfile().equalsIgnoreCase(profile))
                .map(PlatformConfig::getConfigValue)
                .findFirst()
                .orElse(null);
    }

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

    @Transactional
    public void rollbackConfig(String key, String profile, int targetVersion, String operator) {
        PlatformConfig config = configRepo.findAll().stream()
                .filter(c -> c.getConfigKey().equals(key) && c.getProfile().equalsIgnoreCase(profile))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Config not found"));

        versionManager.rollback(config, targetVersion, operator);
    }
}