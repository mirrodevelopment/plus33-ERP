package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiEventStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiEventStoreRepository extends JpaRepository<BiEventStore, Long> {
    java.util.List<BiEventStore> findByCompanyIdAndEventTypeOrderByOccurredAtDesc(Long companyId, String eventType);
}
