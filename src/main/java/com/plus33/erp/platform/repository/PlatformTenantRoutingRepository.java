package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformTenantRouting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlatformTenantRoutingRepository extends JpaRepository<PlatformTenantRouting, Long> {
    Optional<PlatformTenantRouting> findByTenantId(String tenantId);
}