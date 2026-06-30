package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.WarehouseRule;
import com.plus33.erp.wms.repository.WarehouseRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WarehouseRulesEngine {

    private final WarehouseRuleRepository ruleRepo;

    public WarehouseRulesEngine(WarehouseRuleRepository ruleRepo) {
        this.ruleRepo = ruleRepo;
    }

    public List<WarehouseRule> getActiveRules(Long companyId) {
        return ruleRepo.findByCompanyIdAndActiveTrueOrderByPriorityAsc(companyId);
    }
}
