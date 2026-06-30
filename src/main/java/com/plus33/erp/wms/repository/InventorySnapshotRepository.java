package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.InventorySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface InventorySnapshotRepository extends JpaRepository<InventorySnapshot, Long> {
    List<InventorySnapshot> findByCompanyIdAndWarehouseIdAndSnapshotDate(Long companyId, Long warehouseId, LocalDate snapshotDate);
}
