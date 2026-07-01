package com.plus33.erp.intelligence.causal;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class RootCauseAnalyzer {
    @Autowired PlatformRootCauseAnalysisRepository analysisRepo;
    @Autowired BayesianInferenceService bayesianInferenceService;
    @Autowired ExplainabilityService explainabilityService;

    @Transactional
    public PlatformRootCauseAnalysis runAnalysis(Long modelId, String anomaly) {
        String probs = bayesianInferenceService.calculateProbabilities(modelId, anomaly);
        String cause = "Sensor_A_Failure";
        String explanation = explainabilityService.generateExplanation(cause, probs);

        PlatformRootCauseAnalysis analysis = new PlatformRootCauseAnalysis();
        analysis.setCausalModelId(modelId);
        analysis.setAnomalyEvent(anomaly);
        analysis.setProbabilitiesJson(probs);
        analysis.setRootCauseNode(cause);
        analysis.setExplanation(explanation);
        analysis.setAnalyzedAt(LocalDateTime.now());
        return analysisRepo.save(analysis);
    }
}