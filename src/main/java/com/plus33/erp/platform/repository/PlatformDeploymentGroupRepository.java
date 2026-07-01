package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformDeploymentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlatformDeploymentGroupRepository extends JpaRepository<PlatformDeploymentGroup, Long> {
    Optional<PlatformDeploymentGroup> findByGroupName(String groupName);
}