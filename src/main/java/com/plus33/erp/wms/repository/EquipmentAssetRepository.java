package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.EquipmentAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EquipmentAssetRepository extends JpaRepository<EquipmentAsset, Long> {
    Optional<EquipmentAsset> findByAssetCode(String assetCode);
    List<EquipmentAsset> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
}
