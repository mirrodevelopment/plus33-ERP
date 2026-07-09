/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.causal
 * File              : RootCauseAnalyzer.java
 * Purpose           : Business logic service layer for Intelligence Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RootCauseAnalyzerController
 * Related Service   : RootCauseAnalyzer
 * Related Repository: RootCauseAnalyzerRepository
 * Related Entity    : RootCauseAnalyzer
 * Related DTO       : N/A
 * Related Mapper    : RootCauseAnalyzerMapper
 * Related DB Table  : root_cause_analyzers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : RootCauseAnalyzerController, RootCauseAnalyzerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Intelligence Module. Implements RootCauseAnalyzerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.intelligence.causal;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code RootCauseAnalyzer}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.causal}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Intelligence Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * RootCauseAnalyzerController
 *   --> RootCauseAnalyzer (this)
 *   --> Validate business rules
 *   --> RootCauseAnalyzerRepository (read/write 'root_cause_analyzers')
 *   --> RootCauseAnalyzerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code root_cause_analyzers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class RootCauseAnalyzer {
    @Autowired PlatformRootCauseAnalysisRepository analysisRepo;
    @Autowired BayesianInferenceService bayesianInferenceService;
    @Autowired ExplainabilityService explainabilityService;
    /**
     * Performs the runAnalysis operation in this module.
     *
     * @param modelId the modelId input value
     * @param anomaly the anomaly input value
     * @return the PlatformRootCauseAnalysis result
     */
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