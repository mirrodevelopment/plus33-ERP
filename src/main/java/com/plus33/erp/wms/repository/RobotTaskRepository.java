package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.RobotTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RobotTaskRepository extends JpaRepository<RobotTask, Long> {
    List<RobotTask> findByStatus(String status);
}
