package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.*;
import com.plus33.erp.hcm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class SuccessionPlanningService {

    private final TalentPoolRepository talentPoolRepository;
    private final SuccessorRepository successorRepository;

    public SuccessionPlanningService(TalentPoolRepository talentPoolRepository, SuccessorRepository successorRepository) {
        this.talentPoolRepository = talentPoolRepository;
        this.successorRepository = successorRepository;
    }

    @Transactional
    public TalentPool createTalentPool(String name, String desc) {
        TalentPool pool = new TalentPool();
        pool.setName(name);
        pool.setDescription(desc);
        talentPoolRepository.save(pool);
        return pool;
    }

    @Transactional
    public Successor nominateSuccessor(Long talentPoolId, Long employeeId, BigDecimal readiness) {
        Successor succ = new Successor();
        succ.setTalentPoolId(talentPoolId);
        succ.setEmployeeId(employeeId);
        succ.setReadinessScore(readiness);
        successorRepository.save(succ);
        return succ;
    }
}
