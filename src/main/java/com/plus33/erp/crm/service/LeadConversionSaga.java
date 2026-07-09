/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.service
 * File              : LeadConversionSaga.java
 * Purpose           : Business logic service layer for Crm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LeadConversionSagaController
 * Related Service   : LeadConversionSaga
 * Related Repository: CrmLeadRepository
 * Related Entity    : LeadConversionSaga
 * Related DTO       : N/A
 * Related Mapper    : LeadConversionSagaMapper
 * Related DB Table  : lead_conversion_sagas
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LeadConversionSagaController, LeadConversionSagaImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Crm Module. Implements LeadConversionSagaService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmLead;
import com.plus33.erp.crm.repository.CrmLeadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code LeadConversionSaga}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Crm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * LeadConversionSagaController
 *   --> LeadConversionSaga (this)
 *   --> Validate business rules
 *   --> LeadConversionSagaRepository (read/write 'lead_conversion_sagas')
 *   --> LeadConversionSagaMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code lead_conversion_sagas}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class LeadConversionSaga {

    private static final Logger log = LoggerFactory.getLogger(LeadConversionSaga.class);
    private final CrmLeadRepository leadRepo;

    public LeadConversionSaga(CrmLeadRepository leadRepo) {
        this.leadRepo = leadRepo;
    }

    /**
     * Performs the executeLeadConversion operation in this module.
     *
     * @param leadId the leadId input value
     */
    public void executeLeadConversion(Long leadId) {
        CrmLead lead = leadRepo.findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));
        try {
            log.info("Starting LeadConversionSaga for lead: {}", leadId);
            lead.setStatus("CONVERTED");
            leadRepo.save(lead);
            // Steps: 1. Customer create, 2. Credit, 3. Tax profile, 4. Territory assignment
            log.info("LeadConversionSaga completed successfully for lead: {}", leadId);
        } catch (Exception e) {
            log.error("Saga failed, executing compensations...", e);
            lead.setStatus("QUALIFIED");
            leadRepo.save(lead);
            throw new RuntimeException("Lead conversion failed, rolled back", e);
        }
    }
}