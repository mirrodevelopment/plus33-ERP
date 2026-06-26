package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetRevaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetRevaluationRepository extends JpaRepository<FixedAssetRevaluation, Long> {
    List<FixedAssetRevaluation> findAllByFixedAssetIdOrderByRevaluationDateDesc(Long fixedAssetId);
}
