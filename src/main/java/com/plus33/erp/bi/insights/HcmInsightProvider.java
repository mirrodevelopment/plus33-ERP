/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.insights
 * File              : HcmInsightProvider.java
 * Purpose           : Component of Bi Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmInsightProviderController
 * Related Service   : HcmInsightProviderService, HcmInsightProviderServiceImpl
 * Related Repository: HcmInsightProviderRepository
 * Related Entity    : HcmInsightProvider
 * Related DTO       : N/A
 * Related Mapper    : HcmInsightProviderMapper
 * Related DB Table  : hcm_insight_providers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Bi Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.bi.insights;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code HcmInsightProvider}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.insights}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Bi Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class HcmInsightProvider implements AiInsightProvider {
    /**
     * Retrieves domain data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDomain() { return "HCM"; }
    /**
     * Generates the insight based on input parameters and business rules.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param kpiCode the kpiCode input value
     * @param varianceValue the varianceValue input value
     * @return the result string value
     */
    public String generateInsight(Long companyId, String kpiCode, BigDecimal varianceValue) {
        return "HCM Insight for " + kpiCode + ": variance of " + varianceValue + " is driven by cyclical contractor headcount.";
    }
}