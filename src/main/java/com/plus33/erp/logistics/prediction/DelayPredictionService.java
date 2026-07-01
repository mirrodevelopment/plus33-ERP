package com.plus33.erp.logistics.prediction;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class DelayPredictionService {
    @Autowired PlatformLogisticsDelayPredictionRepository predictionRepo;

    @Transactional
    public PlatformLogisticsDelayPrediction predict(Long routeId, BigDecimal confidence) {
        PlatformLogisticsDelayPrediction p = new PlatformLogisticsDelayPrediction();
        p.setTransitRouteId(routeId);
        p.setPredictionModel("ETA_ESTIMATOR_V1");
        p.setPredictionConfidence(confidence);
        p.setPredictedArrival(LocalDateTime.now().plusHours(4));
        p.setGeneratedAt(LocalDateTime.now());
        return predictionRepo.save(p);
    }
}