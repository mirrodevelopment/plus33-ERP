package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.logistics.prediction.DelayPredictionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DelayPredictionTest {

    @Autowired DelayPredictionService delayPredictionService;

    @Autowired PlatformLogisticsDelayPredictionRepository predictionRepo;

    @Test
    void testDelayPredictionScenarios() {
        // Evaluate delay ETA estimations over 40 iterations
        for (int i = 1; i <= 40; i++) {
            delayPredictionService.predict((long) i, BigDecimal.valueOf(92.40));
        }

        List<PlatformLogisticsDelayPrediction> predictions = predictionRepo.findAll();
        assertTrue(predictions.size() >= 40);
        assertEquals("ETA_ESTIMATOR_V1", predictions.get(0).getPredictionModel());
    }
}
