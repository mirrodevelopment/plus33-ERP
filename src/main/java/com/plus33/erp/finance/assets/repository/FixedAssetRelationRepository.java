package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetRelationRepository extends JpaRepository<FixedAssetRelation, Long> {
    List<FixedAssetRelation> findAllBySourceAssetId(Long sourceAssetId);
    List<FixedAssetRelation> findAllByTargetAssetId(Long targetAssetId);
}
