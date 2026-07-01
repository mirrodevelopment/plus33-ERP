package com.plus33.erp.intelligence.optimization;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrossModuleOptimizer {
    @Autowired PlatformOptimizationStrategyRepository strategyRepo;

    @Transactional
    public PlatformOptimizationStrategy registerStrategy(String code, String name, String params) {
        PlatformOptimizationStrategy strategy = new PlatformOptimizationStrategy();
        strategy.setStrategyCode(code);
        strategy.setStrategyName(name);
        strategy.setParametersJson(params);
        strategy.setActive(true);
        return strategyRepo.save(strategy);
    }
}