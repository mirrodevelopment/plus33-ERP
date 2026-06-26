package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetAttachmentRepository extends JpaRepository<FixedAssetAttachment, Long> {
    List<FixedAssetAttachment> findAllByFixedAssetId(Long fixedAssetId);
}
