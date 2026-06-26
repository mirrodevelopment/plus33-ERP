package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetHistoryRepository extends JpaRepository<FixedAssetHistory, Long> {
    List<FixedAssetHistory> findAllByFixedAssetIdOrderByEventDateDescCreatedAtDesc(Long fixedAssetId);
}
