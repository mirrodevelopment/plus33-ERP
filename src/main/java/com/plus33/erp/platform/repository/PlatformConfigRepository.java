package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlatformConfigRepository extends JpaRepository<PlatformConfig, Long> {
    Optional<PlatformConfig> findByConfigKey(String configKey);
}