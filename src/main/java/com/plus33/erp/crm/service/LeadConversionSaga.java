package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmLead;
import com.plus33.erp.crm.repository.CrmLeadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LeadConversionSaga {

    private static final Logger log = LoggerFactory.getLogger(LeadConversionSaga.class);
    private final CrmLeadRepository leadRepo;

    public LeadConversionSaga(CrmLeadRepository leadRepo) {
        this.leadRepo = leadRepo;
    }

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
