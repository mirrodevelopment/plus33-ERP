package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetTransferRepository extends JpaRepository<FixedAssetTransfer, Long> {
    List<FixedAssetTransfer> findAllByFixedAssetId(Long fixedAssetId);
}
