package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetAttachmentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FixedAssetAttachmentVersionRepository extends JpaRepository<FixedAssetAttachmentVersion, Long> {
    List<FixedAssetAttachmentVersion> findAllByAttachmentIdOrderByVersionNumberDesc(Long attachmentId);
    Optional<FixedAssetAttachmentVersion> findByAttachmentIdAndVersionNumber(Long attachmentId, Integer versionNumber);
}
