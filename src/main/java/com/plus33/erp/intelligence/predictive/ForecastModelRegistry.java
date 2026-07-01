package com.plus33.erp.intelligence.predictive;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class ForecastModelRegistry {
    @Autowired PlatformForecastModelRegistryRepository registryRepo;

    @Transactional
    public PlatformForecastModelRegistry registerModel(String code, BigDecimal score) {
        PlatformForecastModelRegistry r = new PlatformForecastModelRegistry();
        r.setModelCode(code);
        r.setAccuracyScore(score);
        r.setStatus("ACTIVE");
        return registryRepo.save(r);
    }
}