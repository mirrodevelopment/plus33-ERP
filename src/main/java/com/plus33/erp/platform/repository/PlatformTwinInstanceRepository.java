package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformTwinInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformTwinInstanceRepository extends JpaRepository<PlatformTwinInstance, Long> {
}