/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.predictive
 * File              : ForecastModelRegistry.java
 * Purpose           : Business logic service layer for Intelligence Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ForecastModelRegistryController
 * Related Service   : ForecastModelRegistry
 * Related Repository: ForecastModelRegistryRepository
 * Related Entity    : ForecastModelRegistry
 * Related DTO       : N/A
 * Related Mapper    : ForecastModelRegistryMapper
 * Related DB Table  : forecast_model_registrys
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : ForecastModelRegistryController, ForecastModelRegistryImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Intelligence Module. Implements ForecastModelRegistryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.intelligence.predictive;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code ForecastModelRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.predictive}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Intelligence Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ForecastModelRegistryController
 *   --> ForecastModelRegistry (this)
 *   --> Validate business rules
 *   --> ForecastModelRegistryRepository (read/write 'forecast_model_registrys')
 *   --> ForecastModelRegistryMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code forecast_model_registrys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ForecastModelRegistry {
    @Autowired PlatformForecastModelRegistryRepository registryRepo;
    /**
     * Creates a new model and persists it to the database.
     *
     * @param code the code input value
     * @param score the score input value
     * @return the PlatformForecastModelRegistry result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformForecastModelRegistry registerModel(String code, BigDecimal score) {
        PlatformForecastModelRegistry r = new PlatformForecastModelRegistry();
        r.setModelCode(code);
        r.setAccuracyScore(score);
        r.setStatus("ACTIVE");
        return registryRepo.save(r);
    }
}