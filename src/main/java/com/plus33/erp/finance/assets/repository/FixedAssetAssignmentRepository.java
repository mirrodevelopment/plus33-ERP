package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FixedAssetAssignmentRepository extends JpaRepository<FixedAssetAssignment, Long> {
    List<FixedAssetAssignment> findAllByFixedAssetId(Long fixedAssetId);
    Optional<FixedAssetAssignment> findTopByFixedAssetIdAndReleasedAtIsNull(Long fixedAssetId);
}
