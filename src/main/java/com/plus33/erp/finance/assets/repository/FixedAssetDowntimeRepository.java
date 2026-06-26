package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetDowntime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetDowntimeRepository extends JpaRepository<FixedAssetDowntime, Long> {
    List<FixedAssetDowntime> findAllByFixedAssetIdOrderByStartTimeDesc(Long fixedAssetId);
}
