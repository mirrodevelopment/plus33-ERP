package com.plus33.erp.crm.event;

import com.plus33.erp.crm.entity.CrmEventStoreItem;
import com.plus33.erp.crm.repository.CrmEventStoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CrmEventStore {

    private final CrmEventStoreRepository repo;

    public CrmEventStore(CrmEventStoreRepository repo) {
        this.repo = repo;
    }

    public CrmEventStoreItem recordEvent(Long companyId, String eventType, String payload, String key) {
        CrmEventStoreItem item = new CrmEventStoreItem();
        item.setCompanyId(companyId);
        item.setEventType(eventType);
        item.setPayloadJson(payload);
        item.setIdempotencyKey(key);
        return repo.save(item);
    }

    @Transactional(readOnly = true)
    public List<CrmEventStoreItem> getEvents(Long companyId) {
        return repo.findByCompanyIdOrderByOccurredAtAsc(companyId);
    }
}
