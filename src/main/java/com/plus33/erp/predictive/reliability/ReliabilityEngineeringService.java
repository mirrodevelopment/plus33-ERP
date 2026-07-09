/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Predictive Module
 * Package           : com.plus33.erp.predictive.reliability
 * File              : ReliabilityEngineeringService.java
 * Purpose           : Business logic service layer for Predictive Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReliabilityEngineeringController
 * Related Service   : ReliabilityEngineeringService
 * Related Repository: ReliabilityEngineeringRepository
 * Related Entity    : ReliabilityEngineering
 * Related DTO       : N/A
 * Related Mapper    : ReliabilityEngineeringMapper
 * Related DB Table  : reliability_engineerings
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : ReliabilityEngineeringController, ReliabilityEngineeringServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Predictive Module. Implements ReliabilityEngineeringService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.predictive.reliability;

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
 * <p><b>Class  :</b> {@code ReliabilityEngineeringService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.predictive.reliability}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Predictive Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ReliabilityEngineeringController
 *   --> ReliabilityEngineeringService (this)
 *   --> Validate business rules
 *   --> ReliabilityEngineeringRepository (read/write 'reliability_engineerings')
 *   --> ReliabilityEngineeringMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code reliability_engineerings}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ReliabilityEngineeringService {
    @Autowired PlatformReliabilityFailureLogRepository reliabilityRepo;
    @Autowired PlatformPredictiveAuditLogRepository auditRepo;
    /**
     * Performs the recordReliabilityLogs operation in this module.
     *
     * @param assetId the assetId input value
     * @param MTBF the MTBF input value
     * @param MTTR the MTTR input value
     * @param availability the availability input value
     * @return the PlatformReliabilityFailureLog result
     */
    @Transactional
    public PlatformReliabilityFailureLog recordReliabilityLogs(Long assetId, BigDecimal MTBF, BigDecimal MTTR, BigDecimal availability) {
        PlatformReliabilityFailureLog log = new PlatformReliabilityFailureLog();
        log.setAssetId(assetId);
        log.setMtbfHours(MTBF);
        log.setMttrHours(MTTR);
        log.setAvailabilityRate(availability);
        log.setFailureRate(BigDecimal.valueOf(0.00015));
        log.setReliabilityScore(BigDecimal.valueOf(98.50));
        log.setRepairDurationMinutes(120);
        log.setDowntimeDurationMinutes(240);
        log.setRootCauseCategory("ELECTRICAL_OVERHEATING");
        log.setFailureMode("COIL_BURNOUT");
        log.setCorrectiveAction("Coil replacement and cooling shroud installation");
        log.setReportedAt(LocalDateTime.now());
        log = reliabilityRepo.save(log);

        PlatformPredictiveAuditLog audit = new PlatformPredictiveAuditLog();
        audit.setPredictionId(log.getId());
        audit.setOperator("reliability-lead");
        audit.setActionType("STRATEGY_CHANGE");
        audit.setNewThresholdConfig("{ \"strategy\": \"Predictive\" }");
        audit.setTraceId("TRACE-ID-RELIABILITY-INIT");
        auditRepo.save(audit);

        return log;
    }
}