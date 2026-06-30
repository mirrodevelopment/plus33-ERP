package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseEventStoreItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WarehouseEventStoreRepository extends JpaRepository<WarehouseEventStoreItem, Long> {
    List<WarehouseEventStoreItem> findByAggregateTypeAndAggregateIdOrderByVersionNumberAsc(String aggregateType, Long aggregateId);
}
