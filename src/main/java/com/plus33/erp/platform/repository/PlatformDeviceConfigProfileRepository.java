package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformDeviceConfigProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformDeviceConfigProfileRepository extends JpaRepository<PlatformDeviceConfigProfile, Long> {
}