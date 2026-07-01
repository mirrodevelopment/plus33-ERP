package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformTwinPredictionSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformTwinPredictionSnapshotRepository extends JpaRepository<PlatformTwinPredictionSnapshot, Long> {
}