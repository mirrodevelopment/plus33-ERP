package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetAuditItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetAuditItemRepository extends JpaRepository<FixedAssetAuditItem, Long> {
    List<FixedAssetAuditItem> findAllByAuditId(Long auditId);
}
