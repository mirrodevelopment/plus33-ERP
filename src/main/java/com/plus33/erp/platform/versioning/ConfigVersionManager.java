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

@Service
public class ConfigVersionManager {
    @Autowired PlatformConfigVersionRepository versionRepo;
    @Autowired PlatformConfigRepository configRepo;

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