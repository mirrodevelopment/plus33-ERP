package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformK8sResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformK8sResourceRepository extends JpaRepository<PlatformK8sResource, Long> {
}