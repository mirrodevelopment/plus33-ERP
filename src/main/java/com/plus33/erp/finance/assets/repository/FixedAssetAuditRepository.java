package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FixedAssetAuditRepository extends JpaRepository<FixedAssetAudit, Long> {
    Optional<FixedAssetAudit> findByCompanyIdAndId(Long companyId, Long id);
    List<FixedAssetAudit> findAllByCompanyId(Long companyId);
}
