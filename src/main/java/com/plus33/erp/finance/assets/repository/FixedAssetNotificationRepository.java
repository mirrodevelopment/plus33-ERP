package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetNotificationRepository extends JpaRepository<FixedAssetNotification, Long> {
    List<FixedAssetNotification> findAllByCompanyIdAndReadFalseOrderByCreatedAtDesc(Long companyId);
}
