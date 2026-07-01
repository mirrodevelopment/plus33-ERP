package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmEventStoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HcmEventStoreItemRepository extends JpaRepository<HcmEventStoreItem, Long> {
}
