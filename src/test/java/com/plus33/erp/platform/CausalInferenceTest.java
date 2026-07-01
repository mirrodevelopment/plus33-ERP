package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.intelligence.causal.CausalInferenceEngine;
import com.plus33.erp.intelligence.causal.RootCauseAnalyzer;
import com.plus33.erp.intelligence.query.NaturalLanguageQueryService;
import com.plus33.erp.intelligence.query.DecisionLineageTracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CausalInferenceTest {

    @Autowired CausalInferenceEngine causalEngine;
    @Autowired RootCauseAnalyzer rootCauseAnalyzer;
    @Autowired NaturalLanguageQueryService queryService;
    @Autowired DecisionLineageTracker lineageTracker;

    @Autowired PlatformCausalModelRepository modelRepo;
    @Autowired PlatformRootCauseAnalysisRepository analysisRepo;
    @Autowired PlatformOperationalQueryLogRepository logRepo;
    @Autowired PlatformXaiLineageRepository lineageRepo;

    @Test
    void testCausalInferenceScenarios() {
        // Register causal models over 40 iterations
        for (int i = 1; i <= 40; i++) {
            causalEngine.registerModel("causal-code-" + i, "Bayesian Pump Network " + i, "{}");
        }
        List<PlatformCausalModel> models = modelRepo.findAll();
        assertTrue(models.size() >= 40);

        // Run root cause ranking analyses over 40 iterations
        PlatformCausalModel testModel = models.get(0);
        for (int i = 1; i <= 40; i++) {
            rootCauseAnalyzer.runAnalysis(testModel.getId(), "PRESSURE_DROP_" + i);
        }
        List<PlatformRootCauseAnalysis> analyses = analysisRepo.findAll();
        assertTrue(analyses.size() >= 40);

        // Run operational queries over 40 iterations
        for (int i = 1; i <= 40; i++) {
            queryService.executeQuery("What caused pressure drop variant " + i + "?");
        }
        List<PlatformOperationalQueryLog> logs = logRepo.findAll();
        assertTrue(logs.size() >= 40);

        // Record explainable AI lineages over 40 iterations
        for (int i = 1; i <= 40; i++) {
            lineageTracker.recordLineage("dec-key-" + i, "vibration, high temperature factor", "v1.0.0");
        }
        List<PlatformXaiLineage> lineages = lineageRepo.findAll();
        assertTrue(lineages.size() >= 40);
    }
}
