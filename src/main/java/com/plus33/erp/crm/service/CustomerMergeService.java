/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.service
 * File              : CustomerMergeService.java
 * Purpose           : Business logic service layer for Crm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerMergeController
 * Related Service   : CustomerMergeService
 * Related Repository: CrmLeadRepository
 * Related Entity    : CustomerMerge
 * Related DTO       : N/A
 * Related Mapper    : CustomerMergeMapper
 * Related DB Table  : customer_merges
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerMergeController, CustomerMergeServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Crm Module. Implements CustomerMergeService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmLead;
import com.plus33.erp.crm.repository.CrmLeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerMergeService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Crm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CustomerMergeController
 *   --> CustomerMergeService (this)
 *   --> Validate business rules
 *   --> CustomerMergeRepository (read/write 'customer_merges')
 *   --> CustomerMergeMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code customer_merges}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class CustomerMergeService {

    private final CrmLeadRepository leadRepo;

    public CustomerMergeService(CrmLeadRepository leadRepo) {
        this.leadRepo = leadRepo;
    }

    /**
     * Performs the mergeLeads operation in this module.
     *
     * @param sourceLeadId the sourceLeadId input value
     * @param targetLeadId the targetLeadId input value
     */
    public void mergeLeads(Long sourceLeadId, Long targetLeadId) {
        CrmLead source = leadRepo.findById(sourceLeadId)
                .orElseThrow(() -> new IllegalArgumentException("Source lead not found: " + sourceLeadId));
        CrmLead target = leadRepo.findById(targetLeadId)
                .orElseThrow(() -> new IllegalArgumentException("Target lead not found: " + targetLeadId));

        target.setScore(target.getScore() + source.getScore());
        source.setStatus("MERGED");
        leadRepo.save(target);
        leadRepo.save(source);
    }
}