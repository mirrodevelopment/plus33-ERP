package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.RobotTask;
import com.plus33.erp.wms.repository.RobotTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RobotTaskEngine {

    private final RobotTaskRepository robotTaskRepo;

    public RobotTaskEngine(RobotTaskRepository robotTaskRepo) {
        this.robotTaskRepo = robotTaskRepo;
    }

    public RobotTask dispatchRobotTask(Long warehouseTaskId, String providerKey, String payloadJson) {
        RobotTask task = new RobotTask();
        task.setWarehouseTaskId(warehouseTaskId);
        task.setRobotProviderKey(providerKey);
        task.setPayloadJson(payloadJson);
        task.setStatus("DISPATCHED");
        return robotTaskRepo.save(task);
    }
}
