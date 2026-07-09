/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.service
 * File              : EmployeeLifecycleService.java
 * Purpose           : Business logic service layer for Hcm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeLifecycleController
 * Related Service   : EmployeeLifecycleService
 * Related Repository: EmployeeLifecycleRepository
 * Related Entity    : EmployeeLifecycle
 * Related DTO       : N/A
 * Related Mapper    : EmployeeLifecycleMapper
 * Related DB Table  : employee_lifecycles
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeLifecycleController, EmployeeLifecycleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Hcm Module. Implements EmployeeLifecycleService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.EmployeeLifecycle;
import com.plus33.erp.hcm.repository.EmployeeLifecycleRepository;
import com.plus33.erp.hcm.event.HcmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeLifecycleService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Hcm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * EmployeeLifecycleController
 *   --> EmployeeLifecycleService (this)
 *   --> Validate business rules
 *   --> EmployeeLifecycleRepository (read/write 'employee_lifecycles')
 *   --> EmployeeLifecycleMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code employee_lifecycles}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class EmployeeLifecycleService {

    private final EmployeeLifecycleRepository lifecycleRepository;
    private final HcmEventBus eventBus;

    public EmployeeLifecycleService(EmployeeLifecycleRepository lifecycleRepository, HcmEventBus eventBus) {
        this.lifecycleRepository = lifecycleRepository;
        this.eventBus = eventBus;
    }

    /**
     * Initializes the lifecycle configuration and prepares default state.
     *
     * @param employeeId the employeeId input value
     * @return the EmployeeLifecycle result
     */
    @Transactional
    public EmployeeLifecycle initializeLifecycle(Long employeeId) {
        EmployeeLifecycle lc = new EmployeeLifecycle();
        lc.setEmployeeId(employeeId);
        lc.setLifecycleStatus("CANDIDATE");
        lifecycleRepository.save(lc);
        return lc;
    }

    /**
     * Performs the transitionStatus operation in this module.
     *
     * @param employeeId the employeeId input value
     * @param targetStatus the targetStatus input value
     */
    @Transactional
    public void transitionStatus(Long employeeId, String targetStatus) {
        EmployeeLifecycle lc = lifecycleRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Lifecycle profile not found"));

        lc.setLifecycleStatus(targetStatus);
        lc.setUpdatedAt(LocalDateTime.now());
        lifecycleRepository.save(lc);

        if ("CONFIRMED".equals(targetStatus)) {
            eventBus.publish("EmployeeConfirmed", 1L, employeeId, "Employee status confirmed");
        } else if ("SEPARATED".equals(targetStatus)) {
            eventBus.publish("EmployeeSeparated", 1L, employeeId, "Employee separated");
        }
    }
}