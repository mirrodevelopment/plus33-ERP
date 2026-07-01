package com.plus33.erp.intelligence.optimization;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ReinforcementPolicyEngine {
    @Autowired PlatformRlPolicyRepository policyRepo;
    @Autowired PlatformRlPolicyUpdateRepository updateRepo;

    @Transactional
    public void trainPolicy(String code, String action, BigDecimal reward) {
        PlatformRlPolicy policy = policyRepo.findAll().stream()
                .filter(p -> p.getPolicyCode().equals(code))
                .findFirst()
                .orElseGet(() -> {
                    PlatformRlPolicy p = new PlatformRlPolicy();
                    p.setPolicyCode(code);
                    p.setCurrentStateJson("{}");
                    return policyRepo.save(p);
                });

        PlatformRlPolicyUpdate update = new PlatformRlPolicyUpdate();
        update.setPolicyId(policy.getId());
        update.setActionTaken(action);
        update.setReward(reward);
        update.setStateJson("{\"trained\": true}");
        update.setUpdatedAt(LocalDateTime.now());
        updateRepo.save(update);
    }
}