package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetDepreciationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetDepreciationLogRepository extends JpaRepository<FixedAssetDepreciationLog, Long> {
    List<FixedAssetDepreciationLog> findAllByFixedAssetId(Long fixedAssetId);
}
