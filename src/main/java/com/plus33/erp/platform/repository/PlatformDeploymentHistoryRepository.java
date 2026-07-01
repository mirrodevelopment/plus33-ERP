package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformDeploymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlatformDeploymentHistoryRepository extends JpaRepository<PlatformDeploymentHistory, Long> {
    List<PlatformDeploymentHistory> findByDeploymentVersion(String deploymentVersion);
}