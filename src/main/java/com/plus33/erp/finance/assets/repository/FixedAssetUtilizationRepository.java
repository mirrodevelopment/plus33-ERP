package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetUtilization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetUtilizationRepository extends JpaRepository<FixedAssetUtilization, Long> {
    List<FixedAssetUtilization> findAllByFixedAssetIdOrderByReadingDateDesc(Long fixedAssetId);
}
