package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.OfflineQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OfflineQueueItemRepository extends JpaRepository<OfflineQueueItem, Long> {
    List<OfflineQueueItem> findByCompanyIdAndDeviceIdAndStatus(Long companyId, String deviceId, String status);
    List<OfflineQueueItem> findByStatusOrderByLoggedAtAsc(String status);
}
