package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.ReplenishmentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReplenishmentTaskRepository extends JpaRepository<ReplenishmentTask, Long> {
    List<ReplenishmentTask> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
    List<ReplenishmentTask> findByAssignedToAndStatus(Long userId, String status);
}
