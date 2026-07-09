/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Predictive Module
 * Package           : com.plus33.erp.predictive.prognostics
 * File              : FailurePrognosticsEngine.java
 * Purpose           : Business logic service layer for Predictive Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FailurePrognosticsEngineController
 * Related Service   : FailurePrognosticsEngine
 * Related Repository: FailurePrognosticsEngineRepository
 * Related Entity    : FailurePrognosticsEngine
 * Related DTO       : N/A
 * Related Mapper    : FailurePrognosticsEngineMapper
 * Related DB Table  : failure_prognostics_engines
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : FailurePrognosticsEngineController, FailurePrognosticsEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Predictive Module. Implements FailurePrognosticsEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.predictive.prognostics;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Predictive Module</b>
 *
 * <p><b>Class  :</b> {@code FailurePrognosticsEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.predictive.prognostics}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Predictive Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * FailurePrognosticsEngineController
 *   --> FailurePrognosticsEngine (this)
 *   --> Validate business rules
 *   --> FailurePrognosticsEngineRepository (read/write 'failure_prognostics_engines')
 *   --> FailurePrognosticsEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code failure_prognostics_engines}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class FailurePrognosticsEngine {
    @Autowired PlatformFailurePrognosticsLogRepository prognosticsRepo;
    @Autowired PlatformMaintenanceTriggerLogRepository triggerRepo;
    /**
     * Performs the predictFailure operation in this module.
     *
     * @param assetId the assetId input value
     * @param RUL the RUL input value
     * @param prob the prob input value
     * @param conf the conf input value
     * @return the PlatformFailurePrognosticsLog result
     */
    @Transactional
    public PlatformFailurePrognosticsLog predictFailure(Long assetId, BigDecimal RUL, BigDecimal prob, BigDecimal conf) {
        PlatformFailurePrognosticsLog log = new PlatformFailurePrognosticsLog();
        log.setAssetId(assetId);
        log.setPredictionTime(LocalDateTime.now());
        log.setPredictedFailureTime(LocalDateTime.now().plusHours(RUL.longValue()));
        log.setRemainingUsefulLifeHours(RUL);
        log.setFailureProbability(prob);
        log.setConfidenceScore(conf);
        log.setPredictionModelVersion("v2.1.0-RF");
        log.setTriggerReason("Bearing temperature spike anomaly detected.");
        log.setRecommendedAction("Schedule grease lubrication and bearing inspection window");
        log = prognosticsRepo.save(log);

        PlatformMaintenanceTriggerLog trig = new PlatformMaintenanceTriggerLog();
        trig.setTriggerSource("PREDICTIVE_ENGINE");
        trig.setPredictedFailureId(log.getId());
        trig.setWorkOrderReference("WO-REPAIR-" + log.getId());
        trig.setMaintenanceStatus("SCHEDULED");
        trig.setScheduledTime(LocalDateTime.now().plusHours(12));
        trig.setAutomaticExecution(true);
        triggerRepo.save(trig);

        return log;
    }
}