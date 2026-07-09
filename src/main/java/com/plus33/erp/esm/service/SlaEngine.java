/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.service
 * File              : SlaEngine.java
 * Purpose           : Business logic service layer for Esm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SlaEngineController
 * Related Service   : SlaEngine
 * Related Repository: CrmCaseRepository
 * Related Entity    : SlaEngine
 * Related DTO       : N/A
 * Related Mapper    : SlaEngineMapper
 * Related DB Table  : sla_engines
 * Related REST APIs : N/A
 * Depends On        : Crm Module
 * Used By           : SlaEngineController, SlaEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Esm Module. Implements SlaEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.esm.service;

import com.plus33.erp.crm.entity.CrmCase;
import com.plus33.erp.crm.repository.CrmCaseRepository;
import com.plus33.erp.esm.event.EsmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code SlaEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Esm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SlaEngineController
 *   --> SlaEngine (this)
 *   --> Validate business rules
 *   --> SlaEngineRepository (read/write 'sla_engines')
 *   --> SlaEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code sla_engines}</p>
 * <p><b>Module Deps      :</b> Crm, Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class SlaEngine {

    private final CrmCaseRepository caseRepository;
    private final EsmEventBus eventBus;

    public SlaEngine(CrmCaseRepository caseRepository, EsmEventBus eventBus) {
        this.caseRepository = caseRepository;
        this.eventBus = eventBus;
    }

    /**
     * Validates business rules and constraints for sla.
     *
     * @param caseId the caseId input value
     * @return true if operation succeeded, false otherwise
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public boolean checkSla(Long caseId) {
        CrmCase customerCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Case not found"));

        // Determine resolution limit based on priority
        int limitHours = 24;
        if ("URGENT".equals(customerCase.getPriority())) {
            limitHours = 4;
        } else if ("HIGH".equals(customerCase.getPriority())) {
            limitHours = 8;
        }

        LocalDateTime deadline = customerCase.getCreatedAt().plusHours(limitHours);
        boolean breached = LocalDateTime.now().isAfter(deadline);

        if (breached && !customerCase.isSlaBreached()) {
            customerCase.setSlaBreached(true);
            caseRepository.save(customerCase);
            eventBus.publish("SlaBreached", customerCase.getCompanyId(), caseId, "SLA Breached for Case " + customerCase.getCaseNumber());
        }

        return breached;
    }
}