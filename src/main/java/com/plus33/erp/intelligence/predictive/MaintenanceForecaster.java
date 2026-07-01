package com.plus33.erp.intelligence.predictive;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class MaintenanceForecaster {
    @Autowired PlatformPredictiveMaintenanceForecastRepository forecastRepo;
    @Autowired FailurePredictionService failurePredictionService;

    @Transactional
    public PlatformPredictiveMaintenanceForecast generateForecast(Long modelId, Long instanceId) {
        BigDecimal probability = failurePredictionService.predictFailureProbability(instanceId);

        PlatformPredictiveMaintenanceForecast f = new PlatformPredictiveMaintenanceForecast();
        f.setModelId(modelId);
        f.setTwinInstanceId(instanceId);
        f.setFailureProbability(probability);
        f.setExpectedFailureAt(LocalDateTime.now().plusDays(5));
        f.setGeneratedAt(LocalDateTime.now());
        return forecastRepo.save(f);
    }
}