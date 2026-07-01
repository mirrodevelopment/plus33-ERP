package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmLead;
import com.plus33.erp.crm.repository.CrmLeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerMergeService {

    private final CrmLeadRepository leadRepo;

    public CustomerMergeService(CrmLeadRepository leadRepo) {
        this.leadRepo = leadRepo;
    }

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
