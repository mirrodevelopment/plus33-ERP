package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmLead;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DuplicateDetectionEngine {

    public boolean isPotentialDuplicate(CrmLead lead, List<CrmLead> existingLeads) {
        return existingLeads.stream()
                .anyMatch(existing -> existing.getEmail() != null && 
                                      existing.getEmail().equalsIgnoreCase(lead.getEmail()));
    }
}
