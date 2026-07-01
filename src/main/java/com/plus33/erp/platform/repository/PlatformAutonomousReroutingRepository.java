package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformAutonomousRerouting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformAutonomousReroutingRepository extends JpaRepository<PlatformAutonomousRerouting, Long> {
}