package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformRegionProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRegionProfileRepository extends JpaRepository<PlatformRegionProfile, Long> {
}