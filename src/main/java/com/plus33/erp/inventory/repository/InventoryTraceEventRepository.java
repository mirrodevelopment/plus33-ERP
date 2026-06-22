package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryTraceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InventoryTraceEventRepository extends JpaRepository<InventoryTraceEvent, Long>, JpaSpecificationExecutor<InventoryTraceEvent> {

    List<InventoryTraceEvent> findByProductIdOrderByCreatedAtDesc(Long productId);

    List<InventoryTraceEvent> findByLotLotNumberOrderByCreatedAtDesc(String lotNumber);

    List<InventoryTraceEvent> findBySerialSerialNumberOrderByCreatedAtDesc(String serialNumber);
}
