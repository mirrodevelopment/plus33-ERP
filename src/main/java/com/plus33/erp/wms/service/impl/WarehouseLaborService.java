/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service.impl
 * File              : WarehouseLaborService.java
 * Purpose           : Business logic service layer for Wms Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseLaborController
 * Related Service   : WarehouseLaborService
 * Related Repository: WarehouseLaborLogRepository
 * Related Entity    : WarehouseLabor
 * Related DTO       : N/A
 * Related Mapper    : WarehouseLaborMapper
 * Related DB Table  : warehouse_labors
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseLaborController, WarehouseLaborServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Wms Module. Implements WarehouseLaborService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.WarehouseLaborLog;
import com.plus33.erp.wms.repository.WarehouseLaborLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseLaborService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Wms Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * WarehouseLaborController
 *   --> WarehouseLaborService (this)
 *   --> Validate business rules
 *   --> WarehouseLaborRepository (read/write 'warehouse_labors')
 *   --> WarehouseLaborMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code warehouse_labors}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class WarehouseLaborService {

    private final WarehouseLaborLogRepository laborRepo;

    public WarehouseLaborService(WarehouseLaborLogRepository laborRepo) {
        this.laborRepo = laborRepo;
    }

    /**
     * Performs the logTaskLabor operation in this module.
     *
     * @return the WarehouseLaborLog result
     */
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