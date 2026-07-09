/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.service
 * File              : DuplicateDetectionEngine.java
 * Purpose           : Business logic service layer for Crm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DuplicateDetectionEngineController
 * Related Service   : DuplicateDetectionEngine
 * Related Repository: DuplicateDetectionEngineRepository
 * Related Entity    : DuplicateDetectionEngine
 * Related DTO       : N/A
 * Related Mapper    : DuplicateDetectionEngineMapper
 * Related DB Table  : duplicate_detection_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DuplicateDetectionEngineController, DuplicateDetectionEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Crm Module. Implements DuplicateDetectionEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmLead;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code DuplicateDetectionEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Crm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DuplicateDetectionEngineController
 *   --> DuplicateDetectionEngine (this)
 *   --> Validate business rules
 *   --> DuplicateDetectionEngineRepository (read/write 'duplicate_detection_engines')
 *   --> DuplicateDetectionEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code duplicate_detection_engines}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DuplicateDetectionEngine {

    /**
     * Performs the isPotentialDuplicate operation in this module.
     *
     * @param lead the lead input value
     * @param existingLeads the existingLeads input value
     * @return true if operation succeeded, false otherwise
     */
    public boolean isPotentialDuplicate(CrmLead lead, List<CrmLead> existingLeads) {
        return existingLeads.stream()
                .anyMatch(existing -> existing.getEmail() != null && 
                                      existing.getEmail().equalsIgnoreCase(lead.getEmail()));
    }
}