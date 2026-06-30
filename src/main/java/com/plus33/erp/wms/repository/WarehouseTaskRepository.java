package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WarehouseTaskRepository extends JpaRepository<WarehouseTask, Long> {
    List<WarehouseTask> findByCompanyIdAndWarehouseIdAndTaskStatus(Long companyId, Long warehouseId, String status);
    List<WarehouseTask> findByAssignedToAndTaskStatus(Long userId, String status);
    List<WarehouseTask> findByTaskTypeAndTaskStatus(String taskType, String status);
    List<WarehouseTask> findByRefTypeAndRefId(String refType, Long refId);
}
