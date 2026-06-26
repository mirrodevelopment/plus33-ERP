package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetLease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FixedAssetLeaseRepository extends JpaRepository<FixedAssetLease, Long> {
    Optional<FixedAssetLease> findByFixedAssetId(Long fixedAssetId);
    List<FixedAssetLease> findAllByFixedAssetId(Long fixedAssetId);
}
