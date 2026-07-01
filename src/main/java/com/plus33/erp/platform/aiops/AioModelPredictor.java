package com.plus33.erp.platform.aiops;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AioModelPredictor {
    @Autowired PlatformAiopsModelRepository modelRepo;
    @Autowired PlatformCapacityForecastRepository forecastRepo;
    @Autowired PlatformAiPredictionExplanationRepository explanationRepo;

    @Transactional
    public void registerModel(String code, double accuracy) {
        PlatformAiopsModel model = new PlatformAiopsModel();
        model.setModelName(code);
        model.setAccuracy(BigDecimal.valueOf(accuracy));
        model.setStatus("ACTIVE");
        modelRepo.save(model);
    }

    @Transactional
    public void runProjection(String metric, double forecastedVal, String reason) {
        PlatformCapacityForecast f = new PlatformCapacityForecast();
        f.setMetricName(metric);
        f.setForecastValue(BigDecimal.valueOf(forecastedVal));
        f.setConfidence(BigDecimal.valueOf(94.50));
        f.setTargetTime(LocalDateTime.now().plusHours(2));
        forecastRepo.save(f);

        PlatformAiPredictionExplanation exp = new PlatformAiPredictionExplanation();
        exp.setPredictionTarget(metric);
        exp.setReasoning(reason);
        exp.setConfidence(BigDecimal.valueOf(94.50));
        exp.setPredictionTime(LocalDateTime.now());
        explanationRepo.save(exp);
    }
}