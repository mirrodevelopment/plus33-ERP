package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetMaintenanceRepository extends JpaRepository<FixedAssetMaintenance, Long> {
    List<FixedAssetMaintenance> findAllByFixedAssetId(Long fixedAssetId);
}
