package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformTelemetryRetentionPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformTelemetryRetentionPolicyRepository extends JpaRepository<PlatformTelemetryRetentionPolicy, Long> {
}