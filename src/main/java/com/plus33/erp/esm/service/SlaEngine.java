package com.plus33.erp.esm.service;

import com.plus33.erp.crm.entity.CrmCase;
import com.plus33.erp.crm.repository.CrmCaseRepository;
import com.plus33.erp.esm.event.EsmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SlaEngine {

    private final CrmCaseRepository caseRepository;
    private final EsmEventBus eventBus;

    public SlaEngine(CrmCaseRepository caseRepository, EsmEventBus eventBus) {
        this.caseRepository = caseRepository;
        this.eventBus = eventBus;
    }

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
