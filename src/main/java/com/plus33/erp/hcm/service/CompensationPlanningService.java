/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.service
 * File              : CompensationPlanningService.java
 * Purpose           : Business logic service layer for Hcm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CompensationPlanningController
 * Related Service   : CompensationPlanningService
 * Related Repository: CompensationHistoryRepository
 * Related Entity    : CompensationPlanning
 * Related DTO       : N/A
 * Related Mapper    : CompensationPlanningMapper
 * Related DB Table  : compensation_plannings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CompensationPlanningController, CompensationPlanningServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Hcm Module. Implements CompensationPlanningService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.CompensationHistory;
import com.plus33.erp.hcm.repository.CompensationHistoryRepository;
import com.plus33.erp.hcm.event.HcmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code CompensationPlanningService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Hcm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CompensationPlanningController
 *   --> CompensationPlanningService (this)
 *   --> Validate business rules
 *   --> CompensationPlanningRepository (read/write 'compensation_plannings')
 *   --> CompensationPlanningMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code compensation_plannings}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class CompensationPlanningService {

    private final CompensationHistoryRepository compensationRepository;
    private final HcmEventBus eventBus;

    public CompensationPlanningService(CompensationHistoryRepository compensationRepository, HcmEventBus eventBus) {
        this.compensationRepository = compensationRepository;
        this.eventBus = eventBus;
    }

    /**
     * Performs the reviseSalary operation in this module.
     *
     * @param employeeId the employeeId input value
     * @param newSalary the newSalary input value
     * @param notes the notes input value
     * @return the CompensationHistory result
     */
    @Transactional
    public CompensationHistory reviseSalary(Long employeeId, BigDecimal newSalary, String notes) {
        CompensationHistory history = new CompensationHistory();
        history.setEmployeeId(employeeId);
        history.setEffectiveDate(LocalDate.now());
        history.setBaseSalary(newSalary);
        history.setNotes(notes);
        compensationRepository.save(history);

        eventBus.publish("SalaryChanged", 1L, employeeId, "Salary updated to " + newSalary);
        return history;
    }
}