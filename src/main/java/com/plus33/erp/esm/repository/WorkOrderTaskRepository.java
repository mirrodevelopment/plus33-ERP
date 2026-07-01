package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.WorkOrderTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkOrderTaskRepository extends JpaRepository<WorkOrderTask, Long> {
    List<WorkOrderTask> findByWorkOrderId(Long workOrderId);
}
