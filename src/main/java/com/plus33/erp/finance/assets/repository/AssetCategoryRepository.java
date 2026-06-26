package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.AssetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetCategoryRepository extends JpaRepository<AssetCategory, Long> {
    Optional<AssetCategory> findByCompanyIdAndCode(Long companyId, String code);
    List<AssetCategory> findAllByCompanyId(Long companyId);
}
