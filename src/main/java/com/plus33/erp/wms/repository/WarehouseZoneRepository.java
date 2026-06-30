package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseZoneRepository extends JpaRepository<WarehouseZone, Long> {
    Optional<WarehouseZone> findByWarehouseIdAndCode(Long warehouseId, String code);
    List<WarehouseZone> findByCompanyIdAndWarehouseId(Long companyId, Long warehouseId);
    List<WarehouseZone> findByCompanyIdAndWarehouseIdAndActiveTrue(Long companyId, Long warehouseId);
}
