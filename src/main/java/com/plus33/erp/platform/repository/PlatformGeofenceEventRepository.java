package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformGeofenceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformGeofenceEventRepository extends JpaRepository<PlatformGeofenceEvent, Long> {
}