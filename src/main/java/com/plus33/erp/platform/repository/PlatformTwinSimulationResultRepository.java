package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformTwinSimulationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformTwinSimulationResultRepository extends JpaRepository<PlatformTwinSimulationResult, Long> {
}