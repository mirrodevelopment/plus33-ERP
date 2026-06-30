package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.WarehouseLaborLog;
import com.plus33.erp.wms.repository.WarehouseLaborLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class WarehouseLaborService {

    private final WarehouseLaborLogRepository laborRepo;

    public WarehouseLaborService(WarehouseLaborLogRepository laborRepo) {
        this.laborRepo = laborRepo;
    }

    public WarehouseLaborLog logTaskLabor(Long companyId, Long warehouseId, Long userId, Long taskId, String taskType,
                                          LocalDateTime startTime, LocalDateTime endTime) {
        WarehouseLaborLog log = new WarehouseLaborLog();
        log.setCompanyId(companyId);
        log.setWarehouseId(warehouseId);
        log.setUserId(userId);
        log.setTaskId(taskId);
        log.setTaskType(taskType);
        log.setStartTime(startTime);
        log.setEndTime(endTime);
        return laborRepo.save(log);
    }
}
