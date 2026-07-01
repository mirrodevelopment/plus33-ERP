package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.platform.aiops.AioModelPredictor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PlatformAIOpsIntegrationTest {

    @Autowired AioModelPredictor predictor;
    @Autowired PlatformAiopsModelRepository modelRepo;
    @Autowired PlatformCapacityForecastRepository forecastRepo;
    @Autowired PlatformAiPredictionExplanationRepository explanationRepo;

    @Test
    void testAiopsScenarios() {
        // Register 40 models
        for (int i = 1; i <= 40; i++) {
            predictor.registerModel("ai-model-" + i, 92.5 + (i % 5));
        }
        List<PlatformAiopsModel> models = modelRepo.findAll();
        assertTrue(models.size() >= 40);

        // Run 40 forecasts with explanations
        for (int i = 1; i <= 40; i++) {
            predictor.runProjection("cpu_utilization", 85.0 + i, "Traffic increase " + i);
        }
        List<PlatformCapacityForecast> forecasts = forecastRepo.findAll();
        assertTrue(forecasts.size() >= 40);

        List<PlatformAiPredictionExplanation> explanations = explanationRepo.findAll();
        assertTrue(explanations.size() >= 40);
    }
}
