package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.OfflineQueueItem;
import com.plus33.erp.wms.repository.OfflineQueueItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OfflineSyncEngine {

    private final OfflineQueueItemRepository queueRepo;

    public OfflineSyncEngine(OfflineQueueItemRepository queueRepo) {
        this.queueRepo = queueRepo;
    }

    public OfflineQueueItem queueOfflineTransaction(Long companyId, String deviceId, String eventType, String payloadJson) {
        OfflineQueueItem item = new OfflineQueueItem();
        item.setCompanyId(companyId);
        item.setDeviceId(deviceId);
        item.setEventType(eventType);
        item.setPayloadJson(payloadJson);
        item.setLoggedAt(LocalDateTime.now());
        item.setStatus("PENDING");
        return queueRepo.save(item);
    }

    public List<OfflineQueueItem> replayQueue() {
        List<OfflineQueueItem> items = queueRepo.findByStatusOrderByLoggedAtAsc("PENDING");
        items.forEach(item -> {
            item.setStatus("SYNCED");
            item.setSyncedAt(LocalDateTime.now());
        });
        return queueRepo.saveAll(items);
    }
}
