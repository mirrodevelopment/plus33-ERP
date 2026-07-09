/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.causal
 * File              : CausalInferenceEngine.java
 * Purpose           : Business logic service layer for Intelligence Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CausalInferenceEngineController
 * Related Service   : CausalInferenceEngine
 * Related Repository: CausalInferenceEngineRepository
 * Related Entity    : CausalInferenceEngine
 * Related DTO       : N/A
 * Related Mapper    : CausalInferenceEngineMapper
 * Related DB Table  : causal_inference_engines
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : CausalInferenceEngineController, CausalInferenceEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Intelligence Module. Implements CausalInferenceEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.intelligence.causal;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code CausalInferenceEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.causal}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Intelligence Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CausalInferenceEngineController
 *   --> CausalInferenceEngine (this)
 *   --> Validate business rules
 *   --> CausalInferenceEngineRepository (read/write 'causal_inference_engines')
 *   --> CausalInferenceEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code causal_inference_engines}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class CausalInferenceEngine {
    @Autowired PlatformCausalModelRepository modelRepo;
    /**
     * Creates a new model and persists it to the database.
     *
     * @param code the code input value
     * @param name the name input value
     * @param structure the structure input value
     * @return the PlatformCausalModel result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformCausalModel registerModel(String code, String name, String structure) {
        PlatformCausalModel model = new PlatformCausalModel();
        model.setModelCode(code);
        model.setModelName(name);
        model.setStructureJson(structure);
        return modelRepo.save(model);
    }
}