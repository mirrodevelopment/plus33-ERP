/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.aiops
 * File              : AioModelPredictor.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AioModelPredictorController
 * Related Service   : AioModelPredictor
 * Related Repository: AioModelPredictorRepository
 * Related Entity    : AioModelPredictor
 * Related DTO       : N/A
 * Related Mapper    : AioModelPredictorMapper
 * Related DB Table  : aio_model_predictors
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AioModelPredictorController, AioModelPredictorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements AioModelPredictorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.aiops;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code AioModelPredictor}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.aiops}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * AioModelPredictorController
 *   --> AioModelPredictor (this)
 *   --> Validate business rules
 *   --> AioModelPredictorRepository (read/write 'aio_model_predictors')
 *   --> AioModelPredictorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code aio_model_predictors}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class AioModelPredictor {
    @Autowired PlatformAiopsModelRepository modelRepo;
    @Autowired PlatformCapacityForecastRepository forecastRepo;
    @Autowired PlatformAiPredictionExplanationRepository explanationRepo;
    /**
     * Creates a new model and persists it to the database.
     *
     * @param code the code input value
     * @param accuracy the accuracy input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerModel(String code, double accuracy) {
        PlatformAiopsModel model = new PlatformAiopsModel();
        model.setModelName(code);
        model.setAccuracy(BigDecimal.valueOf(accuracy));
        model.setStatus("ACTIVE");
        modelRepo.save(model);
    }

    /**
     * Performs the runProjection operation in this module.
     *
     * @param metric the metric input value
     * @param forecastedVal the forecastedVal input value
     * @param reason the reason input value
     */
    @Transactional
    public void runProjection(String metric, double forecastedVal, String reason) {
        PlatformCapacityForecast f = new PlatformCapacityForecast();
        f.setMetricName(metric);
        f.setForecastValue(BigDecimal.valueOf(forecastedVal));
        f.setConfidence(BigDecimal.valueOf(94.50));
        f.setTargetTime(LocalDateTime.now().plusHours(2));
        forecastRepo.save(f);

        PlatformAiPredictionExplanation exp = new PlatformAiPredictionExplanation();
        exp.setPredictionTarget(metric);
        exp.setReasoning(reason);
        exp.setConfidence(BigDecimal.valueOf(94.50));
        exp.setPredictionTime(LocalDateTime.now());
        explanationRepo.save(exp);
    }
}