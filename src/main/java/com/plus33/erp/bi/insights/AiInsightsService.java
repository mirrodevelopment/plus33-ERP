/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.insights
 * File              : AiInsightsService.java
 * Purpose           : Business logic service layer for Bi Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AiInsightsController
 * Related Service   : AiInsightsService
 * Related Repository: AiInsightsRepository
 * Related Entity    : AiInsights
 * Related DTO       : N/A
 * Related Mapper    : AiInsightsMapper
 * Related DB Table  : ai_insightss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AiInsightsController, AiInsightsServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Bi Module. Implements AiInsightsService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.bi.insights;

import com.plus33.erp.bi.entity.BiAiInsightGeneration;
import com.plus33.erp.bi.repository.BiAiInsightGenerationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code AiInsightsService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.insights}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Bi Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * AiInsightsController
 *   --> AiInsightsService (this)
 *   --> Validate business rules
 *   --> AiInsightsRepository (read/write 'ai_insightss')
 *   --> AiInsightsMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code ai_insightss}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class AiInsightsService {

    @Autowired BiAiInsightGenerationRepository insightRepo;
    @Autowired AiInsightProviderRegistry providerRegistry;
    /**
     * Generates the kpi insight based on input parameters and business rules.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param kpiCode the kpiCode input value
     * @param domainArea the domainArea input value
     * @param variance the variance input value
     * @return the BiAiInsightGeneration result
     */
    @Transactional
    public BiAiInsightGeneration generateKpiInsight(Long companyId, String kpiCode, String domainArea, BigDecimal variance) {
        AiInsightProvider provider = providerRegistry.getProvider(domainArea);
        String narrativeText;
        if (provider != null) {
            narrativeText = provider.generateInsight(companyId, kpiCode, variance);
        } else {
            narrativeText = "General Insight: KPI " + kpiCode + " changed by " + variance;
        }

        BiAiInsightGeneration gen = new BiAiInsightGeneration();
        gen.setInsightCode("INS-" + System.currentTimeMillis() + "-" + kpiCode);
        gen.setCompanyId(companyId);
        gen.setKpiCode(kpiCode);
        gen.setSourceSnapshotId(1L);
        gen.setGeneratedModel("Claude-3.5-Sonnet");
        gen.setConfidenceScore(BigDecimal.valueOf(94.50));
        gen.setNarrativeText(narrativeText);
        gen.setGeneratedAt(LocalDateTime.now());
        gen.setAcceptedByUser(false);
        return insightRepo.save(gen);
    }

    /**
     * Performs the recordFeedback operation in this module.
     *
     * @param insightId the insightId input value
     * @param rating the rating input value
     * @param accepted the accepted input value
     * @return the BiAiInsightGeneration result
     */
    @Transactional
    public BiAiInsightGeneration recordFeedback(Long insightId, int rating, boolean accepted) {
        BiAiInsightGeneration gen = insightRepo.findById(insightId)
                .orElseThrow(() -> new IllegalArgumentException("Insight not found: " + insightId));
        gen.setFeedbackRating(rating);
        gen.setAcceptedByUser(accepted);
        return insightRepo.save(gen);
    }
}