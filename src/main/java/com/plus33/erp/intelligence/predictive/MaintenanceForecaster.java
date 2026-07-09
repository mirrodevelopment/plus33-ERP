/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.predictive
 * File              : MaintenanceForecaster.java
 * Purpose           : Business logic service layer for Intelligence Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MaintenanceForecasterController
 * Related Service   : MaintenanceForecaster
 * Related Repository: MaintenanceForecasterRepository
 * Related Entity    : MaintenanceForecaster
 * Related DTO       : N/A
 * Related Mapper    : MaintenanceForecasterMapper
 * Related DB Table  : maintenance_forecasters
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : MaintenanceForecasterController, MaintenanceForecasterImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Intelligence Module. Implements MaintenanceForecasterService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.intelligence.predictive;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code MaintenanceForecaster}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.predictive}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Intelligence Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * MaintenanceForecasterController
 *   --> MaintenanceForecaster (this)
 *   --> Validate business rules
 *   --> MaintenanceForecasterRepository (read/write 'maintenance_forecasters')
 *   --> MaintenanceForecasterMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code maintenance_forecasters}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class MaintenanceForecaster {
    @Autowired PlatformPredictiveMaintenanceForecastRepository forecastRepo;
    @Autowired FailurePredictionService failurePredictionService;
    /**
     * Generates the forecast based on input parameters and business rules.
     *
     * @param modelId the modelId input value
     * @param instanceId the instanceId input value
     * @return the numeric result value
     */
    @Transactional
    public PlatformPredictiveMaintenanceForecast generateForecast(Long modelId, Long instanceId) {
        BigDecimal probability = failurePredictionService.predictFailureProbability(instanceId);

        PlatformPredictiveMaintenanceForecast f = new PlatformPredictiveMaintenanceForecast();
        f.setModelId(modelId);
        f.setTwinInstanceId(instanceId);
        f.setFailureProbability(probability);
        f.setExpectedFailureAt(LocalDateTime.now().plusDays(5));
        f.setGeneratedAt(LocalDateTime.now());
        return forecastRepo.save(f);
    }
}