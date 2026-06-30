package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseLaborLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WarehouseLaborLogRepository extends JpaRepository<WarehouseLaborLog, Long> {
    List<WarehouseLaborLog> findByCompanyIdAndWarehouseId(Long companyId, Long warehouseId);
    List<WarehouseLaborLog> findByUserId(Long userId);
}
