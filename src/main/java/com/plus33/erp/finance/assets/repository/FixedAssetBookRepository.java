package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FixedAssetBookRepository extends JpaRepository<FixedAssetBook, Long> {
    Optional<FixedAssetBook> findByFixedAssetIdAndDepreciationBookId(Long fixedAssetId, Long depreciationBookId);
    List<FixedAssetBook> findAllByFixedAssetId(Long fixedAssetId);
}
