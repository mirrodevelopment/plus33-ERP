package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetInsuranceClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetInsuranceClaimRepository extends JpaRepository<FixedAssetInsuranceClaim, Long> {
    List<FixedAssetInsuranceClaim> findAllByFixedAssetId(Long fixedAssetId);
}
