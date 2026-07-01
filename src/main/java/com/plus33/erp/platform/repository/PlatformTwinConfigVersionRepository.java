package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformTwinConfigVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformTwinConfigVersionRepository extends JpaRepository<PlatformTwinConfigVersion, Long> {
}