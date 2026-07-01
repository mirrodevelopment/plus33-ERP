package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmCommission;
import com.plus33.erp.crm.repository.CrmCommissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class CommissionEngine {

    private final CrmCommissionRepository commissionRepo;

    public CommissionEngine(CrmCommissionRepository commissionRepo) {
        this.commissionRepo = commissionRepo;
    }

    public CrmCommission calculateCommission(Long companyId, Long repId, Long opportunityId, BigDecimal dealAmount) {
        // Flat 5% base commission rate
        BigDecimal rate = new BigDecimal("0.05");
        BigDecimal commissionAmount = dealAmount.multiply(rate);

        CrmCommission comm = new CrmCommission();
        comm.setCompanyId(companyId);
        comm.setSalesRepId(repId);
        comm.setOpportunityId(opportunityId);
        comm.setAmount(commissionAmount);
        comm.setStatus("CALCULATED");
        return commissionRepo.save(comm);
    }
}
