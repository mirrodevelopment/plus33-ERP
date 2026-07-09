/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.insights
 * File              : AiInsightProviderRegistry.java
 * Purpose           : Component of Bi Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AiInsightProviderRegistryController
 * Related Service   : AiInsightProviderRegistryService, AiInsightProviderRegistryServiceImpl
 * Related Repository: AiInsightProviderRegistryRepository
 * Related Entity    : AiInsightProviderRegistry
 * Related DTO       : N/A
 * Related Mapper    : AiInsightProviderRegistryMapper
 * Related DB Table  : ai_insight_provider_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Bi Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.bi.insights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code AiInsightProviderRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.insights}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Bi Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class AiInsightProviderRegistry {

    private final Map<String, AiInsightProvider> providers = new HashMap<>();

    @Autowired
    public AiInsightProviderRegistry(List<AiInsightProvider> providerList) {
        for (AiInsightProvider p : providerList) {
            providers.put(p.getDomain().toUpperCase(), p);
        }
    }

    /**
     * Retrieves provider data from the database.
     *
     * @param domain the domain input value
     * @return the AiInsightProvider result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public AiInsightProvider getProvider(String domain) {
        return providers.get(domain.toUpperCase());
    }
}