/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.insights
 * File              : ManufacturingInsightProvider.java
 * Purpose           : Component of Bi Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingInsightProviderController
 * Related Service   : ManufacturingInsightProviderService, ManufacturingInsightProviderServiceImpl
 * Related Repository: ManufacturingInsightProviderRepository
 * Related Entity    : ManufacturingInsightProvider
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingInsightProviderMapper
 * Related DB Table  : manufacturing_insight_providers
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
 * <p><b>Class  :</b> {@code ManufacturingInsightProvider}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.insights}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Bi Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class ManufacturingInsightProvider implements AiInsightProvider {
    /**
     * Retrieves domain data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDomain() { return "MANUFACTURING"; }
    /**
     * Generates the insight based on input parameters and business rules.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param kpiCode the kpiCode input value
     * @param varianceValue the varianceValue input value
     * @return the result string value
     */
    public String generateInsight(Long companyId, String kpiCode, BigDecimal varianceValue) {
        return "Manufacturing Insight for " + kpiCode + ": variance of " + varianceValue + " indicates shopfloor capacity changes.";
    }
}