package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.EsmEventStoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EsmEventStoreItemRepository extends JpaRepository<EsmEventStoreItem, Long> {
}
