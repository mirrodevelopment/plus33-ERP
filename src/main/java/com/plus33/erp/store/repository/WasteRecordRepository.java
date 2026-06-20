package com.plus33.erp.store.repository;

import com.plus33.erp.store.entity.WasteRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WasteRecordRepository extends JpaRepository<WasteRecord, Long> {
    List<WasteRecord> findByStoreId(Long storeId);
}
