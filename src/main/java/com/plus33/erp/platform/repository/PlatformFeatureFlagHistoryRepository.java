package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformFeatureFlagHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlatformFeatureFlagHistoryRepository extends JpaRepository<PlatformFeatureFlagHistory, Long> {
    List<PlatformFeatureFlagHistory> findByFlagKeyOrderByChangedAtDesc(String flagKey);
}