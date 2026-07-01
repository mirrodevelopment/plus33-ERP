package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.ProcurementEventStoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcurementEventStoreItemRepository extends JpaRepository<ProcurementEventStoreItem, Long> {
}
