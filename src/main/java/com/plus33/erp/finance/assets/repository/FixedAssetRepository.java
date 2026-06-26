package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAsset;
import com.plus33.erp.finance.assets.entity.FixedAssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FixedAssetRepository extends JpaRepository<FixedAsset, Long>, JpaSpecificationExecutor<FixedAsset> {
    Optional<FixedAsset> findByCompanyIdAndAssetCode(Long companyId, String assetCode);
    Optional<FixedAsset> findByCompanyIdAndId(Long companyId, Long id);
    List<FixedAsset> findAllByCompanyIdAndStatus(Long companyId, FixedAssetStatus status);
    List<FixedAsset> findAllByCompanyId(Long companyId);
    List<FixedAsset> findAllByParentAssetId(Long parentAssetId);

    @Query(value = "SELECT nextval('fixed_asset_code_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
