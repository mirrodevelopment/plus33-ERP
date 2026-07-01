package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformDiscoveryNode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlatformDiscoveryNodeRepository extends JpaRepository<PlatformDiscoveryNode, Long> {
    Optional<PlatformDiscoveryNode> findByNodeCode(String nodeCode);
}