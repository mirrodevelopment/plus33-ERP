package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetMaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FixedAssetMaintenancePlanRepository extends JpaRepository<FixedAssetMaintenancePlan, Long> {
    List<FixedAssetMaintenancePlan> findAllByFixedAssetId(Long fixedAssetId);
    List<FixedAssetMaintenancePlan> findAllByFixedAssetIdAndActiveTrue(Long fixedAssetId);
    List<FixedAssetMaintenancePlan> findAllByActiveTrueAndNextMaintenanceDateLessThanEqual(LocalDate date);
}
