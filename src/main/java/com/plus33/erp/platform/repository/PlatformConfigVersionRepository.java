package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformConfigVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlatformConfigVersionRepository extends JpaRepository<PlatformConfigVersion, Long> {
    List<PlatformConfigVersion> findByConfigIdOrderByVersionDesc(Long configId);
}