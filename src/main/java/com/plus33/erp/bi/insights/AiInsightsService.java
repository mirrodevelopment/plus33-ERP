package com.plus33.erp.bi.insights;

import com.plus33.erp.bi.entity.BiAiInsightGeneration;
import com.plus33.erp.bi.repository.BiAiInsightGenerationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AiInsightsService {

    @Autowired BiAiInsightGenerationRepository insightRepo;
    @Autowired AiInsightProviderRegistry providerRegistry;

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

    @Transactional
    public BiAiInsightGeneration recordFeedback(Long insightId, int rating, boolean accepted) {
        BiAiInsightGeneration gen = insightRepo.findById(insightId)
                .orElseThrow(() -> new IllegalArgumentException("Insight not found: " + insightId));
        gen.setFeedbackRating(rating);
        gen.setAcceptedByUser(accepted);
        return insightRepo.save(gen);
    }
}