package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetImpairment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetImpairmentRepository extends JpaRepository<FixedAssetImpairment, Long> {
    List<FixedAssetImpairment> findAllByFixedAssetIdOrderByImpairmentDateDesc(Long fixedAssetId);
}
