/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.service
 * File              : OpportunityForecastEngine.java
 * Purpose           : Business logic service layer for Crm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OpportunityForecastEngineController
 * Related Service   : OpportunityForecastEngine
 * Related Repository: OpportunityForecastEngineRepository
 * Related Entity    : OpportunityForecastEngine
 * Related DTO       : N/A
 * Related Mapper    : OpportunityForecastEngineMapper
 * Related DB Table  : opportunity_forecast_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : OpportunityForecastEngineController, OpportunityForecastEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Crm Module. Implements OpportunityForecastEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmOpportunity;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code OpportunityForecastEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Crm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * OpportunityForecastEngineController
 *   --> OpportunityForecastEngine (this)
 *   --> Validate business rules
 *   --> OpportunityForecastEngineRepository (read/write 'opportunity_forecast_engines')
 *   --> OpportunityForecastEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code opportunity_forecast_engines}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class OpportunityForecastEngine {

    /**
     * Calculates forecasts totals including subtotal, tax, discounts, and net amount.
     *
     * @param opps the opps input value
     * @return the result string value
     */
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