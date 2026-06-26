package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetFxLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetFxLogRepository extends JpaRepository<FixedAssetFxLog, Long> {
    List<FixedAssetFxLog> findAllByFixedAssetIdOrderByValuationDateDesc(Long fixedAssetId);
}
