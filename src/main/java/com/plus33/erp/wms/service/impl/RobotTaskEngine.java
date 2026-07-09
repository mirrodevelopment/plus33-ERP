/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service.impl
 * File              : RobotTaskEngine.java
 * Purpose           : Business logic service layer for Wms Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RobotTaskEngineController
 * Related Service   : RobotTaskEngine
 * Related Repository: RobotTaskRepository
 * Related Entity    : RobotTaskEngine
 * Related DTO       : N/A
 * Related Mapper    : RobotTaskEngineMapper
 * Related DB Table  : robot_task_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RobotTaskEngineController, RobotTaskEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Wms Module. Implements RobotTaskEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.RobotTask;
import com.plus33.erp.wms.repository.RobotTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code RobotTaskEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Wms Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * RobotTaskEngineController
 *   --> RobotTaskEngine (this)
 *   --> Validate business rules
 *   --> RobotTaskEngineRepository (read/write 'robot_task_engines')
 *   --> RobotTaskEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code robot_task_engines}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class RobotTaskEngine {

    private final RobotTaskRepository robotTaskRepo;

    public RobotTaskEngine(RobotTaskRepository robotTaskRepo) {
        this.robotTaskRepo = robotTaskRepo;
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param warehouseTaskId the warehouseTaskId input value
     * @param providerKey the providerKey input value
     * @param payloadJson the payloadJson input value
     * @return the RobotTask result
     */
    public RobotTask dispatchRobotTask(Long warehouseTaskId, String providerKey, String payloadJson) {
        RobotTask task = new RobotTask();
        task.setWarehouseTaskId(warehouseTaskId);
        task.setRobotProviderKey(providerKey);
        task.setPayloadJson(payloadJson);
        task.setStatus("DISPATCHED");
        return robotTaskRepo.save(task);
    }
}