package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmOpportunity;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class OpportunityForecastEngine {

    public Map<String, BigDecimal> calculateForecasts(List<CrmOpportunity> opps) {
        BigDecimal pipeline = BigDecimal.ZERO;
        BigDecimal weighted = BigDecimal.ZERO;
        BigDecimal commit = BigDecimal.ZERO;

        for (CrmOpportunity opp : opps) {
            BigDecimal amt = opp.getAmount();
            BigDecimal prob = opp.getProbability().divide(BigDecimal.valueOf(100));
            pipeline = pipeline.add(amt);
            weighted = weighted.add(amt.multiply(prob));
            if ("CONTRACT_REVIEW".equals(opp.getStage()) || "VERBAL_COMMIT".equals(opp.getStage())) {
                commit = commit.add(amt);
            }
        }

        return Map.of(
                "pipelineForecast", pipeline,
                "weightedForecast", weighted,
                "commitForecast", commit
        );
    }
}
