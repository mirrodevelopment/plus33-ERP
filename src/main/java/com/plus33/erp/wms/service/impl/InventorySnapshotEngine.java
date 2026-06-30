package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.InventorySnapshot;
import com.plus33.erp.wms.repository.InventorySnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class InventorySnapshotEngine {

    private final InventorySnapshotRepository snapshotRepo;

    public InventorySnapshotEngine(InventorySnapshotRepository snapshotRepo) {
        this.snapshotRepo = snapshotRepo;
    }

    public InventorySnapshot createDailySnapshot(Long companyId, Long warehouseId, Long locationId, Long productId,
                                                  String lotNumber, BigDecimal qty) {
        InventorySnapshot snapshot = new InventorySnapshot();
        snapshot.setCompanyId(companyId);
        snapshot.setWarehouseId(warehouseId);
        snapshot.setLocationId(locationId);
        snapshot.setProductId(productId);
        snapshot.setLotNumber(lotNumber);
        snapshot.setSnapshotDate(LocalDate.now());
        snapshot.setOnHandQuantity(qty);
        snapshot.setAvailableQuantity(qty);
        return snapshotRepo.save(snapshot);
    }
}
