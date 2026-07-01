package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformFeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlatformFeatureFlagRepository extends JpaRepository<PlatformFeatureFlag, Long> {
    Optional<PlatformFeatureFlag> findByFlagKey(String flagKey);
}