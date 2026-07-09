/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.ai
 * File              : AiPredictionRegistry.java
 * Purpose           : Business logic service layer for Crm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AiPredictionRegistryController
 * Related Service   : AiPredictionRegistry
 * Related Repository: AiPredictionRegistryRepository
 * Related Entity    : AiPredictionRegistry
 * Related DTO       : N/A
 * Related Mapper    : AiPredictionRegistryMapper
 * Related DB Table  : ai_prediction_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AiPredictionRegistryController, AiPredictionRegistryImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Crm Module. Implements AiPredictionRegistryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.crm.ai;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code AiPredictionRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.ai}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Crm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * AiPredictionRegistryController
 *   --> AiPredictionRegistry (this)
 *   --> Validate business rules
 *   --> AiPredictionRegistryRepository (read/write 'ai_prediction_registrys')
 *   --> AiPredictionRegistryMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code ai_prediction_registrys}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class AiPredictionRegistry {

    private final Map<String, AiPredictionProvider> providers = new HashMap<>();

    public AiPredictionRegistry(List<AiPredictionProvider> predictionProviders) {
        predictionProviders.forEach(p -> providers.put(p.getProviderName().toUpperCase(), p));
    }

    /**
     * Retrieves provider data from the database.
     *
     * @param name the name input value
     * @return the AiPredictionProvider result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public AiPredictionProvider getProvider(String name) {
        return providers.get(name.toUpperCase());
    }
}